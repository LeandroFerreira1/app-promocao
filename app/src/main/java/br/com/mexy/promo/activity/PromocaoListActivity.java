package br.com.mexy.promo.activity;

import static br.com.mexy.promo.util.StaticInstances.estabelecimentos;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.adapter.EstabelecimentoAdapter;
import br.com.mexy.promo.adapter.PromocaoCardAdapter;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Promocao;
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

        PromocaoCardAdapter adapter = new PromocaoCardAdapter( promocoes );
        recyclerListPromocao.setAdapter( adapter );


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
                    PromocaoCardAdapter adapter = new PromocaoCardAdapter( promocoes );
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
