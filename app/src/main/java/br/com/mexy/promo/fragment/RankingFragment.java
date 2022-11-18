package br.com.mexy.promo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.activity.PerfilActivity;
import br.com.mexy.promo.activity.PerfilGeralActivity;
import br.com.mexy.promo.activity.PromocaoCompletaActivity;
import br.com.mexy.promo.adapter.PromocaoCardAdapter;
import br.com.mexy.promo.adapter.RankingAdapter;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Promocao;
import br.com.mexy.promo.model.Usuario;
import br.com.mexy.promo.util.RecyclerItemClickListener;
import br.com.mexy.promo.util.StaticInstances;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RankingFragment extends Fragment {

    private FragmentActivity myContext;
    public View view;
    private Retrofit retrofit;
    private RecyclerView recyclerRanking;
    private List<Usuario> usuarios = new ArrayList<>();
    private Usuario usuario = new Usuario();
    private ProgressBar progressBar;


    public RankingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ranking, container, false);

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recyclerRanking = view.findViewById(R.id.recyclerRanking);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        //Define layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(myContext, 1 );
        recyclerRanking.setLayoutManager( layoutManager );

        RankingAdapter adapter = new RankingAdapter( usuarios );
        recyclerRanking.setAdapter( adapter );

        recyclerRanking.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        myContext,
                        recyclerRanking,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                iniciarActivityPerfil(view, position);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                iniciarActivityPerfil(view, position);
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        recuperarRanking();


        return view;
    }

    private void iniciarActivityPerfil(View view, int position) {
        Intent intent = new Intent(myContext, PerfilGeralActivity.class);
        intent.putExtra("usuario", usuarios.get(position).getId());
        startActivity(intent);
        
    }

    private void recuperarRanking() {

        progressBar.setVisibility(View.VISIBLE);

        DataService service = retrofit.create(DataService.class);
        Call<List<Usuario>> promocaoCall = service.ranking();

        promocaoCall.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {

                    usuarios.clear();
                    usuarios.addAll(response.body());
                    RankingAdapter adapter = new RankingAdapter( usuarios );
                    recyclerRanking.setAdapter( adapter );

                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                // TODO
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext=(PerfilActivity) context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            myContext=(PerfilActivity) activity;
        }
    }

}