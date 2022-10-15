package br.com.mexy.promo.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.adapter.EstabelecimentoAdapter;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.util.StaticInstances;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstabelecimentoActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private List<Estabelecimento> estabelecimentos = new ArrayList<>();
    private RecyclerView recyclerEstabelecimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento);

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recyclerEstabelecimento = findViewById(R.id.recyclerEstabelecimento);

        //Define layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1 );
        recyclerEstabelecimento.setLayoutManager( layoutManager );

        recuperarEstabelecimentos();

        EstabelecimentoAdapter adapter = new EstabelecimentoAdapter( estabelecimentos );
        recyclerEstabelecimento.setAdapter( adapter );

    }

    private void recuperarEstabelecimentos() {

        DataService service = retrofit.create(DataService.class);
        Call<List<Estabelecimento>> estabelecimentoCall = service.recuperarEstabelecimentos();

        estabelecimentoCall.enqueue(new Callback<List<Estabelecimento>>() {
            @Override
            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                if (response.isSuccessful()) {

                    StaticInstances.estabelecimentos.clear();
                    StaticInstances.estabelecimentos.addAll(response.body());
                    estabelecimentos = StaticInstances.estabelecimentos;

                    EstabelecimentoAdapter adapter = new EstabelecimentoAdapter(estabelecimentos);
                    recyclerEstabelecimento.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                // TODO
            }
        });

    }
}