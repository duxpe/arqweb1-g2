package br.edu.ifsp.arq.g2.dao;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import br.edu.ifsp.arq.g2.model.Usuario;

public class UsuarioDAO {

	private ArrayList<Usuario> usuarios;
	private static UsuarioDAO instance;

	private UsuarioDAO() {
		this.usuarios = new ArrayList<>();
		
        usuarios.add(new Usuario("admin", "admin",   "Administrador", 30));
        usuarios.add(new Usuario("pedro", "123456", "Pedro", 23));
        usuarios.add(new Usuario("natan", "123456", "Natan", 25));
        usuarios.add(new Usuario("eduardo", "123456", "Eduardo", 28));
		
	}

	public static UsuarioDAO getInstance() {
		if (instance == null) {
			instance = new UsuarioDAO();
		}
		return instance;
	}

	public void addUsuario(Usuario usuario) {
		this.usuarios.add(usuario);
	}

	public ArrayList<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public Usuario getUsuario(int id) throws NoSuchElementException {
		for (Usuario u : this.usuarios) {
			if (u.getId() == id)
				return u;
		}
		throw new NoSuchElementException("Nenhum usuário encontrado!");
	}

	public void removeUsuario(int id) throws NoSuchElementException {
		for (Usuario u : this.usuarios) {
			if (u.getId() == id) {
				this.usuarios.remove(u);
			}
		}
		throw new NoSuchElementException("Nenhum usuário encontrado!");
	}

	public void updateUsuario(int id, String usuario, String senha, String nome, int idade) {
	    for (Usuario u : this.usuarios) {
	        if (u.getId() == id) {
	            if (usuario != null && !usuario.trim().isEmpty()) {
	                u.setUsuario(usuario.trim());
	            }
	            if (senha != null && !senha.trim().isEmpty()) {
	                u.setSenha(senha.trim());
	            }
	            if (nome != null && !nome.trim().isEmpty()) {
	                u.setNome(nome.trim());
	            }
	            
	            if (idade > 0) {
	                u.setIdade(idade);
	            }
	            break;
	        }
	    }
	}

	public Usuario getUsuario(String usuario, String senha) throws NoSuchElementException {
		System.out.println("entrou");

		for (Usuario u : this.usuarios) {
			System.out.println(u.getUsuario());
			System.out.println(u.getSenha());
			System.out.println(usuario);
			System.out.println(senha);
			System.out.println(u.getUsuario() == usuario);
			System.out.println(u.getSenha() == senha);

			if (u.getUsuario().equals(usuario) && u.getSenha().equals(senha)) {
				System.out.println("Got it! " + u.toString());
				return u;
			}
		}
		throw new NoSuchElementException("Nenhum usuário encontrado!");
	}

}
