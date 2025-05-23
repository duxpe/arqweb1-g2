package br.edu.ifsp.arq.g2.controller.noticia;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import br.edu.ifsp.arq.g2.dao.NoticiaDAO;
import br.edu.ifsp.arq.g2.model.Noticia;

@WebServlet("/editar-noticia")
@MultipartConfig
public class UpdateNoticiaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private NoticiaDAO dao = NoticiaDAO.getInstance();

    public UpdateNoticiaServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
        	System.out.println("id estava vazio");
            response.sendRedirect(request.getContextPath() + "/listar-noticia");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Noticia noticia = dao.getNoticia(id);
            if (noticia == null) {
                response.sendRedirect(request.getContextPath() + "/listar-noticia");
                return;
            }
            request.setAttribute("noticiaSelecionada", noticia);
            request.getRequestDispatcher("changeNoticia.jsp")
                   .forward(request, response);
        }
        catch (Exception ex) {
        	System.out.println(ex.getMessage());
            response.sendRedirect(request.getContextPath() + "/listar-noticia");
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            String idParam = request.getParameter("id");
            String titulo = request.getParameter("titulo");
            String conteudo = request.getParameter("conteudo");
            String resumo = request.getParameter("resumo");
            String dataPubStr = request.getParameter("dataPublicacao");
            String nomeAutor = request.getParameter("nomeAutor");
            String categoria = request.getParameter("categoria");

            if (idParam == null || titulo == null || conteudo == null
             || resumo  == null || dataPubStr == null
             || nomeAutor == null || categoria == null
             || idParam.isEmpty() || titulo.isEmpty()
             || conteudo.isEmpty() || resumo.isEmpty()
             || dataPubStr.isEmpty() || nomeAutor.isEmpty()
             || categoria.isEmpty()) {
                throw new RuntimeException("Todos os campos são obrigatórios.");
            }

            int id = Integer.parseInt(idParam);
            LocalDate dataPublicacao = LocalDate.parse(dataPubStr);

            dao.updateNoticia(id, titulo, conteudo, resumo,
                              dataPublicacao, nomeAutor, categoria);

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

        request.getRequestDispatcher("changeNoticia.jsp")
               .forward(request, response);
    }
}
