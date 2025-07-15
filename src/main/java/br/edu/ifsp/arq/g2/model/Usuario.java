package br.edu.ifsp.arq.g2.model;

public class Usuario {
    private static int lastId = 0;
    private String usuario;
    private String senha;
    private String nome;
    private boolean isAdmin = false;
    private boolean isVisualizador = true;
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
        if(usuario == "admin") {
        	setIsAdmin(true);
        }
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public boolean isVisualizador() {
        return this.isVisualizador;
    }

    public void setVisualizador(boolean visualizador, Usuario user) throws Exception {
        if (!user.isAdmin()) throw new Exception("Somente admin pode definir visualizador!");
        this.isVisualizador = visualizador;
        if (visualizador) this.isAdmin = false;
    }

    public boolean setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        if (isAdmin) this.isVisualizador = false;
        return this.isAdmin;
    }

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
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
