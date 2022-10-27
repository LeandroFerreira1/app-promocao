package br.com.mexy.promo.activity;

import android.app.SharedElementCallback;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.fragment.BottomSheetCadastroProduto;
import br.com.mexy.promo.fragment.BottomSheetEstabelecimento;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Produto;
import br.com.mexy.promo.model.Promocao;
import br.com.mexy.promo.util.CustomInterface;
import br.com.mexy.promo.util.Mask;
import br.com.mexy.promo.util.MoneyTextWatcher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PromocaoActivity extends AppCompatActivity  implements CustomInterface{

    private Button buttonEstabelecimento;
    private ImageButton buttonProduto;
    BottomSheetEstabelecimento bottomSheetEstabelecimento;
    private CustomInterface callback;
    private TextView textViewEstabelecimento;
    private ImageView imageViewProdu;
    private Retrofit retrofit;
    private Produto produto = new Produto();
    private String idEstabelecimento;
    private Promocao promocao = new Promocao();
    private Estabelecimento estabelecimentoCadastro = new Estabelecimento();
    private EditText editValorPromocional;
    private EditText editValorOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_promocao);

        callback=this;
/*
        Date data = new Date(System.currentTimeMillis());
        SimpleDateFormat formatarDate = new SimpleDateFormat("dd/MM/yyyy");

        EditText editDataValidade = (EditText) findViewById(R.id.editDataValidade);
        editDataValidade.setText(formatarDate.format(data));
        editDataValidade.addTextChangedListener(Mask.insert("##/##/####", editDataValidade));*/

        editValorPromocional = (EditText) findViewById(R.id.editValorPromocional);
        editValorPromocional.setText("0,00");
        editValorPromocional.addTextChangedListener(new MoneyTextWatcher(editValorPromocional));

        editValorOriginal = (EditText) findViewById(R.id.editValorOriginal);
        editValorOriginal.setText("0,00");
        editValorOriginal.addTextChangedListener(new MoneyTextWatcher(editValorOriginal));

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        String res = sharedPreferences.getString("ID_USUARIO", null);

        String token = "Bearer " + res;

        retrofit = new Retrofit.Builder()
                .baseUrl(DataService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        buttonEstabelecimento = (Button) findViewById(R.id.buttonEstabelecimento);
        buttonProduto = (ImageButton ) findViewById(R.id.imageButtonProduto);
        textViewEstabelecimento = findViewById(R.id.textViewEstabelecimento);
        imageViewProdu = (ImageView) findViewById(R.id.imageViewProdu);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            idEstabelecimento = extras.getString("produto");
            System.out.println("TESTE: "+ idEstabelecimento);
            buscarProduto(idEstabelecimento);
        }

        buttonEstabelecimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetEstabelecimento = new BottomSheetEstabelecimento(callback);
                bottomSheetEstabelecimento.show(getSupportFragmentManager(), bottomSheetEstabelecimento.getTag());
            }
        });
    }


    private void buscarProduto(final String str) {

        BigInteger id = new BigInteger(str);
        DataService service = retrofit.create(DataService.class);
        final Call<Produto> produtoCall = service.buscarProduto(id);

        produtoCall.enqueue(new Callback<Produto>() {
            @Override
            public void onResponse(Call<Produto> call, Response<Produto> response) {
                if (response.isSuccessful()) {
                    produto = response.body();
                    Picasso.get()
                            .load(DataService.BASE_URL + produto.getUrlImagem())
                            .error(R.drawable.ic_error)
                            .into(imageViewProdu);
                }
            }

            @Override
            public void onFailure(Call<Produto> call, Throwable t) {
                System.out.println("FALHA: " + t.toString());
            }
        });
    }

    private void registrarPromocao(final String token) {

        promocao.setProduto(produto);
        promocao.setEstabelecimento(estabelecimentoCadastro);
        promocao.setValorPromocional(String.valueOf(editValorPromocional.getText()));
        promocao.setValorOriginal(String.valueOf(editValorOriginal.getText()));

        DataService service = retrofit.create(DataService.class);
        final Call<Promocao> promocaoCall = service.registrarPromocao(token, promocao);

        promocaoCall.enqueue(new Callback<Promocao>() {
            @Override
            public void onResponse(Call<Promocao> call, Response<Promocao> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<Promocao> call, Throwable t) {
                System.out.println("FALHA: " + t.toString());
            }
        });

    }


    @Override
    public void callbackMethod(Estabelecimento estabelecimento) {
        textViewEstabelecimento.setText(estabelecimento.getNome());
        estabelecimentoCadastro = estabelecimento;
    }

    @Override
    public void callbackMethodPhoto(String photo) {

    }
}