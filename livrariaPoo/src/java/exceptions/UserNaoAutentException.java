package exceptions;
public class UserNaoAutentException extends Exception {
    public UserNaoAutentException() {
        super("Usuário não autenticado.");
    }
    
    public UserNaoAutentException(String mensagem) {
        super(mensagem);
    }
}