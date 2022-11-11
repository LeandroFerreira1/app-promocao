package br.com.mexy.promo.activity;

import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.fragment.BottomSheetCadastroProduto;
import br.com.mexy.promo.model.Departamento;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Produto;
import br.com.mexy.promo.model.Result;
import br.com.mexy.promo.util.StaticInstances;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProdutoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String KEY_ALLOW_MANUAL_INPUT = "allow_manual_input";

    private boolean allowManualInput;
    private TextView barcodeResultView;
    private String codigo;
    private Retrofit retrofit;
    private List<Departamento> departamentos = new ArrayList<>();
    private TextView editNomeProduto;
    private TextView editMarca;
    private ImageView imageViewProduto;
    private ImageButton imageButtonFoto;
    private Result result;
    private Produto produtoAlterado = new Produto();
    private Produto produtoAlteradoCompleto = new Produto();
    private Produto produto = new Produto();
    private ProgressBar progressBar;
    private Spinner spinner;
    private Integer idDepartamento = 0;
    private Button btnSalvar;
    private BigInteger idProduto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        spinner = (Spinner) findViewById(R.id.spinnerDepartamento);

        progressBar = findViewById(R.id.progressCadastroProduto);

        barcodeResultView = findViewById(R.id.EditTextEan);
        allowManualInput = true;

        editNomeProduto = findViewById(R.id.editNomeProduto);
        editMarca = findViewById(R.id.editMarca);
        imageViewProduto =findViewById(R.id.imageViewProduto);
        imageButtonFoto = (ImageButton)findViewById(R.id.imageButtonFoto);


        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recuperarDepartamentos();

        btnSalvar = (Button) findViewById(R.id.buttonCadastarPromocao);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(produto != null){
                    Intent i = new Intent(getApplicationContext(), PromocaoActivity.class);
                    i.putExtra("produto", valueOf(produto.getId()));
                    startActivity(i);
                    finish();
                }else if(result != null){
                    alteraProdutos(idProduto);
                }else{
                    alteraProdutosCompleto(idProduto);
                }

            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Spinner spinner = (Spinner) findViewById(R.id.spinnerDepartamento);
        spinner.setOnItemSelectedListener(this);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onScanButtonClicked(View view) {
        GmsBarcodeScannerOptions.Builder optionsBuilder = new GmsBarcodeScannerOptions.Builder();
        if (allowManualInput) {
            optionsBuilder.allowManualInput();
        }
        GmsBarcodeScanner gmsBarcodeScanner =
                GmsBarcodeScanning.getClient(this, optionsBuilder.build());
        gmsBarcodeScanner
                .startScan()
                .addOnSuccessListener(barcode -> barcodeResultView.setText(getSuccessfulMessage(barcode)))
                .addOnFailureListener(
                        e -> barcodeResultView.setText(getErrorMessage(e)))
                .addOnCanceledListener(
                        () -> barcodeResultView.setText(getString(R.string.error_scanner_cancelled)));
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_ALLOW_MANUAL_INPUT, allowManualInput);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        allowManualInput = savedInstanceState.getBoolean(KEY_ALLOW_MANUAL_INPUT);
    }

    private String getSuccessfulMessage(Barcode barcode) {
        String barcodeValue =
                String.format(
                        Locale.getDefault(),
                        "%s",
                        barcode.getRawValue());
        codigo = barcodeValue;
        buscarProduto(codigo);
        return getString(R.string.barcode_result, barcodeValue);
    }

    @SuppressLint("SwitchIntDef")
    private String getErrorMessage(Exception e) {
        if (e instanceof MlKitException) {
            switch (((MlKitException) e).getErrorCode()) {
                case MlKitException.CODE_SCANNER_CAMERA_PERMISSION_NOT_GRANTED:
                    return getString(R.string.error_camera_permission_not_granted);
                case MlKitException.CODE_SCANNER_APP_NAME_UNAVAILABLE:
                    return getString(R.string.error_app_name_unavailable);
                default:
                    return getString(R.string.error_default_message);
            }
        } else {
            return e.getMessage();
        }
    }

    private void configurarCadastro(String ean){
        idProduto = new BigInteger(ean);
        editNomeProduto.setFocusable(true);
        editMarca.setFocusable(true);
        spinner.setFocusable(true);
        imageButtonFoto.setVisibility(View.VISIBLE);
        imageButtonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetCadastroProduto bottomSheetCadastroProduto = new BottomSheetCadastroProduto();
                Bundle bundle = new Bundle();
                bundle.putString("idProduto", (String) ean);
                bottomSheetCadastroProduto.setArguments(bundle);
                bottomSheetCadastroProduto.show(getSupportFragmentManager(), bottomSheetCadastroProduto.getTag());
            }
        });
    }



    private void registrarProdutoEan(final String ean) {
        progressBar.setVisibility(View.VISIBLE);
        idProduto = new BigInteger(ean);
        DataService service = retrofit.create(DataService.class);
        final Call<Result> produtoCall = service.registrarProdutoEan(ean);
        produtoCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    result = response.body();
                    editNomeProduto.setText(result.getNome());
                    editMarca.setText(result.getMarca());
                    Picasso.get()
                            .load(DataService.BASE_URL + result.getUrlImagem())
                            .error(R.drawable.ic_error)
                            .into(imageViewProduto);
                    progressBar.setVisibility(View.GONE);
                    imageButtonFoto.setVisibility(View.GONE);
                    editNomeProduto.setFocusable(false);
                    editMarca.setFocusable(false);
                }else{
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(ProdutoActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(ProdutoActivity.this, "Produto não localizado, favor cadastrar!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            configurarCadastro(ean);
                            break;
                        default:
                            Toast.makeText(ProdutoActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                System.out.println("FALHA: " + t.toString());
            }
        });

    }

    private void buscarProduto(final String str) {

        idProduto = new BigInteger(str);
        DataService service = retrofit.create(DataService.class);
        final Call<Produto> produtoCall = service.buscarProduto(idProduto);

        produtoCall.enqueue(new Callback<Produto>() {
            @Override
            public void onResponse(Call<Produto> call, Response<Produto> response) {

                if (response.isSuccessful()) {
                    produto = response.body();
                    editNomeProduto.setText(produto.getNome());
                    editMarca.setText(produto.getMarca());
                    Picasso.get()
                            .load(DataService.BASE_URL + produto.getUrlImagem())
                            .error(R.drawable.ic_error)
                            .into(imageViewProduto);
//                    spinner.setSelection(produto.getDepartamento());
                    imageButtonFoto.setVisibility(View.GONE);
                    editNomeProduto.setFocusable(false);
                    editMarca.setFocusable(false);
                    spinner.setFocusable(false);
                }else{
                    switch (response.code()) {
                        case 404:
                            progressBar.setVisibility(View.GONE);
                            registrarProdutoEan(str);
                            break;
                        case 500:
                            Toast.makeText(ProdutoActivity.this, "Produto não localizado, favor cadastrar!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            break;
                        default:
                            Toast.makeText(ProdutoActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Produto> call, Throwable t) {
                System.out.println("FALHA: " + t.toString());
            }
        });

    }

    /*

    private void buscarProdutoEan(final String str) {

        idProduto = new BigInteger(str);
        DataService service = retrofit.create(DataService.class);
        final Call<List<Produto>> produtoCall = service.buscarProdutos();

        produtoCall.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {

                if (response.isSuccessful()) {
                    produtos = response.body();

                    if(produtos.isEmpty()){
                        progressBar.setVisibility(View.GONE);
                        registrarProdutoEan(str);
                    }

                    for (Produto p : produtos) {
                        if(!p.getId().equals(idProduto)) {
                            cont = 1;
                            System.out.println("TESTE1");

                        }else if(p.getId().equals(idProduto)){
                            editNomeProduto.setText(p.getNome());
                            editMarca.setText(p.getMarca());
                            Picasso.get()
                                    .load(DataService.BASE_URL + p.getUrlImagem())
                                    .error(R.drawable.ic_error)
                                    .into(imageViewProduto);
                            spinner.setSelection(p.getDepartamento());
                            cont = 0;
                        }
                    }
                    if(cont == 1){
                        System.out.println("TESTE2");
                        progressBar.setVisibility(View.GONE);
                        registrarProdutoEan(str);
                    }
                }


            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                System.out.println("FALHA: " + t.toString());
            }
        });

    }

     */

    private void recuperarDepartamentos() {

        DataService service = retrofit.create(DataService.class);
        Call<List<Departamento>> departamentoCall = service.recuperarDepartamentos();

        departamentoCall.enqueue(new Callback<List<Departamento>>() {
            @Override
            public void onResponse(Call<List<Departamento>> call, Response<List<Departamento>> response) {
                if (response.isSuccessful()) {

                    StaticInstances.departamentos.clear();
                    StaticInstances.departamentos.addAll(response.body());
                    departamentos = StaticInstances.departamentos;

                    ArrayAdapter adapter = new ArrayAdapter(ProdutoActivity.this, android.R.layout.simple_spinner_item, departamentos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);


                    //idDepartamento = ((Departamento)spinner.getSelectedItem()).getId();


                }
            }

            @Override
            public void onFailure(Call<List<Departamento>> call, Throwable t) {
                // TODO
            }
        });

    }

    private void alteraProdutos(BigInteger id) {
        produtoAlterado.setDepartamento(((Departamento)spinner.getSelectedItem()).getId());
        DataService service = retrofit.create(DataService.class);
        Call<Produto> produtoCall = service.alterarProduto(id, produtoAlterado);

        idDepartamento = ((Departamento)spinner.getSelectedItem()).getId();

        produtoCall.enqueue(new Callback<Produto>() {
            @Override
            public void onResponse(Call<Produto> call, Response<Produto> response) {
                if (response.isSuccessful()) {
                    produto = response.body();
                    Intent i = new Intent(getApplicationContext(), PromocaoActivity.class);
                    i.putExtra("produto", valueOf(produto.getId()));
                    startActivity(i);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Produto> call, Throwable t) {
                // TODO
            }
        });

    }

    private void alteraProdutosCompleto(BigInteger id) {
        produtoAlteradoCompleto.setDepartamento(((Departamento)spinner.getSelectedItem()).getId());
        produtoAlteradoCompleto.setNome((String) editNomeProduto.getText());
        produtoAlteradoCompleto.setMarca((String) editMarca.getText());
        DataService service = retrofit.create(DataService.class);
        Call<Produto> produtoCall = service.alterarProduto(id, produtoAlteradoCompleto);

        idDepartamento = ((Departamento)spinner.getSelectedItem()).getId();

        produtoCall.enqueue(new Callback<Produto>() {
            @Override
            public void onResponse(Call<Produto> call, Response<Produto> response) {
                if (response.isSuccessful()) {
                    produto = response.body();
                    Intent i = new Intent(getApplicationContext(), PromocaoActivity.class);
                    i.putExtra("produto", valueOf(produto.getId()));
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Produto> call, Throwable t) {
                // TODO
            }
        });

    }

}
