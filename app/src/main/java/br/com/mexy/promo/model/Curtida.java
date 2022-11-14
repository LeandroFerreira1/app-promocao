package br.com.mexy.promo.model;

import com.google.gson.annotations.SerializedName;

public class Curtida {

    private Integer usuario_id;
    private Integer promocao_id;
    @SerializedName("criador")
    private Usuario usuario;

    public Integer getUsuario() {
        return usuario_id;
    }

    public void setUsuario(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }

    public Integer getPromocao() {
        return promocao_id;
    }

    public void setPromocao(Integer promocao_id) {
        this.promocao_id = promocao_id;
    }

    public Usuario getUsuarioComp() {
        return usuario;
    }

    public void setUsuarioComp (Usuario usuario) {
        this.usuario = usuario;
    }
}
