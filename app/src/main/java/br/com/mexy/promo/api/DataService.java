package br.com.mexy.promo.api;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import br.com.mexy.promo.model.Departamento;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Produto;
import br.com.mexy.promo.model.Promocao;
import br.com.mexy.promo.model.Result;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DataService {

    public static final String BASE_URL = "http://10.0.0.173:8000";
   // String API_KEY = "501e0a13-f1ed-4c86-b7ec-c36d8b55c7ae";

    @GET("/api/v1/promocoes/")
    Call<List<Promocao>> recuperarPromocoes();

    @GET("/api/v1/estabelecimentos/promocoes/")
    Call<List<Estabelecimento>> recuperarEstabelecimentos();

    @GET("/api/v1/promocoes/{id}")
    Call<Promocao> buscarPromocao(@Path("id") Integer id);

    @GET("/api/v1/estabelecimentos/promocao/{id}")
    Call<Estabelecimento> buscarPromocoes(@Path("id") Integer id);

    @POST("/api/v1/produtos/ean/{ean}")
    Call<Result> registrarProdutoEan(@Path("ean") String ean);

    @GET("/api/v1/produtos/")
    Call<List<Produto>> buscarProdutos();

    @GET("/api/v1/produtos/{id}")
    Call<Produto> buscarProduto(@Path("id") BigInteger id);

    @PATCH("/api/v1/produtos/{id}")
    Call<Produto> alterarProduto(@Path("id") BigInteger id, @Body Produto produto);

    @GET("/api/v1/departamentos/")
    Call<List<Departamento>> recuperarDepartamentos();





/*
    @GET("/api/audio")
    Call<List<Musica>> recuperarMusicas(@Query("key") String key);*/

}