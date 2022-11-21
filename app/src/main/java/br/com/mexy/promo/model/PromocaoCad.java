package br.com.mexy.promo.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;
import java.util.Date;

public class PromocaoCad {

    private Integer id;

    private String valor_original;

    private String valor_promocional;

    private String data_validade;

    private Integer usuario_id;

    private  Integer estabelecimento_id;

    private Integer produto_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValorOriginal() {
        return valor_original;
    }

    public void setValorOriginal(String valor_original) {
        this.valor_original = valor_original;
    }

    public String getValorPromocional() {
        return valor_promocional;
    }

    public void setValorPromocional(String valor_promocional) {
        this.valor_promocional = valor_promocional;
    }

    public String getDataValidade() {
        return data_validade;
    }

    public void setDataValidade(String data_validade) {
        this.data_validade = data_validade;
    }

    public Integer getProduto() {
        return produto_id;
    }

    public void setProduto(Integer produto_id) {
        this.produto_id = produto_id;
    }

    public Integer getEstabelecimento() {
        return estabelecimento_id;
    }

    public void setEstabelecimento(Integer estabelecimento_id) {
        this.estabelecimento_id = estabelecimento_id;
    }

    public Integer getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }

}

