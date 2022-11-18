package br.com.mexy.promo.activity;

import static br.com.mexy.promo.util.StaticInstances.estabelecimentos;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.adapter.PromocaoFilterAdapter;
import br.com.mexy.promo.adapter.PromocaoListAdapter;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Curtida;
import br.com.mexy.promo.model.Promocao;
import br.com.mexy.promo.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PromocaoListActivity extends AppCompatActivity  implements PromocaoFilterAdapter.PromocaoAdapterListener {

    private RecyclerView recyclerListPromocao;
    private List<Promocao> promocoes = new ArrayList<>();
    private int position;
    private Retrofit retrofit;
    private View view;
    PromocaoFilterAdapter adapter;
    private SearchView searchViewPesquisa;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promocao_list);

        searchViewPesquisa = findViewById(R.id.searchViewPesquisa);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recyclerListPromocao = (RecyclerView) findViewById(R.id.recyclerListPromocao);

        //Define layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2 );
        recyclerListPromocao.setLayoutManager( layoutManager );

        recuperarPromocoes();

        adapter = new PromocaoFilterAdapter( promocoes, this);
        recyclerListPromocao.setAdapter( adapter );


        recyclerListPromocao.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerListPromocao,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                iniciarActivityPromocaoCompleta(view, position);
                                finish();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                iniciarActivityPromocaoCompleta(view, position);
                                finish();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        //Configura searchview

        searchViewPesquisa.setQueryHint("Buscar Ofertas");
        searchViewPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        searchViewPesquisa.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchViewPesquisa.setFocusable(true);
                searchViewPesquisa.requestFocusFromTouch();
            }
        });

    }


    private void iniciarActivityPromocaoCompleta(View view, int position) {
        Intent intent = new Intent(PromocaoListActivity.this, PromocaoCompletaActivity.class);
        intent.putExtra("promocao", promocoes.get(position));
        startActivity(intent);
    }

    private void recuperarPromocoes() {
        progressBar.setVisibility(View.VISIBLE);
        DataService service = retrofit.create(DataService.class);
        Call<List<Promocao>> promocaoCall = service.recuperarPromocoes();

        promocaoCall.enqueue(new Callback<List<Promocao>>() {
            @Override
            public void onResponse(Call<List<Promocao>> call, Response<List<Promocao>> response) {
                if (response.isSuccessful()) {

                    promocoes.clear();
                    promocoes.addAll(response.body());
                    adapter.notifyDataSetChanged();
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
    public void onSelected(Promocao item) {
        Intent intent = new Intent(getBaseContext(), PromocaoCompletaActivity.class);
        intent.putExtra("idPromocao", item.getId());
        startActivity(intent);
    }
}
