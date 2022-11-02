package br.com.mexy.promo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.adapter.PromocaoListAdapter;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Promocao;
import br.com.mexy.promo.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PromocaoCompletaActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private Promocao promocaoId;
    private Promocao promocao = new Promocao();
    private ImageView imageViewUsuariop;
    private ImageView imageViewProdu;
    private ImageView imageViewEstabelecimento;
    private TextView textViewLike;
    private TextView textViewNomeProduto;
    private TextView textViewDepartamentoProduto;
    private TextView textViewPrecoPromocional;
    private TextView textViewEstabelecimento;
    private TextView textViewDataValidade;
    private Button buttonAvaliarPromocao;
    private ImageButton imageButtonUsuario;
    private Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promocao_completa);

        imageViewUsuariop = (ImageView) findViewById(R.id.imageViewUsuariop);
        imageViewProdu = (ImageView) findViewById(R.id.imageViewProdu);
        imageViewEstabelecimento = (ImageView) findViewById(R.id.imageViewEstabelecimento);
        textViewLike = (TextView) findViewById(R.id.textViewLike);
        textViewNomeProduto = (TextView) findViewById(R.id.textViewNomeProduto);
        textViewDepartamentoProduto = (TextView) findViewById(R.id.textViewDepartamentoProduto);
        textViewEstabelecimento = (TextView) findViewById(R.id.textViewEstabelecimento);
        textViewDataValidade = (TextView) findViewById(R.id.textViewDataValidade);
        textViewPrecoPromocional = (TextView) findViewById(R.id.textViewPrecoPromocional);
        buttonAvaliarPromocao = (Button) findViewById(R.id.buttonAvaliarPromocao);
        imageButtonUsuario = (ImageButton) findViewById(R.id.imageButtonUsuario);

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        promocaoId = getIntent().getExtras().getParcelable("promocao");

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        String res = sharedPreferences.getString("ID_USUARIO", null);

        String token = "Bearer " + res;

        buscarPromocao();
        logado(token);

        buttonAvaliarPromocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
                String res = sharedPreferences.getString("ID_USUARIO", null);

                if (res == null) {
                    Toast.makeText(getApplicationContext(),"Você deve estar logado para fazer uma avaliação!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(PromocaoCompletaActivity.this, AvaliacaoActivity.class);
                    intent.putExtra("promocao", promocaoId);
                    startActivity(intent);
                }

            }
        });

        imageButtonUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(usuario.getId() == promocao.getUsuario().getId()){
                    Intent intent = new Intent(PromocaoCompletaActivity.this, PerfilActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(PromocaoCompletaActivity.this, PerfilGeralActivity.class);
                    intent.putExtra("usuario", promocao.getUsuario().getId());
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void logado(String token) {

        DataService service = retrofit.create(DataService.class);
        final Call<Usuario> usuarioCall = service.logado(token);

        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    usuario = response.body();
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    private void buscarPromocao() {

        DataService service = retrofit.create(DataService.class);
        Call<Promocao> promocaoCall = service.buscarPromocao(promocaoId.getId());

        promocaoCall.enqueue(new Callback<Promocao>() {
            @Override
            public void onResponse(Call<Promocao> call, Response<Promocao> response) {
                if (response.isSuccessful()) {

                    promocao = response.body();

                    Picasso.get()
                            .load(DataService.BASE_URL + promocao.getProduto().getUrlImagem())
                            .error(R.drawable.ic_error)
                            .into(imageViewProdu);
                    Picasso.get()
                            .load(DataService.BASE_URL + promocao.getUsuario().getUrlImagem())
                            .error(R.drawable.ic_error)
                            .into(imageViewUsuariop);
                    Picasso.get()
                            .load(DataService.BASE_URL + promocao.getEstabelecimento().getUrlImagem())
                            .error(R.drawable.ic_error)
                            .into(imageViewEstabelecimento);
                    //textViewLike.setText(promocao.getCurtida());

                    textViewNomeProduto.setText(promocao.getProduto().getNome());
                    //textViewDepartamentoProduto.setText(promocao.getProduto().getDepartamento());
                    textViewEstabelecimento.setText(promocao.getEstabelecimento().getNome());
                    textViewDataValidade.setText(promocao.getDataValidade());
                    textViewPrecoPromocional.setText("R$ "+promocao.getValorPromocional());
                }
            }

            @Override
            public void onFailure(Call<Promocao> call, Throwable t) {
                // TODO
            }
        });

    }
}
