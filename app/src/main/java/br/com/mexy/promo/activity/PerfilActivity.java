package br.com.mexy.promo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.ResponseUsuario;
import br.com.mexy.promo.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PerfilActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private Usuario usuario = new Usuario();
    private TextView textViewNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        textViewNome = findViewById(R.id.textViewNome);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        String res = sharedPreferences.getString("ID_USUARIO", null);

        String token = "Bearer " + res;

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        logado(token);

    }

    private void logado(String token) {

        DataService service = retrofit.create(DataService.class);
        final Call<Usuario> usuarioCall = service.logado(token);

        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    usuario = response.body();
                    textViewNome.setText(usuario.toString());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }
}
