package br.com.mexy.promo.model;

import com.google.gson.annotations.SerializedName;

public class Conquista {

    private Integer id;
    private String nome;
    private Integer valor;
    private String urlimagem;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getImagem() {
        return urlimagem;
    }

    public void setImagem(String urlimagem) {
        this.urlimagem = urlimagem;
    }
}
