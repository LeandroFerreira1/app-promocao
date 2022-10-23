package br.com.mexy.promo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.activity.MainActivity;
import br.com.mexy.promo.activity.PromocaoActivity;
import br.com.mexy.promo.adapter.EstabelecimentoAdapter;
import br.com.mexy.promo.adapter.PromocaoAdapter;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Promocao;
import br.com.mexy.promo.util.CustomInterface;
import br.com.mexy.promo.util.RecyclerItemClickListener;
import br.com.mexy.promo.util.StaticInstances;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetEstabelecimento extends BottomSheetDialogFragment {


    private final CustomInterface callback;
    private ProgressBar progressBar;
    private Retrofit retrofit;
    private RecyclerView recyclerEstabelecimentoEscolha;
    private FragmentActivity myContext;
    private ArrayList<Estabelecimento> estabelecimentos = new ArrayList<>();

    public BottomSheetEstabelecimento(CustomInterface callback) {
        this.callback=callback;
    }


    @Nullable
    @Override public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        return inflater
                .inflate(R.layout.estabelecimento_bottom_sheet_layout, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recyclerEstabelecimentoEscolha = view.findViewById(R.id.recyclerEstabelecimentoEscolha);

        //Define layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(myContext, 1 );
        recyclerEstabelecimentoEscolha.setLayoutManager( layoutManager );

        recuperarEstabelecimentos();

        EstabelecimentoAdapter adapter = new EstabelecimentoAdapter(estabelecimentos);
        recyclerEstabelecimentoEscolha.setAdapter(adapter);

        recyclerEstabelecimentoEscolha.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        myContext,
                        recyclerEstabelecimentoEscolha,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                escolhaEstabelecimento(view, position);
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


    private void recuperarEstabelecimentos() {

        DataService service = retrofit.create(DataService.class);
        Call<List<Estabelecimento>> estabelecimentoCall = service.recuperarEstabelecimentos();

        estabelecimentoCall.enqueue(new Callback<List<Estabelecimento>>() {
            @Override
            public void onResponse(Call<List<Estabelecimento>> call, Response<List<Estabelecimento>> response) {
                if (response.isSuccessful()) {
                    estabelecimentos.clear();
                    estabelecimentos.addAll(response.body());
                    EstabelecimentoAdapter adapter = new EstabelecimentoAdapter(estabelecimentos);
                    recyclerEstabelecimentoEscolha.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Estabelecimento>> call, Throwable t) {
                // TODO
            }
        });

    }

    private void escolhaEstabelecimento(View view, int position) {
        callback.callbackMethod(estabelecimentos.get(position));
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext=(PromocaoActivity) context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            myContext=(PromocaoActivity) activity;
        }
    }

}