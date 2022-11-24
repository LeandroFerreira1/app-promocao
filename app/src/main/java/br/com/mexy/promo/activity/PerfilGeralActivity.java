package br.com.mexy.promo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.adapter.ConquistaAdapter;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.fragment.BottomSheetAppBar;
import br.com.mexy.promo.fragment.PostFragment;
import br.com.mexy.promo.fragment.PostGeralFragment;
import br.com.mexy.promo.fragment.RankingFragment;
import br.com.mexy.promo.model.Conquista;
import br.com.mexy.promo.model.Usuario;
import br.com.mexy.promo.model.UsuarioConquista;
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
    private RecyclerView recyclerConquistaGeral;
    private Conquista conquista = new Conquista();
    private List<UsuarioConquista> usuarioConquistas = new ArrayList<>();
    private List<Conquista> conquistas = new ArrayList<>();


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

        recyclerConquistaGeral = findViewById(R.id.recyclerConquistaGeral);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL,false);
        recyclerConquistaGeral.setLayoutManager(layoutManager);

        recuperarUsuario(idUsuario);
        recuperaUsuarioConquista(idUsuario);

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
                            .error(R.mipmap.ic_logo)
                            .into(imageViewUsuario);
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    private void recuperaUsuarioConquista(Integer id) {

        DataService service = retrofit.create(DataService.class);
        final Call<List<UsuarioConquista>> usuarioConquistaCall = service.recuperarUsuarioConquistasAberto(id);

        usuarioConquistaCall.enqueue(new Callback<List<UsuarioConquista>>() {
            @Override
            public void onResponse(Call<List<UsuarioConquista>> call, Response<List<UsuarioConquista>> response) {
                if (response.isSuccessful()) {
                    usuarioConquistas.clear();
                    usuarioConquistas.addAll(response.body());
                    for (UsuarioConquista a : usuarioConquistas) {
                        recuperaConquistas(a.getConquista());
                    }
                }
            }
            @Override
            public void onFailure(Call<List<UsuarioConquista>> call, Throwable t) {

            }
        });
    }

    private void recuperaConquistas(Integer id) {

        DataService service = retrofit.create(DataService.class);
        final Call<Conquista> conquistaCall = service.recuperaConquista(id);

        conquistaCall.enqueue(new Callback<Conquista>() {
            @Override
            public void onResponse(Call<Conquista> call, Response<Conquista> response) {
                if (response.isSuccessful()) {
                    conquista = response.body();
                    conquistas.add(conquista);

                    ConquistaAdapter adapter2 = new ConquistaAdapter(conquistas);
                    recyclerConquistaGeral.setAdapter(adapter2);
                }
            }
            @Override
            public void onFailure(Call<Conquista> call, Throwable t) {

            }
        });
    }

}
