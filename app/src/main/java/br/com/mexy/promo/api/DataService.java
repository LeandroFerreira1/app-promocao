package br.com.mexy.promo.api;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import br.com.mexy.promo.model.Avaliacao;
import br.com.mexy.promo.model.Conquista;
import br.com.mexy.promo.model.Curtida;
import br.com.mexy.promo.model.Departamento;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Produto;
import br.com.mexy.promo.model.Promocao;
import br.com.mexy.promo.model.PromocaoCad;
import br.com.mexy.promo.model.ResponseUsuario;
import br.com.mexy.promo.model.Result;
import br.com.mexy.promo.model.Usuario;
import br.com.mexy.promo.model.UsuarioConquista;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DataService {

    public static final String BASE_URL = "https://api-promo.leandro.mexy.dev";
   // String API_KEY = "501e0a13-f1ed-4c86-b7ec-c36d8b55c7ae";

    @GET("/api/v1/promocoes/")
    Call<List<Promocao>> recuperarPromocoes();

    @GET("/api/v1/promocoes/usuario/{id}")
    Call<List<Promocao>> recuperarPromocoesUsuario(@Path("id") Integer id);

    @GET("/api/v1/estabelecimentos/promocoes/")
    Call<List<Estabelecimento>> recuperarEstabelecimentosPromo();

    @GET("/api/v1/estabelecimentos/")
    Call<List<Estabelecimento>> recuperarEstabelecimentos();

    @GET("/api/v1/promocoes/{id}")
    Call<Promocao> buscarPromocao(@Path("id") Integer id);

    @POST("/api/v1/promocoes/")
    Call<PromocaoCad> registrarPromocao(@Header("Authorization") String token, @Body PromocaoCad promocao);

    @DELETE("/api/v1/promocoes/{id}")
    Call<ResponseBody> deletarPromocao(@Path("id") Integer id, @Header("Authorization") String token);

    @POST("/api/v1/avaliacoes/")
    Call<Avaliacao> registrarAvaliacao(@Header("Authorization") String token, @Body Avaliacao avaliacao);

    @GET("/api/v1/avaliacoes/avaliacoes_promocao/{id}")
    Call<List<Avaliacao>> buscarAvaliacoes(@Path("id") Integer id);

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

    @POST("/api/v1/produtos/")
    Call<Produto> cadastraProduto(@Body Produto produto);

    @GET("/api/v1/departamentos/")
    Call<List<Departamento>> recuperarDepartamentos();

    @POST("/api/v1/upload-images/{id}")
    @Multipart
    Call<String> uploadImageProduto(
            @Path("id") BigInteger id,
            @Part MultipartBody.Part file);

    @GET("/api/v1/usuarios")
    Call<List<Usuario>> listarUsuarios();

    @GET("/api/v1/usuarios/ranking/")
    Call<List<Usuario>> ranking();

    @PATCH("/api/v1/usuarios/pontuacao/{id}")
    Call<Usuario> alterarponto(@Header("Authorization") String token, @Path("id") Integer id);

    @POST("/api/v1/usuarios/login")
    @FormUrlEncoded
    Call<ResponseUsuario> verificarUsuario(@Field("username") String username,
                                           @Field("password") String password);

    @POST("/api/v1/usuarios/signup")
    Call<Usuario> registrarUsuario(@Body Usuario usuario);

    @POST("/api/v1/upload-images/user/{id}")
    @Multipart
    Call<String> uploadImageUser(
            @Path("id") Integer id,
            @Part MultipartBody.Part file);

    @GET("/api/v1/usuarios/logado")
    Call<Usuario> logado(@Header("Authorization") String token);

    @GET("/api/v1/usuarios/{id}")
    Call<Usuario> recuperarUsuario(@Path("id") Integer id);

    @POST("/api/v1/usuario-conquistas/")
    Call<UsuarioConquista> registrarConquista(@Header("Authorization") String token, @Body UsuarioConquista usuarioConquista);

    @GET("/api/v1/usuario-conquistas/")
    Call<List<UsuarioConquista>> recuperarUsuarioConquistas(@Header("Authorization") String token);

    @GET("/api/v1/usuario-conquistas/{id}")
    Call<List<UsuarioConquista>> recuperarUsuarioConquistasAberto(@Path("id") Integer id);

    @GET("/api/v1/conquistas/{id}")
    Call<Conquista> recuperaConquista(@Path("id") Integer id);

    @POST("/api/v1/curtidas/")
    Call<Curtida> registrarCurtida(@Header("Authorization") String token, @Body Curtida curtida);

    @GET("/api/v1/curtidas/curtidas_promocao/{id}")
    Call<List<Curtida>> buscarCurtidas(@Path("id") Integer id);

    @GET("/api/v1/curtidas/curtida_usuario/{id}")
    Call<Curtida> buscarCurtidaUsuario(@Header("Authorization") String token, @Path("id") Integer id);

    @DELETE("/api/v1/curtidas/{id}")
    Call<ResponseBody> deletarCurtida(@Path("id") Integer id, @Header("Authorization") String token);
}