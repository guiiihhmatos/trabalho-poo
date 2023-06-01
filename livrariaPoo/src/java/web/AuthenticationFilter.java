/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/api/*"})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialização do filtro
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Verificar se a URL contém /api/ e não é /api/session/
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.contains("/api/") && !requestURI.contains("/session")) {
            // Verificar a autenticação aqui (exemplo: verificar token de acesso)
            if(httpRequest.getSession().getAttribute("user") == null){
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso não autorizado");
            }else{
                // Se a autenticação for válida, permite que a requisição prossiga
                chain.doFilter(request, response);
            }
            // Caso contrário, redirecionar ou enviar resposta de erro
            // Exemplo: httpResponse.sendRedirect("/login");
            // ou: httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acesso não autorizado");
        } else {
            // Se não for uma URL que requer autenticação, permite que a requisição prossiga
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Liberação de recursos do filtro
    }
}
