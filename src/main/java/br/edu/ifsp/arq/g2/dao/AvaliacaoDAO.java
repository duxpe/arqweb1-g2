package br.edu.ifsp.arq.g2.dao;

import java.util.HashMap;
import java.util.Optional;

import br.edu.ifsp.arq.g2.model.Avaliacao;

public class AvaliacaoDAO {

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
    
    public void addNotaNoticia(int idNoticia, double nota) throws Exception {
        Avaliacao avaliacao = avaliacoesPorNoticia.get(idNoticia);
        if (avaliacao == null) {
            avaliacao = new Avaliacao();
            avaliacoesPorNoticia.put(idNoticia, avaliacao);
        }
        avaliacao.adicionarNota(nota); 
    }

    public Optional<Avaliacao> getAvaliacaoNoticia(Integer idNoticia) {
        return Optional.ofNullable(this.avaliacoesPorNoticia.get(idNoticia));
    }
}
