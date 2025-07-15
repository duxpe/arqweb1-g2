package br.edu.ifsp.arq.g2.controller.comentario;

import java.io.IOException;
import java.util.Collections; // Para Collections.singletonMap
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;          // Import do Gson
import com.google.gson.GsonBuilder;   // Import do GsonBuilder

import br.edu.ifsp.arq.g2.dao.ComentarioDAO; // Seu DAO de comentários
import br.edu.ifsp.arq.g2.model.Comentario;   // Seu modelo Comentario

@WebServlet("/obter-comentarios")
public class ReadComentarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ComentarioDAO comentarioDAO = ComentarioDAO.getInstance();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ReadComentarioServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String idNoticiaParam = request.getParameter("idNoticia");

		if (idNoticiaParam == null || idNoticiaParam.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(gson.toJson(Collections.singletonMap("message", "Precisa fornecer um ID valido.")));
			return;
		}

		try {
			int idNoticia = Integer.parseInt(idNoticiaParam);
			List<Comentario> comentarios = comentarioDAO.getComentariosByNoticiaId(idNoticia);
			
			response.getWriter().write(gson.toJson(comentarios));

		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(gson.toJson(Collections.singletonMap("message", "ID da notícia inválido. Deve ser um número inteiro.")));
		} catch (Exception e) {
			System.err.println("Erro ao obter comentários: " + e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(gson.toJson(Collections.singletonMap("message", "Erro inesperado ao obter comentários: " + e.getMessage())));
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}