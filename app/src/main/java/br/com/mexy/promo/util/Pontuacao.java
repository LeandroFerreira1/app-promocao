package br.com.mexy.promo.util;

import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Usuario;
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

    public static void verificaponto(Retrofit retrofit, String token, Usuario usuario) {

        int ponto = usuario.getPontuacao();/*

        if(ponto >= 15 || ponto <= 44 ){
            conquista 1
        } else if(ponto >= 45 || ponto <= 74){
            conquista 2
        }else if(ponto >= 45 || ponto <= 154){
            conquista 3
        }else if(ponto >= 155 || ponto <= 299){
            conquista 4
        }else if(ponto >= 300 || ponto <= 599) {
            conquista 6
        }else if(ponto >= 600) {
            conquista 7
        }*/

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
