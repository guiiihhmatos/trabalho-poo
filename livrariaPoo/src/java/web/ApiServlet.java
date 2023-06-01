package web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Livro;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(name = "ApiServlet", urlPatterns = {"/api/*"})
public class ApiServlet extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {   
        response.setContentType("application/json;charset=UTF-8");
        JSONObject file = new JSONObject();
        String path = request.getPathInfo();
        try {
            if (path.endsWith("session")) {
                processSession(file, request, response);
            } else if (path.endsWith("users")) {
                processUsers(file, request, response);
            } else if (path.endsWith("livros")) {
                processLivros(file, request, response);
            } else {
               response.setStatus(HttpServletResponse.SC_NOT_FOUND);
               file.put("error", "Invalid URL");
            }
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error: " + ex.getLocalizedMessage());
        }
        response.getWriter().print(file.toString());        
    }

    private void processSession(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String method = request.getMethod();
        if (method.equals("GET")) {
            handleSessionGet(file, request, response);
        } else if (method.equals("PUT")) {
            handleSessionPut(file, request, response);
        } else if (method.equals("DELETE")) {
            handleSessionDelete(file, request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }        
    }
    
    private void processUsers(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String method = request.getMethod();
        if (method.equals("GET")) {
            handleUserGet(file, request, response);
        } else if (method.equals("PUT")) {
            handleUserPut(file, request, response);
        } else if (method.equals("DELETE")) {
            handleUserDelete(file, request, response);
        } else if (method.equals("POST")) {
            handleUserPost(file, request, response);            
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }          
    }

    private void processLivros(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String method = request.getMethod();
        if (method.equals("GET")) {
            handleLivrosGet(file, request, response);
        } else if (method.equals("PUT")) {
            handleLivrosPut(file, request, response);
        } else if (method.equals("DELETE")) {
            handleLivrosDelete(file, request, response);
        } else if (method.equals("POST")) {
            handleLivrosPost(file, request, response);            
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }   
    }
       
    private void handleSessionGet(JSONObject file, HttpServletRequest request, HttpServletResponse response) {
        User usuario = (User) request.getSession().getAttribute("user");
        request.getSession().setAttribute("user", usuario);
        file.put("id", usuario.getId());
        file.put("login", usuario.getLogin());
        file.put("name", usuario.getName());
        file.put("role", usuario.getRole());
        file.put("passwordHash", usuario.getPasswordHash());
        file.put("message", "Logged in");               
    }

    private void handleSessionDelete(JSONObject file, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        file.put("message", "Logged out");
    }

    private void handleSessionPut(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {  
        JSONObject body = JSONObjectBuilder.createJSONObject(HttpHelper.getHttpBody(request.getReader()));
        String login = body.getString("login");
        String password = body.getString("password");

        User usuario = User.getUser(login, password);

        if(usuario == null)
        {            
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Login or password incorreto.");

        } else 
        {           
            file.put("id", usuario.getId());
            file.put("login", usuario.getLogin());
            file.put("name", usuario.getName());
            file.put("role", usuario.getRole());
           // file.put("passwordHash", usuario.getPasswordHash());
            file.put("message", "Logged in");
        }                    
    }

    private void handleUserGet(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception {
         file.put("list", new JSONArray(User.getUsers()));
    }
    private void handleUserDelete(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = Long.parseLong(request.getParameter("id"));
        User.deleteUser(id);
    }

    private void handleUserPost(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        JSONObject body = JSONObjectBuilder.createJSONObject(HttpHelper.getHttpBody(request.getReader()));
        String login = body.getString("login");
        String name = body.getString("name");
        String role = body.getString("role");
        String password = body.getString("password");
        User.insertUser(login, name, role, password);        
    }

    private void handleUserPut(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {            
        JSONObject body = JSONObjectBuilder.createJSONObject(HttpHelper.getHttpBody(request.getReader()));
        String login = body.getString("login");
        String name = body.getString("name");
        String role = body.getString("role");
        String password = body.getString("password");
        User.updateUser(login, name, role, password);        
    }

    private void handleLivrosGet(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getParameter("disponivel") != null) {
            file.put("list", new JSONArray(Livro.getLivros()));
        } else {
            file.put("list", new JSONArray(Livro.getAllLivros()));
        }
    }

    private void handleLivrosDelete(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = Long.parseLong(request.getParameter("id"));
        Livro.deleteLivro(id);
    }
    
    private void handleLivrosPut(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {        
        JSONObject body  = JSONObjectBuilder.createJSONObject(HttpHelper.getHttpBody(request.getReader()));
        String titulo = body.getString("titulo");
        String autor = body.getString("autor");
        String editora = body.getString("editora");
        int ano_publicacao = body.getInt("ano_publicacao");
        String isbn = body.getString("isbn");
        String descricao = body.getString("descricao");
        Boolean disponibilidade = body.getBoolean("disponibilidade");

        Livro.updateLivro(titulo, autor, editora, ano_publicacao, isbn, descricao, disponibilidade);          
    }    

    private void handleLivrosPost(JSONObject file, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        JSONObject body = JSONObjectBuilder.createJSONObject(HttpHelper.getHttpBody(request.getReader()));
        String titulo = body.getString("titulo");
        String autor = body.getString("autor");
        String editora = body.getString("editora");
        int ano_publicacao = body.getInt("ano_publicacao");
        String isbn = body.getString("isbn");
        String descricao = body.getString("descricao");
        Boolean disponibilidade = body.getBoolean("disponibilidade");

        Livro.insertLivro(titulo, autor, editora, ano_publicacao, isbn, descricao, disponibilidade);
    }
    
    
 @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
