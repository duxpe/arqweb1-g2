package br.edu.ifsp.arq.g2.model;

public class Usuario {
	private static int lastId = 0;
	private String usuario;
	private String senha;
	private String nome;
	private boolean isAdmin = true;
	private int idade;
	private int id;
	
	public Usuario() {
		this.id = Usuario.lastId++;
	}
	
	public Usuario(String usuario, String senha, String nome, int idade) {
		this();
		setUsuario(usuario);
		setSenha(senha);
		setNome(nome);
		setIdade(idade);
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public boolean isAdmin() {
		return this.isAdmin;
	}

	public boolean setIsAdmin(boolean isAdmin, Usuario user) throws Exception {
		if (!user.isAdmin()) throw new Exception("Usuário precisa ser admin para definir essa propriedade!");
		this.isAdmin = isAdmin;
		return this.isAdmin;
	}

	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public int getId() {
		return id;
	}
	
	
}
