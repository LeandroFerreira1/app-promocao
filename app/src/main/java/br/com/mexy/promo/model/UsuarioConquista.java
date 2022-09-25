package br.com.mexy.promo.model;

import java.util.Date;

public class UsuarioConquista {

	private Usuario usuario;
	private Conquista conquista;
	private Date dataConquista;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Conquista getConquista() {
		return conquista;
	}

	public void setConquista(Conquista conquista) {
		this.conquista = conquista;
	}

	public Date getDataConquista() {
		return dataConquista;
	}

	public void setDataConquista(Date dataConquista) {
		this.dataConquista = dataConquista;
	}

}
