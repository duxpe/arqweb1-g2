package br.edu.ifsp.arq.g2.model;

import java.time.LocalDate;

public class Noticia {

	private static int lastId = 0;
	private String titulo;
	private String conteudo;
	private LocalDate dataPublicacao;
	private String nomeAutor;
	private String resumo;
	private int visualizacoes = 0;
	private String categoria;
	private int id;

	public Noticia() {
		this.id = Noticia.lastId++;
	}

	public Noticia(String titulo, String conteudo, String resumo, LocalDate dataPublicacao, String nomeAutor, String categoria) {
		this();
		setTitulo(titulo);
		setConteudo(conteudo);
		setDataPublicacao(dataPublicacao);
		setNomeAutor(nomeAutor);
		setCategoria(categoria);
		setResumo(resumo);
		
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public int getVisualizacoes() {
		return visualizacoes;
	}

	public void addVisualizacao() {
		visualizacoes++;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String texto) {
		this.conteudo = texto;
	}

	public LocalDate getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(LocalDate dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public String getNomeAutor() {
		return nomeAutor;
	}

	public void setNomeAutor(String nomeAutor) {
		this.nomeAutor = nomeAutor;
	}

	public int getId() {
		return id;
	}
}
