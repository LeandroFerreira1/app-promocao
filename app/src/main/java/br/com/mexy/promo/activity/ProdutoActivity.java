package br.com.mexy.promo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Produto;
import br.com.mexy.promo.model.Result;
import okhttp3.MediaType;
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
    private List<Produto> produtos = new ArrayList<>();
    private TextView editNomeProduto;
    private TextView editMarca;
    private ImageView imageViewProduto;
    private Result result;
    private Produto produto;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);


        Spinner spinner = (Spinner) findViewById(R.id.spinnerDepartamento);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.departamento, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        progressBar = findViewById(R.id.progressCadastroProduto);

        barcodeResultView = findViewById(R.id.EditTextEan);
        allowManualInput = true;

        editNomeProduto = findViewById(R.id.editNomeProduto);
        editMarca = findViewById(R.id.editMarca);
        imageViewProduto =findViewById(R.id.imageViewProduto);

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

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
        buscarProdutoEan(codigo);
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
                    return getString(R.string.error_default_message, e);
            }
        } else {
            return e.getMessage();
        }
    }

    private void registrarProdutoEan(final String ean) {

        progressBar.setVisibility(View.VISIBLE);
        DataService service = retrofit.create(DataService.class);
        final Call<Result> produtoCall = service.registrarProdutoEan(ean);

        produtoCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful())
                    result = response.body();
                    editNomeProduto.setText(result.getNome());
                    editMarca.setText(result.getMarca());
                    Picasso.get()
                        .load(DataService.BASE_URL + result.getUrlImagem())
                        .error(R.drawable.ic_error)
                        .into(imageViewProduto);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                System.out.println("FALHA: " + t.toString());
            }
        });

    }


    private void buscarProdutoEan(final String str) {

        BigInteger id = new BigInteger(str);

        DataService service = retrofit.create(DataService.class);
        final Call<List<Produto>> produtoCall = service.buscarProdutos();

        produtoCall.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {

                if (response.isSuccessful()) {
                    produtos = response.body();

                    for (Produto p : produtos) {
                        if(p.getId().equals(id)) {
                            editNomeProduto.setText(p.getNome());
                            editMarca.setText(p.getMarca());
                            Picasso.get()
                                    .load(DataService.BASE_URL + p.getUrlImagem())
                                    .error(R.drawable.ic_error)
                                    .into(imageViewProduto);
                        }
                    }
                }else{
                    if (response.code() == 404) {
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
}
