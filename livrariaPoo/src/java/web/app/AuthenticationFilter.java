package web.app;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
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

        String requestURI = httpRequest.getRequestURI();
        if (!requestURI.contains("/session/authenticate")) {
            // Verificar a autenticação aqui (exemplo: verificar token de acesso)
            if(httpRequest.getSession().getAttribute("user") == null){                
                JsonObject mensagemErro = Json.createObjectBuilder()
                .add("message", "Acesso proibido. Você não tem permissão para acessar este recurso.")
                .build();
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResponse.getWriter().write(mensagemErro.toString());                
            }else{
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