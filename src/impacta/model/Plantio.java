package impacta.model;

import java.time.LocalDateTime;

public class Plantio extends Acao {

    private final int qtdMudas;

    public Plantio(int id,
                   String titulo,
                   String descricao,
                   LocalDateTime data,
                   int maxParticipantes,
                   int qtdMudas) {

        super(id, titulo, descricao, data, maxParticipantes);
        this.qtdMudas = qtdMudas;
    }

    @Override
    public int calcularPontuacao() {
        return 5 + (2 * qtdMudas);
    }

    @Override
    public String getTipo() {
        return "Plantio de Mudas";
    }

    @Override
    public String getDetalhesEspecificos() {
        return "qtdMudas=" + qtdMudas;
    }

    public int getQtdMudas() {
        return qtdMudas;
    }
}