package web.contoller;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.util.List;
import model.Livro;

@Path("/livros")
public class LivroController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Livro> getAllLivros() throws Exception {
        List<Livro> livros = Livro.getAllLivros();
        return livros;
    }

    @GET
    @Path("/disponiveis")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Livro> getAllLivrosDispniveis() throws Exception {
        List<Livro> livros = Livro.getLivros();
        return livros;
    }
    
    @GET
    @Path("/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Livro getLivroByIsbn(@PathParam("isbn") String isbn) throws Exception {
        Livro livro = Livro.getLivro(isbn);
        return livro;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createLivro(Livro pLivro) throws Exception {
        boolean criado = Livro.insertLivro(pLivro.getTitulo(), pLivro.getAutor(),
                    pLivro.getEditora(), pLivro.getAno_publicacao(), 
                    pLivro.getIsbn(), pLivro.getDescricao(),pLivro.isDisponibilidade());
        
        if (criado) {
            // Construir a URL para o novo recurso criado (livro)            
            UriBuilder uriBuilder = UriBuilder.fromResource(LivroController.class)
                                              .path(pLivro.getIsbn());
            String livroUrl = uriBuilder.build().toString();
            
            // Retornar uma resposta com código 201 (Created) e o URL do novo livro
            return Response.created(uriBuilder.build())
                           .entity("Livro criado com sucesso: " + livroUrl)
                           .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Falha ao criar o livro")
                           .build();
        }        
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateLivro(Livro livro) throws Exception {
        // Lógica para atualizar o livro no DAO
        // Exemplo de atualização do livro:
        boolean atualizado = Livro.updateLivro(
            livro.getTitulo(),
            livro.getAutor(),
            livro.getEditora(),
            livro.getAno_publicacao(),
            livro.getIsbn(),
            livro.getDescricao(),
            livro.isDisponibilidade()
        );
        if (atualizado) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Falha ao atualizar o livro")
                           .build();
        }
    }     

    @DELETE
    @Path("/{rowId}")
    public Response deleteLivro(@PathParam("rowId") Long rowId) throws Exception {
        boolean excluido = Livro.deleteLivro(rowId);        
        if (excluido) {
            return Response.ok().entity("Livro excluído com sucesso").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Falha ao excluir o livro")
                           .build();
        }
    }
}

