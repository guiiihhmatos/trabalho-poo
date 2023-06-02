package web.contoller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.User;
import web.commons.JsonUtils;
import web.commons.RequestUtils;

@Path("/session")
public class SessionController {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserSession(User pUser, @Context HttpServletRequest request) throws Exception {
        User userModel = User.getUser(pUser.getLogin(), pUser.getPasswordHash());            
        if(userModel == null){
            String mensagemErro = "Acesso proibido. Você não tem permissão para acessar este recurso.";
            return Response.status(Response.Status.FORBIDDEN).entity(mensagemErro).build();
        }         
        request.getSession(true).setAttribute("user", userModel);
        String json = JsonUtils.toJson(userModel);
        return Response.ok(json, "application/json").build(); 
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response autenticarUser(User pUser) {
        try {            
            User userModel = User.getUser(pUser.getLogin(), pUser.getPasswordHash());
            
            if(userModel == null){
                String mensagemErro = "Login ou senha inválidos.";
                return Response.status(Response.Status.FORBIDDEN).entity(mensagemErro).build();
            }         
            String json = JsonUtils.toJson(userModel);
            return Response.ok(json, "application/json").build();            
        } catch (Exception e) {
            e.printStackTrace();
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Erro ao acessar session")
                           .build();
        }
    }
    
    @DELETE
    public Response removerUserSession(@Context HttpServletRequest request) throws Exception {
        request.getSession().removeAttribute("user");        
        return Response.ok("Logged out").build();               
    }
        
}

