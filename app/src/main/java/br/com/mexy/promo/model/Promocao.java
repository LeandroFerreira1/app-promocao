package br.com.mexy.promo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Promocao {

	private Integer id;

	private String valor_original;

	private String valor_promocional;

	private String dataValidade;

	private Integer curtida;

	private Produto produto;

	private Usuario usuario;

	private  Estabelecimento estabelecimento;

	public Promocao() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValorOriginal() {
		return valor_original;
	}

	public void setValorOriginal(String valorOriginal) {
		this.valor_original = valor_original;
	}

	public String getValorPromocional() {
		return valor_promocional;
	}

	public void setValorPromocional(String valor_promocional) {
		this.valor_promocional = valor_promocional;
	}

	public String getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(String dataValidade) {
		this.dataValidade = dataValidade;
	}

	public Integer getCurtida() {
		return curtida;
	}

	public void setCurtida(Integer curtida) {
		this.curtida = curtida;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Estabelecimento getEstabelecimento() {
		return estabelecimento;
	}

	public void setEstabelecimento(Estabelecimento estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

}
