package impacta.model;



import excecoes.AcaoLotadaException;
import excecoes.VoluntarioJaInscritoException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Acao {

    private final int id;
    private final String titulo;
    private final String descricao;
    private final LocalDateTime data;
    private final int maxParticipantes;

    private final List<Voluntario> voluntarios;

    public Acao(int id,
                String titulo,
                String descricao,
                LocalDateTime data,
                int maxParticipantes) {

        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.maxParticipantes = maxParticipantes;
        this.voluntarios = new ArrayList<>();
    }

    public abstract int calcularPontuacao();

    public abstract String getTipo();

    public abstract String getDetalhesEspecificos();

    public void inscreverVoluntario(Voluntario voluntario) {

        if (voluntarios.contains(voluntario)) {
            throw new VoluntarioJaInscritoException(
                    "Voluntário já inscrito nesta ação."
            );
        }

        if (voluntarios.size() >= maxParticipantes) {
            throw new AcaoLotadaException(
                    "A ação já atingiu sua capacidade máxima."
            );
        }

        voluntarios.add(voluntario);
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getData() {
        return data;
    }

    public int getMaxParticipantes() {
        return maxParticipantes;
    }

    public List<Voluntario> getVoluntarios() {
        return Collections.unmodifiableList(voluntarios);
    }

    public boolean possuiVoluntario(Voluntario voluntario) {
    return true;
    }

    public boolean estaLotada() {
        return true;
    }

    public void adicionarVoluntario(Voluntario voluntario) {
    }

}