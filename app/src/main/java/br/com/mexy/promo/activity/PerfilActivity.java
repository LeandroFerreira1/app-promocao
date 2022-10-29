package br.com.mexy.promo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.fragment.PostFragment;
import br.com.mexy.promo.fragment.RankingFragment;
import br.com.mexy.promo.model.Promocao;
import br.com.mexy.promo.model.ResponseUsuario;
import br.com.mexy.promo.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class PerfilActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private Usuario usuario = new Usuario();
    private TextView textViewNome;
    private FloatingActionButton floatingActionButton;
    private ImageView imageViewUsuario;
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        smartTabLayout = findViewById(R.id.viewPagerTab);
        viewPager = findViewById(R.id.viewPager);

        //Configurar adapter para abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Postagens", PostFragment.class )
                        .add("Ranking", RankingFragment.class )
                        .create()
        );

        viewPager.setAdapter( adapter );
        smartTabLayout.setViewPager( viewPager );

        textViewNome = findViewById(R.id.textViewNome);
        imageViewUsuario = findViewById(R.id.imageViewUsuario);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        String res = sharedPreferences.getString("ID_USUARIO", null);

        String token = "Bearer " + res;

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        logado(token);

        floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ProdutoActivity.class);
                startActivity(intent);
                finish();
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
                    textViewNome.setText(usuario.toString());
                    Picasso.get()
                            .load(DataService.BASE_URL + usuario.getUrlImagem())
                            .error(R.drawable.ic_error)
                            .into(imageViewUsuario);
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }
}
