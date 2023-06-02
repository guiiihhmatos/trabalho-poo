package web.contoller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.util.List;
import model.User;

@Path("/users")
public class UserController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() throws Exception {
        List<User> users = User.getUsers();
        return users;
    }
    
    @GET
    @Path("/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUsersByLogin(@PathParam("login") String login) throws Exception {
        User user = User.getUserByLogin(login);
        return user;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User pUser) throws Exception {
        boolean criado = User.insertUser(pUser.getLogin(), pUser.getName(), 
                                            pUser.getRole(), pUser.getPasswordHash());
        
        if (criado) {
            // Construir a URL para o novo recurso criado (user)            
            UriBuilder uriBuilder = UriBuilder.fromResource(UserController.class)
                                              .path(pUser.getLogin());
            String userUrl = uriBuilder.build().toString();
            
            // Retornar uma resposta com código 201 (Created) e o URL do novo user
            return Response.created(uriBuilder.build())
                           .entity("User criado com sucesso: " + userUrl)
                           .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Falha ao criar o user")
                           .build();
        }        
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(User pUser) throws Exception {        
        boolean atualizado = User.updateUser(pUser.getLogin(), pUser.getName(), 
                                            pUser.getRole(), pUser.getPasswordHash());
        if (atualizado) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Falha ao atualizar o user")
                           .build();
        }
    }     

    @DELETE
    @Path("/{rowId}")
    public Response deleteUser(@PathParam("rowId") Long rowId) throws Exception {
        boolean excluido = User.deleteUser(rowId);        
        if (excluido) {
            return Response.ok().entity("User excluído com sucesso").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Falha ao excluir o user")
                           .build();
        }
    }
}