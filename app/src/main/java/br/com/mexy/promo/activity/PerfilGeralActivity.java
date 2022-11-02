package br.com.mexy.promo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.squareup.picasso.Picasso;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.fragment.BottomSheetAppBar;
import br.com.mexy.promo.fragment.PostFragment;
import br.com.mexy.promo.fragment.PostGeralFragment;
import br.com.mexy.promo.fragment.RankingFragment;
import br.com.mexy.promo.model.Usuario;
import br.com.mexy.promo.util.StaticInstances;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PerfilGeralActivity extends AppCompatActivity {

    private Retrofit retrofit;
    public Integer idUsuario;
    private TextView textViewNome;
    private TextView textViewNota;
    private FloatingActionButton floatingActionButton;
    private ImageView imageViewUsuario;
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private PostGeralFragment fragment = new PostGeralFragment();
    private Usuario usuario = new Usuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_geral);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idUsuario = extras.getInt("usuario");
        }

        StaticInstances.idUsuario = idUsuario;

        smartTabLayout = findViewById(R.id.viewPagerTab);
        viewPager = findViewById(R.id.viewPager);


        //Configurar adapter para abas
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Postagens", PostGeralFragment.class)
                        .create()

        );


        viewPager.setAdapter( adapter );
        smartTabLayout.setViewPager( viewPager );


        textViewNome = findViewById(R.id.textViewNome);
        textViewNota = findViewById(R.id.textViewNota);
        imageViewUsuario = findViewById(R.id.imageViewUsuario);
/*
        textViewNome.setText(usuario.toString());
        //  textViewNota.setText(usuario.getPontuacao());
        Picasso.get()
                .load(DataService.BASE_URL + usuario.getUrlImagem())
                .error(R.drawable.ic_error)
                .into(imageViewUsuario);*/

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recuperarUsuario(idUsuario);

    }

    private void recuperarUsuario(Integer id) {

        DataService service = retrofit.create(DataService.class);
        final Call<Usuario> usuarioCall = service.recuperarUsuario(id);

        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    usuario = response.body();
                    textViewNome.setText(usuario.toString());
                    textViewNota.setText(usuario.getPontuacao().toString());
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
