package br.edu.ifsp.arq.g2.model;

public class Avaliacao {

    // Não precisamos de idNoticia aqui, pois o HashMap no DAO usará isso como chave.
    // Este objeto representa a avaliação AGREGADA de UMA notícia.
    private int totalNotas = 0; // Quantidade total de notas recebidas
    private double somaNotas = 0.0; // Soma de todas as notas para calcular a média
    private double mediaNotas = 0.0; // Média calculada

    // Construtor padrão, usado quando uma nova avaliação agregada é inicializada
    public Avaliacao() {
        // Inicializa com 0 notas e 0 soma, media será 0.0
    }

    // Método para adicionar uma nova nota individual e atualizar a média
    public void adicionarNota(double novaNota) throws Exception {
        if (novaNota < 0 || novaNota > 5) {
            throw new Exception("A nota deve ser entre 0 e 5!");
        }
        this.somaNotas += novaNota;
        this.totalNotas++;
        calcularMedia();
    }

    private void calcularMedia() {
        if (totalNotas > 0) {
            this.mediaNotas = somaNotas / totalNotas;
        } else {
            this.mediaNotas = 0.0; // Se não há notas, a média é 0
        }
    }

    public double getMediaNotas() {
        return mediaNotas;
    }
    
    // Métodos para o DAO poder carregar/salvar o estado (se fosse de um DB)
    public int getTotalNotas() {
        return totalNotas;
    }

    public double getSomaNotas() {
        return somaNotas;
    }

    public void setTotalNotas(int totalNotas) {
        this.totalNotas = totalNotas;
        calcularMedia(); // Recalcula a média ao definir o total de notas
    }

    public void setSomaNotas(double somaNotas) {
        this.somaNotas = somaNotas;
        calcularMedia(); // Recalcula a média ao definir a soma das notas
    }
    
    // Método toString para depuração
    @Override
    public String toString() {
        return "Avaliacao [totalNotas=" + totalNotas + ", somaNotas=" + somaNotas + ", mediaNotas=" + mediaNotas + "]";
    }
}