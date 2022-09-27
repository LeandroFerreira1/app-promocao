package br.com.mexy.promo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Estabelecimento {
	private Integer id;
	@SerializedName("nome")
	private String nome;
	private String endereco;
	private String telefone;
	private String latitude;
	private String longitude;
	private String urlImagem;
	private ArrayList<Promocao> promocoes;

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

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getUrlImagem() {
		return urlImagem;
	}

	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}

	public ArrayList<Promocao> getPromocoes() {
		return promocoes;
	}

	public void setPromocoes(ArrayList<Promocao> promocoes) {
		this.promocoes = promocoes;
	}

}
