package br.edu.ifsp.arq.g2.model;

public class Comentario {
	
	private String comentario;
	private int idNoticia;
	private String nomeUsuario;
	
	public Comentario(int idNoticia, String comentario, String nomeUsuario) {
		this.setNomeUsuario(nomeUsuario);
		this.setComentario(comentario);
		this.setIdNoticia(idNoticia);
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public int getIdNoticia() {
		return idNoticia;
	}

	public void setIdNoticia(int idNoticia) {
		this.idNoticia = idNoticia;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
}
