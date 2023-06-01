package web;
public class UsuarioNaoAutenticadoException extends Exception {
    public UsuarioNaoAutenticadoException() {
        super("Usuário não autenticado.");
    }
    
    public UsuarioNaoAutenticadoException(String mensagem) {
        super(mensagem);
    }
}