package br.com.mexy.promo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Estabelecimento implements Parcelable {
	private Integer id;
	private String nome;
	private String endereco;
	private String telefone;
	private String latitude;
	private String longitude;
	private String urlImagem;
	private ArrayList<Promocao> promocoes;

	protected Estabelecimento(Parcel in) {
		if (in.readByte() == 0) {
			id = null;
		} else {
			id = in.readInt();
		}
		nome = in.readString();
		endereco = in.readString();
		telefone = in.readString();
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
		dest.writeString(endereco);
		dest.writeString(telefone);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Estabelecimento> CREATOR = new Creator<Estabelecimento>() {
		@Override
		public Estabelecimento createFromParcel(Parcel in) {
			return new Estabelecimento(in);
		}

		@Override
		public Estabelecimento[] newArray(int size) {
			return new Estabelecimento[size];
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
