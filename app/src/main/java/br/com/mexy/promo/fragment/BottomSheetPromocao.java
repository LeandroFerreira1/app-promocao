package br.com.mexy.promo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import br.com.mexy.promo.R;
import br.com.mexy.promo.activity.MainActivity;
import br.com.mexy.promo.adapter.PromocaoAdapter;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Promocao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetPromocao extends BottomSheetDialogFragment {

    private ProgressBar progressBar;
    private Estabelecimento estabelecimento = new Estabelecimento();
    private Integer idEstabelecimento;
    private Retrofit retrofit;
    private RecyclerView recyclerEstabelecimento;
    private FragmentActivity myContext;
    private ArrayList<Promocao> promocoes;

    @Nullable
    @Override public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        idEstabelecimento = getArguments().getInt("idEstabelecimento");

        return inflater
                .inflate(R.layout.bottom_sheet_promocao, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recyclerEstabelecimento = view.findViewById(R.id.recyclerEstabelecimentos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                myContext, LinearLayoutManager.HORIZONTAL,false);
        recyclerEstabelecimento.setLayoutManager(layoutManager);

        recuperarEstabelecimentoPromocao(idEstabelecimento);

/*
        buttonPerfil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), PerfilActivity.class);
                intent.putExtra("idArtesao", idArtesao);
                startActivity(intent);
            }
        });*/
/*
        imageViewPerfil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), PerfilActivity.class);
                intent.putExtra("idArtesao", idArtesao);
                startActivity(intent);
            }
        });*/
    }


    private void recuperarEstabelecimentoPromocao(Integer id) {

        DataService service = retrofit.create(DataService.class);
        Call<Estabelecimento> estabelecimentoCall = service.buscarPromocoes(id);

        estabelecimentoCall.enqueue(new Callback<Estabelecimento>() {
            @Override
            public void onResponse(Call<Estabelecimento> call, Response<Estabelecimento> response) {
                if (response.isSuccessful()) {

                    estabelecimento = response.body();
                    promocoes = estabelecimento.getPromocoes();
                    System.out.println("TESTE ESTA: "+ estabelecimento.getNome());
                    PromocaoAdapter adapter = new PromocaoAdapter(promocoes);
                    recyclerEstabelecimento.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Estabelecimento> call, Throwable t) {
                // TODO
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(MainActivity) activity;
        super.onAttach(activity);
    }


}