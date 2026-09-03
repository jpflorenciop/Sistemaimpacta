package impacta.model;

import java.time.LocalDateTime;

public class Mutirao extends Acao {

    private final int duracaoHoras;

    public Mutirao(int id,
                   String titulo,
                   String descricao,
                   LocalDateTime data,
                   int maxParticipantes,
                   int duracaoHoras) {

        super(id, titulo, descricao, data, maxParticipantes);
        this.duracaoHoras = duracaoHoras;
    }

    @Override
    public int calcularPontuacao() {
        return 4 * duracaoHoras;
    }

    @Override
    public String getTipo() {
        return "Mutirão de Reciclagem";
    }

    @Override
    public String getDetalhesEspecificos() {
        return "duracaoHoras=" + duracaoHoras;
    }

    public int getDuracaoHoras() {
        return duracaoHoras;
    }
}