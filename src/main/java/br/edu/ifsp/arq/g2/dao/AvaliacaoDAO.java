package br.edu.ifsp.arq.g2.dao;

import java.util.HashMap;
import java.util.Optional; // Importar Optional, ideal para retorno de objetos que podem não existir

import br.edu.ifsp.arq.g2.model.Avaliacao;

public class AvaliacaoDAO {

    // Armazena um objeto Avaliacao (que contém a média e a contagem) para cada idNoticia.
    // A chave é o ID da Notícia, e o valor é o objeto Avaliacao agregada para aquela notícia.
    private HashMap<Integer, Avaliacao> avaliacoesPorNoticia; 
    private static AvaliacaoDAO instance;
    
    private AvaliacaoDAO() {
        this.avaliacoesPorNoticia = new HashMap<>();
    }
    
    public static AvaliacaoDAO getInstance() {
        if (instance == null) {
            instance = new AvaliacaoDAO();
        }
        return instance;
    }
    
    /**
     * Adiciona uma nova nota para uma notícia e atualiza sua média.
     * @param idNoticia O ID da notícia a ser avaliada.
     * @param nota A nota a ser adicionada (entre 0 e 5).
     * @throws Exception Se a nota for inválida ou ocorrer um erro ao atualizar.
     */
    public void addNotaNoticia(int idNoticia, double nota) throws Exception {
        // Obtém a avaliação existente para esta notícia ou cria uma nova se não existir
        Avaliacao avaliacao = avaliacoesPorNoticia.get(idNoticia);
        if (avaliacao == null) {
            avaliacao = new Avaliacao(); // Cria um novo objeto de avaliação agregada
            avaliacoesPorNoticia.put(idNoticia, avaliacao); // Adiciona ao mapa
        }
        
        // Adiciona a nova nota ao objeto de avaliação e ele recalcula sua média
        avaliacao.adicionarNota(nota); 
        System.out.println("AvaliacaoDAO: Nota " + nota + " adicionada para Notícia ID " + idNoticia + ". " + avaliacao.toString());
    }

    /**
     * Obtém o objeto Avaliacao (com a média e total de notas) para uma notícia específica.
     * @param idNoticia O ID da notícia.
     * @return Um Optional contendo o objeto Avaliacao se existir, ou um Optional vazio.
     */
    public Optional<Avaliacao> getAvaliacaoNoticia(Integer idNoticia) {
        return Optional.ofNullable(this.avaliacoesPorNoticia.get(idNoticia));
    }
}