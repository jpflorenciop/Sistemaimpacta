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

    public Acao(
            int id,
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

    /*
    Inscreve um voluntário na ação.
    */
    public void inscreverVoluntario(Voluntario voluntario) {

        /*
        Primeiro verifica se o voluntário já está inscrito.
        */
        if (possuiVoluntario(voluntario)) {
            throw new VoluntarioJaInscritoException(
                    "Voluntário já inscrito nesta ação."
            );
        }

        /*
        Depois verifica se a ação está lotada.
        */
        if (estaLotada()) {
            throw new AcaoLotadaException(
                    "A ação já atingiu sua capacidade máxima."
            );
        }

        /*
        Adiciona o voluntário na ação.
        */
        voluntarios.add(voluntario);

        /*
        Adiciona esta ação ao voluntário.
        */
        voluntario.adicionarAcao(this);
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

    /*
    Verifica se o voluntário já está inscrito.
    */
    public boolean possuiVoluntario(Voluntario voluntario) {
        return voluntarios.contains(voluntario);
    }

    /*
    Verifica se a ação atingiu o número máximo de participantes.
    */
    public boolean estaLotada() {
        return voluntarios.size() >= maxParticipantes;
    }

    /*
    Adiciona um voluntário diretamente.
    */
    public void adicionarVoluntario(Voluntario voluntario) {
        if (!possuiVoluntario(voluntario) && !estaLotada()) {
            voluntarios.add(voluntario);
        }
    }
}

