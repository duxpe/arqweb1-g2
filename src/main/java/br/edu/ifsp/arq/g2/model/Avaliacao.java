package br.edu.ifsp.arq.g2.model;

public class Avaliacao {

    private int totalNotas = 0;
    private double somaNotas = 0.0;
    private double mediaNotas = 0.0;

    public Avaliacao() {
    }

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
            this.mediaNotas = 0.0;
        }
    }

    public double getMediaNotas() {
        return mediaNotas;
    }

    public int getTotalNotas() {
        return totalNotas;
    }

    public double getSomaNotas() {
        return somaNotas;
    }

    public void setTotalNotas(int totalNotas) {
        this.totalNotas = totalNotas;
        calcularMedia();
    }

    public void setSomaNotas(double somaNotas) {
        this.somaNotas = somaNotas;
        calcularMedia();
    }

    @Override
    public String toString() {
        return "Avaliacao [totalNotas=" + totalNotas + ", somaNotas=" + somaNotas + ", mediaNotas=" + mediaNotas + "]";
    }
}
