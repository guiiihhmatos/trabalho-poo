package web.contoller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import model.Emprestimo;

@Path("/emprestimo")
public class EmprestimoController {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Emprestimo> getAllEmprestimos() throws Exception {
        List<Emprestimo> emprestimos = Emprestimo.getAllEmprestimo();
        return emprestimos;
    }
}
