package br.com.mexy.promo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Result;
import br.com.mexy.promo.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText editCadastroNome;
    private EditText editCadastroEmail;
    private EditText editCadastroSenha;
    private EditText editCadastroSobrenome;
    private Button buttonLoginCadastrar;
    private Retrofit retrofit;
    private Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        editCadastroNome = findViewById(R.id.editCadastroNome);
        editCadastroSobrenome = findViewById(R.id.editCadastroSobrenome);
        editCadastroEmail = (EditText) findViewById(R.id.editCadastroEmail);
        editCadastroSenha = (EditText) findViewById(R.id.editCadastroSenha);
        buttonLoginCadastrar = (Button) findViewById(R.id.buttonLoginCadastrar);

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        buttonLoginCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario.setNome(editCadastroNome.getText().toString());
                usuario.setSobrenome(editCadastroSobrenome.getText().toString());
                usuario.setEmail(editCadastroEmail.getText().toString());
                usuario.setSenha(editCadastroSenha.getText().toString());
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
                    System.out.println("SUCESSO");
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                System.out.println("FALHA: " + t.toString());
            }
        });
    }

}
