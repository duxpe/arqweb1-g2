package br.edu.ifsp.arq.g2.controller.noticia;

import java.io.IOException;
import java.util.Base64; // Para converter imagem para Base64
import java.util.List;
import java.util.stream.Collectors; // Para facilitar a manipulação de listas

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson; // Importe Gson
import com.google.gson.GsonBuilder; // Para formatar JSON de forma mais legível (opcional)

import br.edu.ifsp.arq.g2.dao.NoticiaDAO;
import br.edu.ifsp.arq.g2.model.Noticia;

@WebServlet({"/listar-noticia"})
public class ReadNoticiaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private NoticiaDAO dao = NoticiaDAO.getInstance();
    // Instância do Gson para serialização JSON
    // Use setPrettyPrinting() para JSON formatado e legível (útil em desenvolvimento)
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ReadNoticiaServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // Não precisamos mais da sessão para o retorno JSON principal
        // HttpSession session = request.getSession();

        // Sempre defina o tipo de conteúdo como JSON para este servlet
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // String errorMessage = ""; // Não precisamos mais desta variável global para erro em JSP

        try {
            String buscarParam = request.getParameter("buscar");
            String idParam = request.getParameter("id");

            // --- Lógica para retornar uma ÚNICA notícia por ID ---
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    int id = Integer.parseInt(idParam);
                    Noticia selecionada = dao.getNoticia(id);
                    if (selecionada != null) {
                        selecionada.addVisualizacao(); // Lógica de negócio, se necessário
                        
                        // Mapeia Noticia para um Map temporário para incluir a imagem em Base64
                        response.getWriter().write(gson.toJson(mapNoticiaToJsonCompatible(selecionada)));
                        return; // Retorna e encerra a requisição
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                        response.getWriter().write(gson.toJson(new ErrorMessage("Notícia não encontrada para o ID: " + id)));
                        return;
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
                    response.getWriter().write(gson.toJson(new ErrorMessage("ID de notícia inválido.")));
                    return;
                }
            }

            // --- Lógica para retornar uma LISTA de notícias (com ou sem filtro) ---
            List<Noticia> noticias;
            if (buscarParam != null && !buscarParam.isEmpty()) {
                noticias = dao.buscar(buscarParam);
            } else {
                noticias = dao.getnoticias();
            }

            if (noticias == null || noticias.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_OK); // 200 OK, mas sem conteúdo
                response.getWriter().write("[]"); // Retorna um array JSON vazio
                return;
            }

            // Converte a lista de Noticia para uma lista de Maps (JSON-compatível)
            List<Object> noticiasJson = noticias.stream()
                                                .map(this::mapNoticiaToJsonCompatible)
                                                .collect(Collectors.toList());

            response.getWriter().write(gson.toJson(noticiasJson));
            return; // Retorna e encerra a requisição

        } catch (Exception ex) {
            // Loga o erro completo no console do servidor para depuração
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            // Retorna uma mensagem de erro em JSON
            response.getWriter().write(gson.toJson(new ErrorMessage("Erro interno do servidor: " + ex.getMessage())));
        }
    }

    // Método auxiliar para mapear Noticia para um tipo compatível com JSON,
    // incluindo a imagem em Base64, sem modificar a classe Noticia original.
    private Object mapNoticiaToJsonCompatible(Noticia noticia) {
        String imagemBase64 = null;
        if (noticia.getImagem() != null) {
            imagemBase64 = Base64.getEncoder().encodeToString(noticia.getImagem());
        }

        // Usamos um Map simples para construir o objeto JSON dinamicamente
        // ou você pode criar uma classe interna estática simples para isso.
        // Se a classe Noticia tiver getters para todos os campos relevantes,
        // o Gson vai serializá-los diretamente, exceto a imagem (byte[]).
        // Criar um Map<String, Object> nos dá controle total.
        java.util.Map<String, Object> jsonMap = new java.util.LinkedHashMap<>();
        jsonMap.put("id", noticia.getId());
        jsonMap.put("titulo", noticia.getTitulo());
        jsonMap.put("resumo", noticia.getResumo());
        jsonMap.put("dataPublicacao", noticia.getDataPublicacao() != null ? noticia.getDataPublicacao().toString() : null);
        jsonMap.put("nomeAutor", noticia.getNomeAutor());
        jsonMap.put("categoria", noticia.getCategoria());
        jsonMap.put("imagem", imagemBase64); // Imagem já em Base64
        jsonMap.put("visualizacoes", noticia.getVisualizacoes());
        jsonMap.put("conteudo", noticia.getConteudo());


        // Adicione outros campos da sua classe Noticia conforme necessário
        // Ex: jsonMap.put("visualizacoes", noticia.getVisualizacoes());

        return jsonMap;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Para uma API RESTful, POST deveria ser para criar recursos.
        // Para este cenário, mantemos o doGet para simplicidade, mas é bom ter em mente.
        doGet(request, response);
    }

    // Classe auxiliar para formatar mensagens de erro como JSON
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