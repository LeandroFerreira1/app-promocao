package br.com.mexy.promo.activity;

import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.fragment.BottomSheetCadastroProduto;
import br.com.mexy.promo.model.Departamento;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Produto;
import br.com.mexy.promo.model.Result;
import br.com.mexy.promo.util.CustomInterface;
import br.com.mexy.promo.util.StaticInstances;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProdutoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CustomInterface {

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
    private Produto produtoCadastro = new Produto();
    private Produto produto = new Produto();
    private ProgressBar progressBar;
    private Spinner spinner;
    private Integer idDepartamento = 0;
    private Button btnSalvar;
    private BigInteger idProduto;
    private Bitmap imagem;
    private String dadosImagem;
    private File file;
    private Button buttonADD;

    private int validador = 0;
    private int validadorBusca = 0;

    private CustomInterface callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        callback=this;

        spinner = (Spinner) findViewById(R.id.spinnerDepartamento);

        progressBar = findViewById(R.id.progressCadastroProduto);

        barcodeResultView = findViewById(R.id.EditTextEan);
        allowManualInput = true;
        editNomeProduto = findViewById(R.id.editNomeProduto);
        editMarca = findViewById(R.id.editMarca);
        imageViewProduto =findViewById(R.id.imageViewProduto);
        imageButtonFoto = (ImageButton)findViewById(R.id.imageButtonFoto);
        buttonADD =(Button) findViewById(R.id.buttonADD);


        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recuperarDepartamentos();

        btnSalvar = (Button) findViewById(R.id.buttonCadastarPromocao);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(validadorBusca == 600){
                    Intent i = new Intent(getApplicationContext(), PromocaoActivity.class);
                    i.putExtra("produto", valueOf(produto.getEan()));
                    startActivity(i);
                    finish();
                }else if(result != null){
                    //produtoAlterado.setDepartamento(((Departamento)spinner.getSelectedItem()).getId());
                    Intent i = new Intent(getApplicationContext(), PromocaoActivity.class);
                    i.putExtra("produto", valueOf(result.getEan()));
                    startActivity(i);
                    finish();
                }else if(validador == 500){
                    //produtoAlteradoCompleto.setDepartamento(((Departamento)spinner.getSelectedItem()).getId());
                    cadastraProdutoManual(codigo);
                }
            }
        });

        buttonADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(codigo != null){
                    buscarProduto(codigo);
                }else {
                    Toast.makeText(ProdutoActivity.this, "Escaneie ou Digite um código de Barras", Toast.LENGTH_SHORT).show();
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
        validador = 500;
        editNomeProduto.setFocusable(true);
        editMarca.setFocusable(true);
        //spinner.setFocusable(true);
        imageButtonFoto.setVisibility(View.VISIBLE);
        imageButtonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetCadastroProduto bottomSheetCadastroProduto = new BottomSheetCadastroProduto(callback);
                bottomSheetCadastroProduto.show(getSupportFragmentManager(), bottomSheetCadastroProduto.getTag());
            }
        });
    }


