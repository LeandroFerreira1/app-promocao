package br.com.mexy.promo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Avaliacao;
import br.com.mexy.promo.model.Promocao;
import br.com.mexy.promo.model.Usuario;
import br.com.mexy.promo.util.Pontuacao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvaliacaoActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private View view;
    private Promocao promocao = new Promocao();
    private Usuario usuario = new Usuario();
    private Avaliacao avaliacao = new Avaliacao();
    private TextView textComentario;
    private CheckBox checkBoxLocalizacao;
    private RatingBar ratingBarAvaliacao;
    private Button buttonEnviar;
    private Integer ponto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_avaliacao_promocao);

        textComentario = findViewById(R.id.textComentario);
        checkBoxLocalizacao = (CheckBox) findViewById(R.id.checkBoxLocalizacao);
        ratingBarAvaliacao = (RatingBar) findViewById(R.id.ratingBarAvaliacao);
        buttonEnviar = (Button) findViewById(R.id.buttonEnviar);


        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        promocao = getIntent().getExtras().getParcelable("promocao");

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        String res = sharedPreferences.getString("ID_USUARIO", null);

        String token = "Bearer " + res;
        logado(token);

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarAvaliacao(token);
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

    private void registrarAvaliacao(String token) {

        avaliacao.setPromocao(promocao.getId());
        avaliacao.setUsuario(usuario.getId());
        avaliacao.setDescricao(String.valueOf(textComentario.getText()));
        avaliacao.setNota(Math.round(ratingBarAvaliacao.getRating()));
        //avaliacao.setLatitude();
        //avaliacao.setLongitude();

        if(avaliacao.getDescricao() != null && avaliacao.getLatitude() != null){
            Pontuacao.alteraponto(retrofit, token, 20);
        }else if(avaliacao.getDescricao() == null && avaliacao.getLatitude() != null){
            Pontuacao.alteraponto(retrofit, token, 15);
        }else if(avaliacao.getDescricao() != null && avaliacao.getLatitude() == null){
            Pontuacao.alteraponto(retrofit, token, 10);
        }else{
            Pontuacao.alteraponto(retrofit, token, 5);
        }

        DataService service = retrofit.create(DataService.class);
        final Call<Avaliacao> usuarioCall = service.registrarAvaliacao(token, avaliacao);

        usuarioCall.enqueue(new Callback<Avaliacao>() {
            @Override
            public void onResponse(Call<Avaliacao> call, Response<Avaliacao> response) {
                if (response.isSuccessful()) {
                    avaliacao = response.body();
                    Intent intent = new Intent(AvaliacaoActivity.this, PromocaoListActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Você já avaliou essa promoção!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Avaliacao> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Você já avaliou essa promoção!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
