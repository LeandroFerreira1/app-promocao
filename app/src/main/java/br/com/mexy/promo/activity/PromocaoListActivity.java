package br.com.mexy.promo.activity;

import static br.com.mexy.promo.util.StaticInstances.estabelecimentos;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.adapter.PromocaoListAdapter;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Promocao;
import br.com.mexy.promo.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PromocaoListActivity extends AppCompatActivity {

    private RecyclerView recyclerListPromocao;
    private List<Promocao> promocoes = new ArrayList<>();
    private int position;
    private Retrofit retrofit;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promocao_list);

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recyclerListPromocao = (RecyclerView) findViewById(R.id.recyclerListPromocao);

        //Define layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2 );
        recyclerListPromocao.setLayoutManager( layoutManager );

        recuperarPromocoes();

        PromocaoListAdapter adapter = new PromocaoListAdapter( promocoes );
        recyclerListPromocao.setAdapter( adapter );

        recyclerListPromocao.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerListPromocao,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );


    }

    private void recuperarPromocoes() {

        DataService service = retrofit.create(DataService.class);
        Call<List<Promocao>> promocaoCall = service.recuperarPromocoes();

        promocaoCall.enqueue(new Callback<List<Promocao>>() {
            @Override
            public void onResponse(Call<List<Promocao>> call, Response<List<Promocao>> response) {
                if (response.isSuccessful()) {

                    promocoes.clear();
                    promocoes.addAll(response.body());
                    System.out.println("TESTE "+ promocoes.get(1).getId());
                    PromocaoListAdapter adapter = new PromocaoListAdapter( promocoes );
                    recyclerListPromocao.setAdapter( adapter );
                }
            }

            @Override
            public void onFailure(Call<List<Promocao>> call, Throwable t) {
                // TODO
            }
        });

    }
}
