package br.edu.ifsp.arq.g2.controller.noticia;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import br.edu.ifsp.arq.g2.dao.NoticiaDAO;


@WebServlet("/editar-noticia")
public class UpdateNoticiaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private NoticiaDAO dao = NoticiaDAO.getInstance();

    public UpdateNoticiaServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            String idParam = request.getParameter("id");
            String titulo = request.getParameter("titulo");
            String texto = request.getParameter("texto");
            String resumo = request.getParameter("resumo");
            String dataPubStr = request.getParameter("dataPublicacao");
            String nomeAutor = request.getParameter("nomeAutor");
            String categoria = request.getParameter("categoria");

            if (idParam == null || titulo == null || texto == null || resumo == null
             || dataPubStr == null || nomeAutor == null || categoria == null
             || idParam.isEmpty() || titulo.isEmpty() || texto.isEmpty() || resumo.isEmpty()
             || dataPubStr.isEmpty() || nomeAutor.isEmpty() || categoria.isEmpty()) {
                throw new RuntimeException("Todos os campos são obrigatórios.");
            }

            int id = Integer.parseInt(idParam);
            LocalDate dataPublicacao = LocalDate.parse(dataPubStr);

            dao.updateNoticia(id, titulo, texto, resumo, dataPublicacao, nomeAutor, categoria);

            response.sendRedirect(request.getContextPath() + "/listar-noticia");
            return;
        }
        catch (NumberFormatException | DateTimeParseException ex) {
            request.setAttribute("erro", "Parâmetro inválido: " + ex.getMessage());
        }
        catch (RuntimeException ex) {
            request.setAttribute("erro", ex.getMessage());
        }
        catch (Exception ex) {
            request.setAttribute("erro", "Erro ao atualizar notícia: " + ex.getMessage());
        }

        request.getRequestDispatcher("changeNoticia.jsp").forward(request, response);
    }
}
