package web.contoller;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;
import model.Emprestimo;
import model.Livro;

@Path("/emprestimo")
public class EmprestimoController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Emprestimo> getAllEmprestimos() throws Exception {
        List<Emprestimo> emprestimos = Emprestimo.getEmprestimo(null);
        return emprestimos;
    }
    
    @GET
    @Path("/{pIdUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Emprestimo> getEmprestimosByUser(@PathParam("pIdUsuario") Long pIdUsuario) throws Exception {
        List<Emprestimo> emprestimos = Emprestimo.getEmprestimo(pIdUsuario);
        return emprestimos;
    }    

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response alugar(String json) throws Exception {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject jsonObject = reader.readObject();
        String idUsuario = jsonObject.getString("idUsuario");
        String isbn = jsonObject.getString("isbn");

        Livro livro = Livro.getLivro(isbn);
        boolean result = Emprestimo.insertEmprestimo(Long.parseLong(idUsuario), livro.getId(), LocalDate.now());

        if (result) {
            result = Livro.updateDisponibilidade(false, livro.getIsbn());
        }
        if (result) {
            return Response.ok().build();
        } else {
            JsonObject error = Json.createObjectBuilder()
                    .add("message", "Erro ao tentar locação")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(error)
                    .build();
        }
    }
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response devolver(String json) throws Exception {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject jsonObject = reader.readObject();
        String rowId = jsonObject.getString("rowId");
        String isbn = jsonObject.getString("isbn");

        Livro livro = Livro.getLivro(isbn);
        boolean result = Emprestimo.devolverEmprestimo(rowId, LocalDate.now());

        if (result) {
            result = Livro.updateDisponibilidade(true, livro.getIsbn());
        }
        if (result) {
            return Response.ok().build();
        } else {
            JsonObject error = Json.createObjectBuilder()
                    .add("message", "Erro ao tentar locação")
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(error)
                    .build();
        }
    }
}
