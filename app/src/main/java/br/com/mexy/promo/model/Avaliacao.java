package br.com.mexy.promo.model;

public class Avaliacao {

//	private Integer id;
	private Integer usuario_id;
	private Integer promocao_id;
	private String descricao;
	private Integer nota;
	private String latitude;
	private String longitude;

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getNota() {
		return nota;
	}

	public void setNota(Integer nota) {
		this.nota = nota;
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


}
