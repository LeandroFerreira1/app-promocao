package br.com.mexy.promo.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Promocao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetPerfil extends BottomSheetDialogFragment {

    private ImageView imageViewProduto;
    private TextView textViewMarca;
    private TextView textViewEstabelecimento;
    private Button buttonPerfil;
    private ProgressBar progressBar;
    private Promocao promocao = new Promocao();
    private Integer idPromocao;
    private Retrofit retrofit;

    @Nullable
    @Override public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        idPromocao = getArguments().getInt("idPromocao");

        return inflater
                .inflate(R.layout.bottom_sheet_perfil, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        imageViewProduto = view.findViewById(R.id.ImgPromocao);
        textViewMarca = view.findViewById(R.id.textViewMarca);
        textViewEstabelecimento = view.findViewById(R.id.textEstabelecimento);
        buttonPerfil = view.findViewById(R.id.buttonPerfil);
        progressBar = view.findViewById(R.id.progressBar);


        imageViewProduto.setVisibility(View.GONE);
        textViewEstabelecimento.setVisibility(View.GONE);
        textViewMarca.setVisibility(View.GONE);
        buttonPerfil.setVisibility(View.GONE);

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recuperarPromocao(idPromocao);
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

    private void recuperarPromocao(Integer id) {

        DataService service = retrofit.create(DataService.class);
        Call<Promocao> promocaoCall = service.buscarPromocao(id);

        promocaoCall.enqueue(new Callback<Promocao>() {
            @Override
            public void onResponse(Call<Promocao> call, Response<Promocao> response) {
                if (response.isSuccessful()) {
                    promocao = response.body();

                    // TESTE
                    Picasso.get()
                            .load(DataService.BASE_URL + promocao.getProduto().getUrlImagem())
                            .error(R.drawable.ic_error)
                            .into(imageViewProduto);

                    textViewMarca.setText(promocao.getProduto().getMarca());
                    textViewEstabelecimento.setText(promocao.getEstabelecimento().getNome());

                    progressBar.setVisibility(View.GONE);

                    imageViewProduto.setVisibility(View.VISIBLE);
                    textViewEstabelecimento.setVisibility(View.VISIBLE);
                    textViewMarca.setVisibility(View.VISIBLE);
                    buttonPerfil.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onFailure(Call<Promocao> call, Throwable t) {
                // TODO
            }
        });
    }

}