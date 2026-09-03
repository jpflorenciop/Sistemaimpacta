package impacta.model;

import java.time.LocalDateTime;

public class Oficina extends Acao {

    private final int duracaoHoras;
    private final boolean kitMaterial;

    public Oficina(int id,
                   String titulo,
                   String descricao,
                   LocalDateTime data,
                   int maxParticipantes,
                   int duracaoHoras,
                   boolean kitMaterial) {

        super(id, titulo, descricao, data, maxParticipantes);
        this.duracaoHoras = duracaoHoras;
        this.kitMaterial = kitMaterial;
    }

    @Override
    public int calcularPontuacao() {
        return (3 * duracaoHoras) + (kitMaterial ? 10 : 0);
    }

    @Override
    public String getTipo() {
        return "Oficina Ecológica";
    }

    @Override
    public String getDetalhesEspecificos() {
        return "duracaoHoras=" + duracaoHoras
                + ";kitMaterial=" + kitMaterial;
    }

    public int getDuracaoHoras() {
        return duracaoHoras;
    }

    public boolean isKitMaterial() {
        return kitMaterial;
    }
}