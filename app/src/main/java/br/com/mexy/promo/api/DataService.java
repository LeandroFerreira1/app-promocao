package br.com.mexy.promo.api;

import java.util.List;

import br.com.mexy.promo.model.Promocao;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DataService {

    public static final String BASE_URL = "http://192.168.0.109:8000";
   // String API_KEY = "501e0a13-f1ed-4c86-b7ec-c36d8b55c7ae";

    @GET("/api/v1/promocoes/")
    Call<List<Promocao>> recuperarPromocoes();

    @GET("/api/v1/promocoes/{id}")
    Call<Promocao> buscarPromocao(@Path("id") Integer id);

/*
    @GET("/api/audio")
    Call<List<Musica>> recuperarMusicas(@Query("key") String key);*/

}