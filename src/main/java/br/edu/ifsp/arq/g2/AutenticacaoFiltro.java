//package br.edu.ifsp.arq.g2;
//
//import java.io.IOException;
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.*;
//
//@WebFilter("/*")
//public class AutenticacaoFiltro implements Filter {
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
//            throws IOException, ServletException {
//        
//        HttpServletRequest  request  = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//        String uri = request.getRequestURI();
//        String context = request.getContextPath();
//
//        boolean openPage = uri.equals(context + "/login.jsp")
//                        || uri.equals(context + "/auth")
//                        || uri.startsWith(context + "/resources/")
//                        || uri.equals(context + "/")
//                        || uri.equals(context + "/listar-noticia")
//                        || uri.equals(context + "/addUsuario.jsp")
//                        || uri.equals(context + "/criar-usuario")
//                        || uri.equals(context + "/sobre.jsp");
//
//        HttpSession session = request.getSession(false);
//        boolean logged = (session != null && session.getAttribute("usuarioLogado") != null);
//
//        if (!openPage && !logged) {
//            response.sendRedirect(context + "/login.jsp");
//            return;
//        }
//
//        chain.doFilter(req, res);
//    }
//
//	@Override
//	public void init(FilterConfig filterConfig) throws ServletException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void destroy() {
//		// TODO Auto-generated method stub
//		
//	}
//}
