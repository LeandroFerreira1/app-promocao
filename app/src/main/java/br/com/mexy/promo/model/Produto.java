package br.com.mexy.promo.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class Produto {

	private BigInteger id;
	private String nome;
	private String marca;
	private Integer departamento_id;
	private String urlImagem;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public Integer getDepartamento() {
		return departamento_id;
	}

	public void setDepartamento(Integer departamento_id) {
		this.departamento_id = departamento_id;
	}

	public String getUrlImagem() {
		return urlImagem;
	}

	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}

}
