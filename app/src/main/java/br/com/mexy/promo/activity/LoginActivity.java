package br.com.mexy.promo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.ResponseUsuario;
import br.com.mexy.promo.model.Result;
import br.com.mexy.promo.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private ResponseUsuario usuario;
    private Retrofit retrofit;
    private EditText editLoginEmail;
    private EditText editLoginSenha;
    private Button buttonLoginEntrar;
    private TextView textCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_avaliacao_promocao);
/*
        editLoginEmail = findViewById(R.id.editLoginEmail);
        editLoginSenha = findViewById(R.id.editLoginSenha);
        buttonLoginEntrar = findViewById(R.id.buttonLoginEntrar);
        textCadastrar = findViewById(R.id.textCadastrar);

        getSupportActionBar().hide();

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        buttonLoginEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUsuario = editLoginEmail.getText().toString();
                String senha = editLoginSenha.getText().toString();
                autenticarUsuario(emailUsuario, senha);
            }
        });*/

    }

    private void autenticarUsuario(String nomeUsuario, String senha) {

        DataService service = retrofit.create(DataService.class);
        final Call<ResponseUsuario> usuarioCall = service.verificarUsuario(nomeUsuario, senha);

        usuarioCall.enqueue(new Callback<ResponseUsuario>() {
            @Override
            public void onResponse(Call<ResponseUsuario> call, Response<ResponseUsuario> response) {
                if (response.isSuccessful()) {

                    usuario = response.body();

                    SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.pref_text), usuario.getAccessToken());
                    editor.apply();

                    System.out.println("USUARIO LOGADO: " + usuario.getAccessToken());

                    /*
                    Intent intent = new Intent(getBaseContext(), PerfilActivity.class);
                    intent.putExtra("idUsuario", usuario.getId());
                    startActivity(intent);*/

                } else {
                    System.out.println("USUARIO FALHOU");
                    textCadastrar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseUsuario> call, Throwable t) {

            }
        });

    }
}