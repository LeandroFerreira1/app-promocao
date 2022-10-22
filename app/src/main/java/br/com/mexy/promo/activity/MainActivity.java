package br.com.mexy.promo.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.fragment.BottomSheetPromocao;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.util.Permissao;
import br.com.mexy.promo.util.StaticInstances;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Retrofit retrofit;
    private Map<Marker, Map<String, Object>> markers = new HashMap<>();

    FloatingActionButton floatingActionButton;
    BottomAppBar bottomAppBar;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private List<Estabelecimento> estabelecimentos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomAppBar = findViewById(R.id.bar);
        //main line for setting menu in bottom app bar
        setSupportActionBar(bottomAppBar);

        //Validar permissões
        Permissao.validarPermissoes(permissoes, this, 1);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recuperarEstabelecimentos();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.botton_app_perfil :


                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
                String res = sharedPreferences.getString("ID_USUARIO", null);

                if (res == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    Intent intent = new Intent(getBaseContext(), PerfilActivity.class);
                    startActivity(intent);
                }

                break;
           /* case R.id.botton_app_search :
                startActivity(new Intent(getApplicationContext(), PesquisaActivity.class));
                break;*/
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_style));


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Map dataModel = (Map) markers.get(marker);
                final BottomSheetPromocao bottomSheetPromocao = new BottomSheetPromocao();

                Bundle bundle = new Bundle();
                bundle.putInt("idEstabelecimento", (Integer) dataModel.get("idEstabelecimento"));

                bottomSheetPromocao.setArguments(bundle);
                bottomSheetPromocao.show(getSupportFragmentManager(), bottomSheetPromocao.getTag());

                return false;
            }
        });


        //Objeto responsável por gerenciar a localização do usuário
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Localizacao", "onLocationChanged: " + location.toString());

                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                //Ativa Bolinha Azul e Botao de Busca
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                //-23.593054, -46.663584
                //-23.590679, -46.652288
                LatLng localUsuario = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localUsuario,15));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        /*
         * 1) Provedor da localização
         * 2) Tempo mínimo entre atualizacões de localização (milesegundos)
         * 3) Distancia mínima entre atualizacões de localização (metros)
         * 4) Location listener (para recebermos as atualizações)
         * */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000000000,
                    40,
                    locationListener
            );
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {

            //permission denied (negada)
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                //Alerta
                alertaValidacaoPermissao();
            } else if (permissaoResultado == PackageManager.PERMISSION_GRANTED) {
                //Recuperar localizacao do usuario

                /*
                 * 1) Provedor da localização
                 * 2) Tempo mínimo entre atualizacões de localização (milesegundos)
                 * 3) Distancia mínima entre atualizacões de localização (metros)
                 * 4) Location listener (para recebermos as atualizações)
                 * */
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0,
                            locationListener
                    );
                }

            }
        }

    }



    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void recuperarEstabelecimentos() {

        DataService service = retrofit.create(DataService.class);
        Call<List<Estabelecimento>> estabelecimentoCall = service.recuperarEstabelecimentosPromo();

        estabelecimentoCall.enqueue(new Callback<List<Estabelecimento>>() {
            @Override
            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                if (response.isSuccessful()) {

                    StaticInstances.estabelecimentos.clear();
                    StaticInstances.estabelecimentos.addAll(response.body());
                    estabelecimentos = StaticInstances.estabelecimentos;

                    for (Estabelecimento a : estabelecimentos) {

                        Map<String, Object> dataModel = new HashMap<>();
                        dataModel.put("title", a.getNome());
                        dataModel.put("latitude", a.getLongitude());
                        dataModel.put("longitude", a.getLatitude());
                        dataModel.put("idEstabelecimento", a.getId());


                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(a.getLatitude()), Double.parseDouble(a.getLongitude())))
                                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("pin",110,158))));
                        markers.put(marker, dataModel);

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                // TODO
            }
        });

    }

    public Bitmap resizeBitmap(String drawableName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(drawableName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

}