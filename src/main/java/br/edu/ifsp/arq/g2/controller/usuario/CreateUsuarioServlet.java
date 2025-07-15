package br.edu.ifsp.arq.g2.controller.usuario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifsp.arq.g2.dao.UsuarioDAO;
import br.edu.ifsp.arq.g2.model.Usuario;

@WebServlet("/criar-usuario")
public class CreateUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsuarioDAO dao = UsuarioDAO.getInstance();

    public CreateUsuarioServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String contextPath = request.getContextPath();
        System.out.println("CreateUsuarioServlet: Recebendo requisicao POST para /criar-usuario");

        try {
            String usuario = request.getParameter("usuario");
            String senha = request.getParameter("senha");
            String nome = request.getParameter("nome");
            String idadeStr = request.getParameter("idade");

            System.out.println("CreateUsuarioServlet: Dados recebidos: usuario=" + usuario + ", nome=" + nome + ", idadeStr=" + idadeStr);

            if (usuario == null || usuario.isEmpty() || senha == null || senha.isEmpty() || nome == null
                    || nome.isEmpty() || idadeStr == null || idadeStr.isEmpty()) {
                System.out.println("CreateUsuarioServlet: Campos obrigatorios faltando.");
                throw new RuntimeException("Todos os campos sao obrigatorios.");
            }
            
            int idade = Integer.parseInt(idadeStr);
            if (idade < 0 || idade > 150) {
                System.out.println("CreateUsuarioServlet: Idade invalida: " + idade);
                throw new RuntimeException("A idade deve ser entre 0 e 150.");
            }

            Usuario novo = new Usuario(usuario, senha, nome, idade);
            System.out.println("CreateUsuarioServlet: Objeto Usuario criado: " + novo.toString());

            dao.addUsuario(novo);
            System.out.println("CreateUsuarioServlet: Usuario ADICIONADO via DAO. Redirecionando para login.");

            response.sendRedirect(contextPath + "/login.html?sucesso=true&msg=Usuario cadastrado com sucesso! Faca login.");
        } catch (NumberFormatException ex) {
            String errorMsg = "A idade deve ser um numero inteiro valido.";
            System.err.println("CreateUsuarioServlet: Erro de formato de numero: " + errorMsg + " - " + ex.getMessage());
            ex.printStackTrace();
            response.sendRedirect(contextPath + "/addUsuario.html?erro=" + errorMsg);
        } catch (RuntimeException ex) {
            System.err.println("CreateUsuarioServlet: Erro de Runtime: " + ex.getMessage());
            ex.printStackTrace();
            response.sendRedirect(contextPath + "/addUsuario.html?erro=" + ex.getMessage());
        } catch (Exception ex) {
            String errorMsg = "Erro inesperado ao cadastrar usuario: " + ex.getMessage();
            System.err.println("CreateUsuarioServlet: Erro geral: " + errorMsg);
            ex.printStackTrace();
            response.sendRedirect(contextPath + "/addUsuario.html?erro=" + errorMsg);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("CreateUsuarioServlet: Recebendo requisicao GET para /criar-usuario. Redirecionando para addUsuario.html");
        response.sendRedirect(request.getContextPath() + "/addUsuario.html");
    }
}
