package excecoes;

public class VoluntarioJaInscritoException extends RuntimeException {

    public VoluntarioJaInscritoException(String mensagem) {
        super(mensagem);
    }
}