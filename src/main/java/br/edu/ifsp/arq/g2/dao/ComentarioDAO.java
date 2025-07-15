package br.edu.ifsp.arq.g2.dao;

import br.edu.ifsp.arq.g2.model.Comentario;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class ComentarioDAO {

	private static final List<Comentario> comentarios = new ArrayList<>();
	private static ComentarioDAO instance;

	private ComentarioDAO() {
		comentarios.add(new Comentario(1, "Otima noticia sobre exploracao espacial! Muito informativo.", "JoaoSilva"));
		comentarios.add(new Comentario(1, "Concordo! Ansioso por mais novidades do setor.", "MariaSouza"));
		comentarios.add(new Comentario(2, "Interessante, mas faltou aprofundar em alguns pontos.", "CarlosAlberto"));
	}

	public static synchronized ComentarioDAO getInstance() {
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

	public List<Comentario> getComentariosByNoticiaId(int idNoticia) {
		List<Comentario> comentariosDaNoticia = new ArrayList<>();
		for (Comentario c : comentarios) {
			if (c.getIdNoticia() == idNoticia) {
				comentariosDaNoticia.add(c);
			}
		}
		return Collections.unmodifiableList(comentariosDaNoticia);
	}
}
