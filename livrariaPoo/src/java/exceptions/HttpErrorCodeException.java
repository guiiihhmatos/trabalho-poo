package exceptions;
public class HttpErrorCodeException extends Exception {
    public HttpErrorCodeException() {
    }
    
    public HttpErrorCodeException(String mensagem) {
        super(mensagem);
    }

    public HttpErrorCodeException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}