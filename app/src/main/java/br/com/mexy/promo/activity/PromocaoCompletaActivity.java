package br.com.mexy.promo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.adapter.AvaliacaoAdapter;
import br.com.mexy.promo.adapter.PromocaoFilterAdapter;
import br.com.mexy.promo.adapter.PromocaoListAdapter;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Avaliacao;
import br.com.mexy.promo.model.Curtida;
import br.com.mexy.promo.model.Departamento;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Promocao;
import br.com.mexy.promo.model.Usuario;
import okhttp3.ResponseBody;
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
    private TextView textViewAvaliacao;
    private Button buttonAvaliarPromocao;
    private ImageButton imageButtonUsuario;
    private Usuario usuario = new Usuario();
    private List<Avaliacao> avaliacoes = new ArrayList<>();
    private Integer avaliacaoNota = 0;
    private RecyclerView recyclerAvaliacoes;
    private AvaliacaoAdapter adapter;
    private List<Curtida> curtidas = new ArrayList<>();
    private ImageButton curtida;
    private Curtida postCurtida = new Curtida();
    private Curtida curtidaUsuario = new Curtida();


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
        textViewAvaliacao = (TextView) findViewById(R.id.textViewAvaliacao);
        textViewPrecoPromocional = (TextView) findViewById(R.id.textViewPrecoPromocional);
        buttonAvaliarPromocao = (Button) findViewById(R.id.buttonAvaliarPromocao);
        imageButtonUsuario = (ImageButton) findViewById(R.id.imageButtonUsuario);
        recyclerAvaliacoes = (RecyclerView) findViewById(R.id.recyclerAvaliacoes);
        curtida = (ImageButton) findViewById(R.id.curtida);

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        promocaoId = getIntent().getExtras().getParcelable("promocao");

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1 );
        recyclerAvaliacoes.setLayoutManager( layoutManager );

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        String res = sharedPreferences.getString("ID_USUARIO", null);

        String token = "Bearer " + res;
        logado(token);
        avaliacao();
        buscarPromocao();
        curtidaUsuario(token, promocaoId.getId());
        buscarCurtida(promocaoId.getId());

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

        curtida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtidaUsuario(token, promocaoId.getId());
                if(curtidaUsuario.getUsuario() == null){
                    curtir(token, promocaoId.getId());
                    curtidaUsuario = new Curtida();
                }else{
                    descurtir(token, promocaoId.getId());
                    curtidaUsuario = new Curtida();
                }

            }
        });

    }

    private void avaliacao() {

        DataService service = retrofit.create(DataService.class);
        final Call <List<Avaliacao>> usuarioCall = service.buscarAvaliacoes(promocaoId.getId());

        usuarioCall.enqueue(new Callback <List<Avaliacao>>() {
            @Override
            public void onResponse(Call <List<Avaliacao>> call, Response <List<Avaliacao>> response) {
                if (response.isSuccessful()) {
                    avaliacoes = response.body();
                    adapter = new AvaliacaoAdapter( avaliacoes, usuario);
                    recyclerAvaliacoes.setAdapter( adapter );
                    int cont = 0;
                    int aval = 0;
                    for (Avaliacao a : avaliacoes) {
                        aval = aval + a.getNota();
                        cont++;
                    }
                    if(cont != 0){
                        avaliacaoNota = aval/cont;
                    }else{
                        avaliacaoNota = 0;
                    }
                    textViewAvaliacao.setText(avaliacaoNota.toString());
                }
            }
            @Override
            public void onFailure(Call <List<Avaliacao>> call, Throwable t) {

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

    private void curtidaUsuario (String token, Integer id) {

        DataService service = retrofit.create(DataService.class);
        final Call<Curtida> usuarioCall = service.buscarCurtidaUsuario(token, id);

        usuarioCall.enqueue(new Callback<Curtida>() {
            @Override
            public void onResponse(Call<Curtida> call, Response<Curtida> response) {
                if (response.isSuccessful()) {
                    curtidaUsuario = response.body();
                    curtida.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                }
            }
            @Override
            public void onFailure(Call<Curtida> call, Throwable t) {

            }
        });
    }

    private void curtir(String token, Integer id) {
        postCurtida.setPromocao(id);
        postCurtida.setUsuario(0);
        DataService service = retrofit.create(DataService.class);
        final Call<Curtida> usuarioCall = service.registrarCurtida(token, postCurtida);

        usuarioCall.enqueue(new Callback<Curtida>() {
            @Override
            public void onResponse(Call<Curtida> call, Response<Curtida> response) {
                if (response.isSuccessful()) {
                    curtida.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                    buscarCurtida(promocaoId.getId());
                    curtidaUsuario = new Curtida();
                }
            }
            @Override
            public void onFailure(Call<Curtida> call, Throwable t) {

            }
        });
    }

    private void buscarCurtida(Integer id) {
        DataService service = retrofit.create(DataService.class);
        final Call <List<Curtida>> usuarioCall = service.buscarCurtidas(id);

        usuarioCall.enqueue(new Callback<List<Curtida>>() {
            @Override
            public void onResponse(Call<List<Curtida>> call, Response<List<Curtida>> response) {
                if (response.isSuccessful()) {
                    curtidas.clear();
                    curtidas.addAll(response.body());
                    if(curtidas.isEmpty()){
                        textViewLike.setText(String.valueOf(0));
                    }else{
                        textViewLike.setText(String.valueOf(curtidas.size()));
                    }

                }
            }
            @Override
            public void onFailure(Call<List<Curtida>> call, Throwable t) {

            }
        });
    }

    private void descurtir(String token, Integer id) {

        DataService service = retrofit.create(DataService.class);
        final Call<ResponseBody> curtidaCall = service.deletarCurtida(id, token);

        curtidaCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    curtida.setImageDrawable(getResources().getDrawable(R.drawable.ic_deslik));
                    buscarCurtida(promocaoId.getId());
                    curtidaUsuario = new Curtida();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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
