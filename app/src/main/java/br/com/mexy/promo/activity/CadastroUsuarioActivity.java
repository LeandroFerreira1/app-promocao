package br.com.mexy.promo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.fragment.BottomSheetImagemUsuario;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Result;
import br.com.mexy.promo.model.Usuario;
import br.com.mexy.promo.util.CustomInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroUsuarioActivity extends AppCompatActivity implements CustomInterface {

    private EditText editCadastroNome;
    private EditText editCadastroEmail;
    private EditText editCadastroSenha;
    private EditText editCadastroSobrenome;
    private ProgressBar progressCadastroUsuario;
    private Button buttonLoginCadastrar;
    private Retrofit retrofit;
    private CustomInterface callback;
    private Usuario usuario = new Usuario();
    private Usuario resultUsuario = new Usuario();
    private Bitmap imagem;
    private String dadosImagem;
    private File file;
    private ImageView imageViewUsuario;
    private ImageButton buttomImagem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        callback=this;

        editCadastroNome = findViewById(R.id.editCadastroNome);
        editCadastroSobrenome = findViewById(R.id.editCadastroSobrenome);
        editCadastroEmail = (EditText) findViewById(R.id.editCadastroEmail);
        editCadastroSenha = (EditText) findViewById(R.id.editCadastroSenha);
        buttonLoginCadastrar = (Button) findViewById(R.id.buttonLoginCadastrar);
        progressCadastroUsuario = (ProgressBar) findViewById(R.id.progressCadastroUsuario);
        imageViewUsuario = findViewById(R.id.imageViewUsuario);
        buttomImagem = (ImageButton)findViewById(R.id.imagemUploadUsuario);


        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        buttomImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetImagemUsuario bottomSheetImagemUsuario = new BottomSheetImagemUsuario(callback);
                bottomSheetImagemUsuario.show(getSupportFragmentManager(), bottomSheetImagemUsuario.getTag());
            }
        });

        buttonLoginCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario.setNome(editCadastroNome.getText().toString());
                usuario.setSobrenome(editCadastroSobrenome.getText().toString());
                usuario.setEmail(editCadastroEmail.getText().toString());
                usuario.setSenha(editCadastroSenha.getText().toString());
                progressCadastroUsuario.setVisibility(View.VISIBLE);
                cadastrarUsuario(usuario);
            }
        });

    }



    private void cadastrarUsuario(final Usuario usuario) {

        DataService service = retrofit.create(DataService.class);
        final Call<Usuario> usuarioCall = service.registrarUsuario(usuario);
        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    resultUsuario = response.body();
                    uploadImageProduto(resultUsuario.getId(),file);
                    progressCadastroUsuario.setVisibility(View.GONE);
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                System.out.println("FALHA: " + t.toString());
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

    private void uploadImageProduto(Integer id, File file){

        System.out.println("TESTE2 "+ id + file);

        DataService service = retrofit.create(DataService.class);
        final Call<String> postCall = service.uploadImageUser(id, prepareFilePart("file", file));

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

        imageViewUsuario.setImageBitmap(imagem);

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
}
