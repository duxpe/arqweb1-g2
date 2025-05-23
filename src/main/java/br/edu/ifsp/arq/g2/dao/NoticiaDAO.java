package br.edu.ifsp.arq.g2.dao;

import java.time.LocalDate;
import java.util.ArrayList;

import br.edu.ifsp.arq.g2.model.Noticia;

public class NoticiaDAO {

	private ArrayList<Noticia> noticias;
	private static NoticiaDAO instance;

	private NoticiaDAO() {
		this.noticias = new ArrayList<>();
		// alguns mocks :)
		noticias.add(new Noticia("Exploração Espacial Comercial decola no Brasil",
				"Agência aeroespacial nacional anuncia novos contratos com startups de satélites privados.",
				"Novos contratos privados impulsionam a indústria espacial nacional.", LocalDate.of(2025, 1, 15),
				"Mariana Silva", "Ciência"));

		noticias.add(new Noticia("Feira de Tecnologia em São Paulo reúne mais de 200 expositores",
				"Evento traz inovações em IA, IoT e sustentabilidade até o fim do mês no Expo Center Norte.",
				"Mais de 200 empresas apresentam soluções de IA, IoT e sustentabilidade.", LocalDate.of(2025, 2, 10),
				"Carlos Pereira", "Tecnologia"));

		noticias.add(new Noticia("Startup local lança app de finanças pessoais com foco em investimentos",
				"A FinTrack promete ajudar usuários a controlar gastos e projetar carteiras diversificadas.",
				"App facilita gestão de gastos e projeção de investimentos.", LocalDate.of(2025, 3, 5), "Ana Costa",
				"Economia"));

		noticias.add(new Noticia("Novo recorde de público no campeonato nacional de e-sports",
				"Final do torneio de jogos eletrônicos atraiu mais de 50 mil espectadores online.",
				"Torneio nacional de e-sports bate recorde de audiência.", LocalDate.of(2025, 4, 22), "Rafael Souza",
				"Esportes"));

	}

	public static NoticiaDAO getInstance() {
		if (instance == null) {
			instance = new NoticiaDAO();
		}
		return instance;
	}

	public void addNoticia(Noticia Noticia) {
		this.noticias.add(Noticia);
	}

	public ArrayList<Noticia> getnoticias() {
		return this.noticias;
	}

	public Noticia getNoticia(int id) throws Exception {
		for (Noticia n : this.noticias) {
			if (n.getId() == id)
				return n;
		}
		throw new Exception("Nenhum usuário encontrado!");
	}

	public void removeNoticia(int id) throws Exception {
		for (Noticia n : this.noticias) {
			if (n.getId() == id) {
				this.noticias.remove(n);
			}
		}
		throw new Exception("Nenhum usuário encontrado!");
	}

	public void updateNoticia(int id, String titulo, String texto, String resumo, LocalDate dataPublicacao,
			String nomeAutor, String categoria) {
		for (Noticia n : this.noticias) {
			if (n.getId() == id) {
				n.setTitulo(titulo);
				n.setTexto(texto);
				n.setResumo(resumo);
				n.setDataPublicacao(dataPublicacao);
				n.setNomeAutor(nomeAutor);
				n.setCategoria(categoria);
				break;
			}
		}
	}

}
