package br.edu.ifsp.arq.g2.dao;

import java.util.HashMap;

import br.edu.ifsp.arq.g2.model.Avaliacao;

public class AvaliacaoDAO {
	
	HashMap<Integer, Avaliacao> avaliacoes;
	private static AvaliacaoDAO instance;
	
	private AvaliacaoDAO() {
		this.avaliacoes = new HashMap<>();
	}
	
	public static AvaliacaoDAO getInstance() {
		if (instance == null) {
			instance = new AvaliacaoDAO();
		}
		return instance;
	}
	
	public double getAvaliacao(Integer idNoticia) {
		return this.avaliacoes.get(idNoticia).getNota();
	}
}
