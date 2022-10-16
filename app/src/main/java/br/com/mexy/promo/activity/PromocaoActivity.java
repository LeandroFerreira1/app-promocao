package br.com.mexy.promo.activity;

import android.app.SharedElementCallback;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.mexy.promo.R;
import br.com.mexy.promo.fragment.BottomSheetCadastroProduto;
import br.com.mexy.promo.fragment.BottomSheetEstabelecimento;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.util.CustomInterface;
import br.com.mexy.promo.util.Mask;
import br.com.mexy.promo.util.MoneyTextWatcher;

public class PromocaoActivity extends AppCompatActivity  implements CustomInterface{

    private Button buttonEstabelecimento;
    BottomSheetEstabelecimento bottomSheetEstabelecimento;
    private CustomInterface callback;

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
        editDataValidade.addTextChangedListener(Mask.insert("##/##/####", editDataValidade));

        EditText editValorPromocional = (EditText) findViewById(R.id.editValorPromocional);
        editValorPromocional.setText("0,00");
        editValorPromocional.addTextChangedListener(new MoneyTextWatcher(editValorPromocional));

        EditText editValorOriginal = (EditText) findViewById(R.id.editValorOriginal);
        editValorOriginal.setText("0,00");
        editValorOriginal.addTextChangedListener(new MoneyTextWatcher(editValorOriginal));*/

        buttonEstabelecimento = (Button) findViewById(R.id.buttonEstabelecimento);

        buttonEstabelecimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetEstabelecimento = new BottomSheetEstabelecimento(callback);
                bottomSheetEstabelecimento.show(getSupportFragmentManager(), bottomSheetEstabelecimento.getTag());
            }
        });

    }


    @Override
    public void callbackMethod(Estabelecimento estabelecimento) {
        System.out.println("TESTE1 "+ estabelecimento.getNome());
    }
}