package br.edu.ifsp.arq.g2.controller.avaliacao;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.edu.ifsp.arq.g2.dao.AvaliacaoDAO;
import br.edu.ifsp.arq.g2.model.Avaliacao;

@WebServlet("/obter-avaliacao")
public class ReadAvaliacaoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AvaliacaoDAO avaliacaoDAO = AvaliacaoDAO.getInstance();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ReadAvaliacaoServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idNoticiaParam = request.getParameter("idNoticia");

        if (idNoticiaParam == null || idNoticiaParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ErrorResponse("ID da notícia não fornecido.")));
            return;
        }

        try {
            int idNoticia = Integer.parseInt(idNoticiaParam);
            Optional<Avaliacao> avaliacaoOptional = avaliacaoDAO.getAvaliacaoNoticia(idNoticia);

            if (avaliacaoOptional.isPresent()) {
                response.getWriter().write(gson.toJson(avaliacaoOptional.get()));
            } else {
                response.getWriter().write(gson.toJson(new Avaliacao())); 
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ErrorResponse("ID da notícia inválido. Deve ser um número inteiro.")));
        } catch (Exception e) {
            System.err.println("Erro ao obter avaliação: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Erro inesperado ao obter avaliação: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}