//cadastra o produto via ean
    private void registrarProdutoEan(final String ean) {
        progressBar.setVisibility(View.VISIBLE);
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

    //metodo de busca no banco de dados de produto se não encontrar, busca no metodo de scrap

    private void buscarProduto(final String ean) {

        DataService service = retrofit.create(DataService.class);
        final Call<Produto> produtoCall = service.buscarProduto(ean);

        produtoCall.enqueue(new Callback<Produto>() {
            @Override
            public void onResponse(Call<Produto> call, Response<Produto> response) {

                if (response.isSuccessful()) {
                    validadorBusca = 600;
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
                    //spinner.setFocusable(false);
                }else{
                    switch (response.code()) {
                        case 404:
                            progressBar.setVisibility(View.GONE);
                            registrarProdutoEan(ean);
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

    //metodo de alteração de departamento quando vem do scrap
/*
    private void alteraProdutos() {
        DataService service = retrofit.create(DataService.class);
        Call<Produto> produtoCall = service.alterarProduto(id, produtoAlterado);

        idDepartamento = ((Departamento)spinner.getSelectedItem()).getId();

        produtoCall.enqueue(new Callback<Produto>() {
            @Override
            public void onResponse(Call<Produto> call, Response<Produto> response) {
                if (response.isSuccessful()) {

                }
            }
            @Override
            public void onFailure(Call<Produto> call, Throwable t) {
                // TODO
            }
        });

    }*/

    //metodo de cadastro manual do produto

    private void cadastraProdutoManual(String ean) {

        if(ean != null){
            produtoAlteradoCompleto.setId(0);
            produtoAlteradoCompleto.setEan(ean);
        }else{
            Toast.makeText(ProdutoActivity.this, "Leia um código de barras para cadastrar!", Toast.LENGTH_SHORT).show();
        }
        if(editNomeProduto.getText() != null){
            produtoAlteradoCompleto.setNome((String) editNomeProduto.getText().toString());
        } else{
            Toast.makeText(ProdutoActivity.this, "Cadastre um nome para o produto!", Toast.LENGTH_SHORT).show();
        }
        if(editNomeProduto.getText() != null){
            produtoAlteradoCompleto.setMarca((String) editMarca.getText().toString());
        }else{
            Toast.makeText(ProdutoActivity.this, "Cadastre uma marca para o produto!", Toast.LENGTH_SHORT).show();
        }
        DataService service = retrofit.create(DataService.class);
        Call<Produto> produtoCall = service.cadastraProduto(produtoAlteradoCompleto);

        produtoCall.enqueue(new Callback<Produto>() {
            @Override
            public void onResponse(Call<Produto> call, Response<Produto> response) {
                if (response.isSuccessful()) {
                    produtoCadastro = response.body();
                    uploadImageProduto(produtoCadastro.getEan(),file);
                    Intent i = new Intent(getApplicationContext(), PromocaoActivity.class);
                    i.putExtra("produto", valueOf(produtoCadastro.getEan()));
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

    private MultipartBody.Part prepareFilePart(String partName, File file) {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse(partName),
                file
        );
        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
    }

    private void uploadImageProduto(String ean, File file){
        DataService service = retrofit.create(DataService.class);
        final Call<String> postCall = service.uploadImageProduto(ean, prepareFilePart("file", file));

        postCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){

                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("IMAGEM FALHA: " + t.toString());
            }
        });


    }

    @NonNull
    public static String generateRandomFilename(@NonNull String extension) {
        return new StringBuilder(50)
                .append(System.currentTimeMillis())
                .append((int) (Math.random() * 10000.0))
                .append(".")
                .append(extension)
                .toString();
    }

    @NonNull
    public static File storeOnCache(Context context, Bitmap bitmap) throws IOException {
        File cacheDir = context.getCacheDir();
        File file = new File(cacheDir, generateRandomFilename("jpg"));
        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
        out.flush();
        out.close();

        return file;
    }

    private void publicarImagemProduto(String dadosImagem) throws IOException {

        Bitmap bitmapOri = BitmapFactory.decodeFile(dadosImagem);
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(dadosImagem);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmapOri, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmapOri, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmapOri, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmapOri;
        }

        Bitmap bitmapResize = null;

        final int maxSize = 560;
        int outWidth;
        int outHeight;
        int inWidth = bitmapOri.getWidth();
        int inHeight = bitmapOri.getHeight();
        if(inWidth > inHeight){
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }
        bitmapResize = Bitmap.createScaledBitmap(rotatedBitmap, outHeight, outWidth, false);
        imagem = bitmapResize;
        imageViewProduto.setImageBitmap(imagem);
        file = storeOnCache(this,imagem);
    }



    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public void callbackMethod(Estabelecimento estabelecimento) {

    }

    @Override
    public void callbackMethodPhoto(String photo) throws IOException {
        dadosImagem = photo;
        publicarImagemProduto(dadosImagem);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getBaseContext(), PerfilActivity.class);
        finish();
        startActivity(intent);
    }

}