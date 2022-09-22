package br.com.mexy.promo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_promocao);

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
        editValorOriginal.addTextChangedListener(new MoneyTextWatcher(editValorOriginal));

    }
}