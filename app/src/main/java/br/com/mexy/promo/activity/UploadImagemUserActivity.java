package br.com.mexy.promo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.fragment.BottomSheetCadastroProduto;
import br.com.mexy.promo.fragment.BottomSheetImagemUsuario;
import br.com.mexy.promo.model.Usuario;
import retrofit2.Retrofit;

public class UploadImagemUserActivity extends AppCompatActivity {

    private Usuario usuario = new Usuario();
    private TextView textViewNome;
    private ImageButton buttomImagem;
    private Integer idUsuario;
    private Bundle extras;
    private ImageView imageViewUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem_usuario);

        imageViewUsuario = findViewById(R.id.imageViewUsuario);
        extras = getIntent().getExtras();

        if(extras != null){
            idUsuario = extras.getInt("idUsuario");
            System.out.println("TESTE "+ idUsuario);
        }else{
            usuario = getIntent().getExtras().getParcelable("usuario");
            textViewNome.setText(usuario.toString());
        }


        textViewNome = findViewById(R.id.textViewNome);
        buttomImagem = (ImageButton)findViewById(R.id.imagemUploadUsuario);

        buttomImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("TESTE: "+ usuario.getId());
                final BottomSheetImagemUsuario bottomSheetImagemUsuario = new BottomSheetImagemUsuario();
                Bundle bundle = new Bundle();
                bundle.putInt("idUsuario", usuario.getId());
                bottomSheetImagemUsuario.setArguments(bundle);
                bottomSheetImagemUsuario.show(getSupportFragmentManager(), bottomSheetImagemUsuario.getTag());
            }
        });

    }
}
