package br.com.mexy.promo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.activity.MainActivity;
import br.com.mexy.promo.activity.PerfilActivity;
import br.com.mexy.promo.activity.PerfilGeralActivity;
import br.com.mexy.promo.activity.PromocaoCompletaActivity;
import br.com.mexy.promo.activity.PromocaoListActivity;
import br.com.mexy.promo.adapter.PromocaoCardAdapter;
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

public class PostGeralFragment extends Fragment {

    private FragmentActivity myContext;
    public View view;
    private Retrofit retrofit;
    private RecyclerView recyclerListPromo;
    private List<Promocao> promocoes = new ArrayList<>();
    private Usuario usuario = new Usuario();
    private ProgressBar progressBar;

    public PostGeralFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post, container, false);

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recuperarPromocoes(StaticInstances.idUsuario);

        recyclerListPromo = view.findViewById(R.id.recyclerListPromo);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        //Define layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(myContext, 2 );
        recyclerListPromo.setLayoutManager( layoutManager );

        PromocaoCardAdapter adapter = new PromocaoCardAdapter( promocoes );
        recyclerListPromo.setAdapter( adapter );

        recyclerListPromo.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        myContext,
                        recyclerListPromo,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                iniciarActivityPromocaoCompleta(view, position);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                iniciarActivityPromocaoCompleta(view, position);
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );


        return view;

    }

    private void iniciarActivityPromocaoCompleta(View view, int position) {
        Intent intent = new Intent(myContext, PromocaoCompletaActivity.class);
        intent.putExtra("promocao", promocoes.get(position));
        startActivity(intent);
    }

    private void recuperarPromocoes(Integer id) {

        progressBar.setVisibility(View.VISIBLE);

        DataService service = retrofit.create(DataService.class);
        Call<List<Promocao>> promocaoCall = service.recuperarPromocoesUsuario(id);

        promocaoCall.enqueue(new Callback<List<Promocao>>() {
            @Override
            public void onResponse(Call<List<Promocao>> call, Response<List<Promocao>> response) {
                if (response.isSuccessful()) {

                    promocoes.clear();
                    promocoes.addAll(response.body());
                    PromocaoCardAdapter adapter = new PromocaoCardAdapter( promocoes );
                    recyclerListPromo.setAdapter( adapter );
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Promocao>> call, Throwable t) {
                // TODO
            }
        });

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext=(PerfilGeralActivity) context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            myContext=(PerfilGeralActivity) activity;
        }
    }

}


