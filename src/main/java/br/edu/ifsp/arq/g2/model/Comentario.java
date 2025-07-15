package br.edu.ifsp.arq.g2.model;

public class Comentario {
	
	private String content;
	private int idNoticia;
	private String nome;
	
	public Comentario(int idNoticia, String content, String nome) {
		this.setNome(nome);
		this.setContent(content);
		this.setIdNoticia(idNoticia);
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getNome() {
		return content;
	}
	public void setNome(String content) {
		this.content = content;
	}
	
	public int getIdNoticia() {
		return idNoticia;
	}
	public void setIdNoticia(int idNoticia) {
		this.idNoticia = idNoticia;
	}
	
	
	
}
