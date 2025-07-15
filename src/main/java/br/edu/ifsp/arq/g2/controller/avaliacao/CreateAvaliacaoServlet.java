package br.edu.ifsp.arq.g2.controller.avaliacao;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.ifsp.arq.g2.dao.AvaliacaoDAO;
import br.edu.ifsp.arq.g2.model.Usuario; // Assumindo que você tem um modelo Usuario

@WebServlet("/avaliar-noticia")
public class CreateAvaliacaoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AvaliacaoDAO avaliacaoDAO = AvaliacaoDAO.getInstance();

    public CreateAvaliacaoServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        String contextPath = request.getContextPath();
        
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            response.sendRedirect(contextPath + "/login.html?erro=Precisa estar logado para avaliar.");
            return;
        }

        String idNoticiaParam = request.getParameter("idNoticia");
        String notaParam = request.getParameter("nota");
        
        int idNoticia = -1;
        double nota = -1;
        String errorMessage = null;

        try {
            if (idNoticiaParam == null || idNoticiaParam.isEmpty()) {
                errorMessage = "ID da notícia não fornecido.";
            } else {
                idNoticia = Integer.parseInt(idNoticiaParam);
            }

            if (notaParam == null || notaParam.isEmpty()) {
                errorMessage = "Nota não fornecida.";
            } else {
                nota = Double.parseDouble(notaParam);
                if (nota < 0 || nota > 5) { //melhor garantir né kkk
                    errorMessage = "A nota deve ser entre 0 e 5.";
                }
            }

            if (errorMessage != null) {
                response.sendRedirect(contextPath + "/viewNoticia.html?id=" + idNoticia + "&erro=" + errorMessage);
                return;
            }

            avaliacaoDAO.addNotaNoticia(idNoticia, nota);

            response.sendRedirect(contextPath + "/viewNoticia.html?id=" + idNoticia + "&sucesso=Avaliada com sucesso!");

        } catch (NumberFormatException e) {
            errorMessage = "ID da noticia ou nota invalida. Por favor, insira numeros validos.";
            response.sendRedirect(contextPath + "/viewNoticia.html?id=" + (idNoticia != -1 ? idNoticia : "") + "&erro=" + errorMessage);
        } catch (Exception e) {
            System.err.println("Erro ao processar avaliação: " + e.getMessage());
            e.printStackTrace();
            errorMessage = "Erro inesperado ao avaliar: " + e.getMessage();
            response.sendRedirect(contextPath + "/viewNoticia.html?id=" + (idNoticia != -1 ? idNoticia : "") + "&erro=" + errorMessage);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idNoticiaParam = request.getParameter("idNoticia");
        String contextPath = request.getContextPath();
        if (idNoticiaParam != null && !idNoticiaParam.isEmpty()) {
            response.sendRedirect(contextPath + "/viewNoticia.html?id=" + idNoticiaParam);
        } else {
            response.sendRedirect(contextPath + "/index.html");
        }
    }
}