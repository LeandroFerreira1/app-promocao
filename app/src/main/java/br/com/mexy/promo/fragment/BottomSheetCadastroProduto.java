package br.com.mexy.promo.fragment;

        import android.Manifest;
        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Matrix;
        import android.media.ExifInterface;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.core.content.FileProvider;
        import androidx.fragment.app.FragmentActivity;

        import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
        import com.google.android.material.navigation.NavigationView;

        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.math.BigInteger;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import static androidx.core.content.FileProvider.getUriForFile;

        import br.com.mexy.promo.R;
        import br.com.mexy.promo.activity.CadastroUsuarioActivity;
        import br.com.mexy.promo.activity.MainActivity;
        import br.com.mexy.promo.activity.ProdutoActivity;
        import br.com.mexy.promo.activity.PromocaoActivity;
        import br.com.mexy.promo.api.DataService;
        import br.com.mexy.promo.util.CustomInterface;
        import br.com.mexy.promo.util.Permissao;
        import okhttp3.MediaType;
        import okhttp3.MultipartBody;
        import okhttp3.RequestBody;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;
        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetCadastroProduto extends BottomSheetDialogFragment {

    private ImageButton buttonCamera;
    private ImageButton buttonArquivos;
    private Integer idUsuario;
    private FragmentActivity myContext;
    private Retrofit retrofit;
    private Bitmap imagem;
    private final CustomInterface callback;

    public BottomSheetCadastroProduto(CustomInterface callback) {
        this.callback=callback;
    }

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private static final int SELECAO_CAMERA  = 100;
    private static final int SELECAO_GALERIA = 200;


    @Nullable
    @Override public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        return inflater
                .inflate(R.layout.produto_bottom_sheet_foto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Permissao.validarPermissoes(permissoes, getActivity(), 1 );

        buttonArquivos = view.findViewById(R.id.imageButtonAbrirArquivos);
        buttonCamera = view.findViewById(R.id.imageButtonAbrirCamera);

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //File file = new File("/myImage.jpg");
                //Uri outputFileUri = Uri.fromFile(file);

                /*File imagePath = new File(getActivity().getFilesDir(), "images");
                File newFile = new File(imagePath, "default_image.jpg");
                Uri contentUri = getUriForFile(getContext(), "br.com.aiker.art.fileprovider", newFile);

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                if( i.resolveActivity( getActivity().getPackageManager() ) != null ){
                    startActivityForResult(i, SELECAO_CAMERA);
                }*/
                dispatchTakePictureIntent();
            }
        });

        //Adiciona evento de clique no botão da galeria
        buttonArquivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECAO_GALERIA);
            }
        });

    }


    String currentPhotoPath;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri contentUri = getUriForFile(getActivity(), "br.com.mexy.promo.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                startActivityForResult(takePictureIntent, 100);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "TMP";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }





    public static File savebitmap(Bitmap bmp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "postagem.jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }


    View view;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == getActivity().RESULT_OK ){

            Bitmap imagem = null;

            try {

                //Valida tipo de seleção da imagem
                switch ( requestCode ){
                    case SELECAO_CAMERA :
                        //imagem = (Bitmap) BitmapFactory.decodeFile(currentPhotoPath);
                        passarimagem(view, currentPhotoPath);
                        break;
                    case SELECAO_GALERIA :
                        Uri localImagemSelecionada = data.getData();
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        passarimagem(view, picturePath);
                        //imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localImagemSelecionada);
                        break;
                }

                //Valida imagem selecionada
               /* if( imagem != null ){

                    //Converte imagem em byte array
                    /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.PNG, 50, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Envia imagem escolhida para aplicação de filtro
                    Intent i = new Intent(getActivity(), FiltroActivity.class);
                    i.putExtra("fotoEscolhida", currentPhotoPath );
                    startActivity( i );

                }*/

            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private void passarimagem(View view, String photo) throws IOException {
        callback.callbackMethodPhoto(photo);
        dismiss();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext=(ProdutoActivity) context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            myContext=(ProdutoActivity) activity;
        }
    }
}