package br.edu.ifsp.arq.g2.controller.noticia;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.edu.ifsp.arq.g2.dao.NoticiaDAO;
import br.edu.ifsp.arq.g2.model.Noticia;

@WebServlet({"/listar-noticia"})
public class ReadNoticiaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private NoticiaDAO dao = NoticiaDAO.getInstance();
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ReadNoticiaServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String buscarParam = request.getParameter("buscar");
            String idParam = request.getParameter("id");

            if (idParam != null && !idParam.isEmpty()) {
                try {
                    int id = Integer.parseInt(idParam);
                    Noticia selecionada = dao.getNoticia(id);
                    if (selecionada != null) {
                        selecionada.addVisualizacao();
                        response.getWriter().write(gson.toJson(mapNoticiaToJsonCompatible(selecionada)));
                        return;
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write(gson.toJson(new ErrorMessage("Noticia nao encontrada para o ID: " + id)));
                        return;
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write(gson.toJson(new ErrorMessage("ID de noticia invalido.")));
                    return;
                }
            }

            List<Noticia> noticias;
            if (buscarParam != null && !buscarParam.isEmpty()) {
                noticias = dao.buscar(buscarParam);
            } else {
                noticias = dao.getnoticias();
            }

            if (noticias == null || noticias.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("[]");
                return;
            }

            List<Object> noticiasJson = noticias.stream()
                                                .map(this::mapNoticiaToJsonCompatible)
                                                .collect(Collectors.toList());

            response.getWriter().write(gson.toJson(noticiasJson));
            return;

        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorMessage("Erro interno do servidor: " + ex.getMessage())));
        }
    }

    private Object mapNoticiaToJsonCompatible(Noticia noticia) {
        String imagemBase64 = null;
        if (noticia.getImagem() != null) {
            imagemBase64 = Base64.getEncoder().encodeToString(noticia.getImagem());
        }

        java.util.Map<String, Object> jsonMap = new java.util.LinkedHashMap<>();
        jsonMap.put("id", noticia.getId());
        jsonMap.put("titulo", noticia.getTitulo());
        jsonMap.put("resumo", noticia.getResumo());
        jsonMap.put("dataPublicacao", noticia.getDataPublicacao() != null ? noticia.getDataPublicacao().toString() : null);
        jsonMap.put("nomeAutor", noticia.getNomeAutor());
        jsonMap.put("categoria", noticia.getCategoria());
        jsonMap.put("imagem", imagemBase64);
        jsonMap.put("visualizacoes", noticia.getVisualizacoes());
        jsonMap.put("conteudo", noticia.getConteudo());

        return jsonMap;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private static class ErrorMessage {
        private String message;

        public ErrorMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
