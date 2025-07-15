package br.edu.ifsp.arq.g2.dao;

import java.util.ArrayList;

import br.edu.ifsp.arq.g2.model.Comentario;

public class ComentarioDAO {
	
	ArrayList<Comentario> comentarios;
	private static ComentarioDAO instance;
	
	private ComentarioDAO() {
		this.comentarios = new ArrayList<>();
	}

	public static ComentarioDAO getInstance() {
		if(instance == null) {
			instance = new ComentarioDAO();
		}
		return instance;
	}

	public void addComentario(Comentario comentario) {
		this.comentarios.add(comentario);
	}
	
	public void rmComentario(Comentario comentario) {
		this.comentarios.remove(comentario);
	}
}
