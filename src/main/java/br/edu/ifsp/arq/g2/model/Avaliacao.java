package br.edu.ifsp.arq.g2.model;

public class Avaliacao {
	
	private int qtdNota = 0;
	private double nota;
	
	public Avaliacao(double nota, String nomeUsuario) throws Exception {
		setNota(nota);
	}

	public double getNota() {
		return nota;
	}

	public void setNota(double nota) throws Exception {
		if(this.nota >= 0 && this.nota <= 5) {		
			this.qtdNota++;
			this.nota = nota / qtdNota;
		} else {
			throw new Exception("A nota deve ser entre 0 e 5!");
		}
	}
	
}