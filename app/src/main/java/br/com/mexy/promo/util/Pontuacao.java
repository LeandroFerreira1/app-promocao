package br.com.mexy.promo.util;

import java.util.List;

import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Conquista;
import br.com.mexy.promo.model.Usuario;
import br.com.mexy.promo.model.UsuarioConquista;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Pontuacao {

    public static void alteraponto(Retrofit retrofit, String token, Integer ponto) {

        DataService service = retrofit.create(DataService.class);
        final Call<Usuario> usuarioCall = service.alterarponto(token, ponto);

        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

}
