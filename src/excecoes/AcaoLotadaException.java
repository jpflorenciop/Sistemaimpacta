package excecoes;

public class AcaoLotadaException extends RuntimeException {

    public AcaoLotadaException(String mensagem) {
        super(mensagem);
    }
}