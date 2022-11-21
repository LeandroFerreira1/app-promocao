package br.com.mexy.promo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Promocao implements Parcelable{

	private Integer id;

	private String valor_original;

	private String valor_promocional;

	@SerializedName("data_validade")
	private String dataValidade;

	private Integer curtida;

	private Produto produto;

	private ArrayList<Curtida> curtidas;

	@SerializedName("criador")
	private Usuario usuario;

	private  Estabelecimento estabelecimento;

	public Promocao() {
	}

	public Promocao(Parcel in) {
		if (in.readByte() == 0) {
			id = null;
		} else {
			id = in.readInt();
		}
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (id == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(id);
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Promocao> CREATOR = new Parcelable.Creator<Promocao>() {
		@Override
		public Promocao createFromParcel(Parcel in) {
			return new Promocao(in);
		}

		@Override
		public Promocao[] newArray(int size) {
			return new Promocao[size];
		}
	};

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
	public ArrayList<Curtida> getCurtidas() {
		return curtidas;
	}

	public void setCurtidas(ArrayList<Curtida> curtidas) {
		this.curtidas = curtidas;
	}


    public void onSelected(Promocao promocao) {
    }
}
