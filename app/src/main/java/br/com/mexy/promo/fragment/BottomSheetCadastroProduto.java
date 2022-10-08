package br.com.mexy.promo.fragment;

        import android.Manifest;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
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

        import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
        import com.google.android.material.navigation.NavigationView;

        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.IOException;
        import java.math.BigInteger;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import static androidx.core.content.FileProvider.getUriForFile;

        import br.com.mexy.promo.R;
        import br.com.mexy.promo.activity.ProdutoActivity;
        import br.com.mexy.promo.util.Permissao;

public class BottomSheetCadastroProduto extends BottomSheetDialogFragment {

    private ImageButton buttonCamera;
    private ImageButton buttonArquivos;
    private String idProduto;

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

        idProduto = getArguments().getString("idProduto");
        return inflater
                .inflate(R.layout.produto_bottom_sheet_foto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Permissao.validarPermissoes(permissoes, getActivity(), 1 );

        buttonArquivos = view.findViewById(R.id.imageButtonAbrirArquivos);
        buttonCamera = view.findViewById(R.id.imageButtonAbrirCamera);

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
        String imageFileName = idProduto;
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
                        Intent i = new Intent(getActivity(), ProdutoActivity.class);
                        i.putExtra("fotoEscolhida", currentPhotoPath );
                        startActivity( i );
                        break;
                    case SELECAO_GALERIA :
                        Intent i2 = new Intent(getActivity(), ProdutoActivity.class);
                        Uri localImagemSelecionada = data.getData();

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        String testee = localImagemSelecionada.getPath();
                        Toast.makeText(getActivity().getApplicationContext(), testee, Toast.LENGTH_SHORT).show();

                        i2.putExtra("fotoEscolhida", picturePath );
                        startActivity( i2 );
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



}