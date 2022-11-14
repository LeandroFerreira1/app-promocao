package br.com.mexy.promo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.appcompat.app.AlertDialog;
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
import br.com.mexy.promo.adapter.PromocaoCardAdapter;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Curtida;
import br.com.mexy.promo.model.Promocao;
import br.com.mexy.promo.model.Usuario;
import br.com.mexy.promo.util.RecyclerItemClickListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostFragment extends Fragment {

    private FragmentActivity myContext;
    private Integer idUsuario;
    public View view;
    private Retrofit retrofit;
    private RecyclerView recyclerListPromo;
    private List<Promocao> promocoes = new ArrayList<>();
    private Usuario usuario = new Usuario();
    PromocaoCardAdapter adapter;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        String res = sharedPreferences.getString("ID_USUARIO", null);

        String token = "Bearer " + res;

        System.out.println("TESTE: "+ token);


        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        logado(token);

        recyclerListPromo = view.findViewById(R.id.recyclerListPromo);

        //Define layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(myContext, 2 );
        recyclerListPromo.setLayoutManager( layoutManager );

        adapter = new PromocaoCardAdapter( promocoes );
        recyclerListPromo.setAdapter( adapter );


        recyclerListPromo.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        myContext,
                        recyclerListPromo,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(myContext);

                                //Configura título e mensagem
                                dialog.setTitle("Confirmar exclusão");
                                dialog.setMessage("Deseja excluir a oferta?");

                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deletePromocao(token, promocoes.get(position).getId());
                                    }
                                });

                                dialog.setNegativeButton("Não", null );
                                //Exibir dialog
                                dialog.create();
                                dialog.show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        return view;
    }

    private void logado(String token) {
        DataService service = retrofit.create(DataService.class);
        final Call<Usuario> usuarioCall = service.logado(token);

        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    usuario = response.body();
                    recuperarPromocoes(usuario.getId());
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    private void recuperarPromocoes(Integer id) {

        DataService service = retrofit.create(DataService.class);
        Call<List<Promocao>> promocaoCall = service.recuperarPromocoesUsuario(id);

        promocaoCall.enqueue(new Callback<List<Promocao>>() {
            @Override
            public void onResponse(Call<List<Promocao>> call, Response<List<Promocao>> response) {
                if (response.isSuccessful()) {

                    promocoes.clear();
                    promocoes.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Promocao>> call, Throwable t) {
                // TODO
            }
        });

    }

    private void deletePromocao(String token, Integer id) {

        DataService service = retrofit.create(DataService.class);
        final Call<ResponseBody> curtidaCall = service.deletarPromocao(id, token);

        curtidaCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    recuperarPromocoes(usuario.getId());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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

