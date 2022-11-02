package br.com.mexy.promo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Usuario implements Parcelable{

	private Integer id;
	private String nome;
	private String sobrenome;
	private String email;
	private String senha;
	private String urlImagem;
	private Integer pontuacao;
	private ArrayList<Promocao> promocoes;

	public Usuario(Parcel in) {
		if (in.readByte() == 0) {
			id = null;
		} else {
			id = in.readInt();
		}
		nome = in.readString();
		sobrenome = in.readString();
		email = in.readString();
		senha = in.readString();
		urlImagem = in.readString();
		pontuacao = in.readInt();
	}

	public Usuario() {

	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (id == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(id);
		}
		dest.writeString(nome);
		dest.writeString(sobrenome);
		dest.writeString(email);
		dest.writeString(senha);
		dest.writeString(urlImagem);
		dest.writeInt(pontuacao);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
		@Override
		public Usuario createFromParcel(Parcel in) {
			return new Usuario(in);
		}

		@Override
		public Usuario[] newArray(int size) {
			return new Usuario[size];
		}
	};

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

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getUrlImagem() {
		return urlImagem;
	}

	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}

	public String toString() {
		return nome + " " + sobrenome;
	}

	public Integer getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(Integer pontuacao) {
		this.pontuacao = pontuacao;
	}

	public ArrayList<Promocao> getPromocoes() {
		return promocoes;
	}

	public void setPromocoes(ArrayList<Promocao> promocoes) {
		this.promocoes = promocoes;
	}

}
