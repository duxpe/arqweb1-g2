package br.edu.ifsp.arq.g2.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

import br.edu.ifsp.arq.g2.model.Noticia;

public class NoticiaDAO {

	private ArrayList<Noticia> noticias;
	private static NoticiaDAO instance;

	private NoticiaDAO() {
		this.noticias = new ArrayList<>();

		// mocks! Agora com imagens hehe
		noticias.add(createMock("Exploração Espacial Comercial decola no Brasil",
				"Agência aeroespacial nacional anuncia novos contratos com startups de satélites privados.",
				"Novos contratos privados impulsionam a indústria espacial nacional.", LocalDate.of(2025, 1, 15),
				"Mariana Silva", "Ciência", "space.jpg"));

		noticias.add(createMock("Feira de Tecnologia em São Paulo reúne mais de 200 expositores",
				"Evento traz inovações em IA, IoT e sustentabilidade até o fim do mês no Expo Center Norte.",
				"Mais de 200 empresas apresentam soluções de IA, IoT e sustentabilidade.", LocalDate.of(2025, 2, 10),
				"Carlos Pereira", "Tecnologia", "tecnologia.jpg"));

		noticias.add(createMock("Startup local lança app de finanças pessoais com foco em investimentos",
				"A FinTrack promete ajudar usuários a controlar gastos e projetar carteiras diversificadas.",
				"App facilita gestão de gastos e projeção de investimentos.", LocalDate.of(2025, 3, 5), "Ana Costa",
				"Economia", "finance.jpg"));

		noticias.add(createMock("Novo recorde de público no campeonato nacional de e-sports",
				"Final do torneio de jogos eletrônicos atraiu mais de 50 mil espectadores online.",
				"Torneio nacional de e-sports bate recorde de audiência.", LocalDate.of(2025, 4, 22), "Rafael Souza",
				"Esportes", "esport.jpg"));
	}

	public static NoticiaDAO getInstance() {
		if (instance == null) {
			instance = new NoticiaDAO();
		}
		return instance;
	}

	public void addNoticia(Noticia noticia) {
		this.noticias.add(noticia);
	}

	public ArrayList<Noticia> getnoticias() {
		return this.noticias;
	}

	public Noticia getNoticia(int id) throws Exception {
		for (Noticia n : this.noticias) {
			if (n.getId() == id)
				return n;
		}
		throw new Exception("Notícia não encontrada!");
	}

	public void removeNoticia(int id) throws Exception {
		for (Noticia n : this.noticias) {
			if (n.getId() == id) {
				this.noticias.remove(n);
				return;
			}
		}
		throw new Exception("Notícia não encontrada!");
	}

	public void updateNoticia(int id, String titulo, String texto, String resumo, LocalDate dataPublicacao,
			String nomeAutor, String categoria, byte[] imagemBytes) {
		for (Noticia n : this.noticias) {
			if (n.getId() == id) {
				n.setTitulo(titulo);
				n.setConteudo(texto);
				n.setResumo(resumo);
				n.setDataPublicacao(dataPublicacao);
				n.setNomeAutor(nomeAutor);
				n.setCategoria(categoria);
				if (imagemBytes != null && imagemBytes.length > 0) {
					n.setImagem(imagemBytes);
				}
				break;
			}
		}
	}

	private Noticia createMock(String titulo, String conteudo, String resumo, LocalDate data, String autor,
			String categoria, String imagePath) {
		Noticia n = new Noticia(titulo, conteudo, resumo, data, autor, categoria);
		
		try (InputStream is = NoticiaDAO.class.getClassLoader().getResourceAsStream("assets/" + imagePath)) {
			if (is != null) {
				byte[] bytes = is.readAllBytes();
				n.setImagem(bytes);
			} else {
				System.err.println("Recurso não encontrado: assets/" + imagePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return n;
	}
}
