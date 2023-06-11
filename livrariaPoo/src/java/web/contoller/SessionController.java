package web.contoller;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.User;
import web.commons.JsonUtils;

@Path("/session")
@Produces(MediaType.APPLICATION_JSON)
public class SessionController {

    @PUT
    @Path("/authenticate")
    public Response autenticarUser(User pUser, @Context HttpServletRequest request) throws Exception {
        User userModel = User.getUser(pUser.getLogin(), pUser.getPasswordHash());            
        if(userModel == null){
            return loginErrorMessage();
        }
        request.getSession(true).setAttribute("user", userModel);
        String json = JsonUtils.toJson(userModel);
        return Response.ok(json, "application/json").build(); 
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserSessionDb(User pUser) throws Exception {
        User userModel = User.getUser(pUser.getLogin(), pUser.getPasswordHash());

        if (userModel == null) {
            return loginErrorMessage();
        }
        String json = JsonUtils.toJson(userModel);
        return Response.ok(json, "application/json").build();
    }

    private Response loginErrorMessage() {
        JsonObject mensagemErro = Json.createObjectBuilder()
                .add("message", "Login ou senha inválidos.")
                .build();
        return Response.status(Response.Status.FORBIDDEN).entity(mensagemErro).build();
    }
    
    @DELETE
    public Response removerUserSession(@Context HttpServletRequest request) throws Exception {
        request.getSession().removeAttribute("user"); 
        JsonObject mensagem = Json.createObjectBuilder()
                    .add("message", "Logged out")
                    .build();
        return Response.ok(mensagem).build();               
    }
        
}