package impacta.model;

import excecoes.AcaoLotadaException;
import excecoes.VoluntarioJaInscritoException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
representa uma acao do preojeto
nao pode ser criada diretamente por ser abistrata(serve pra outras classes herdarem dela)
 */
public abstract class Acao {

    private final int id;
    private final String titulo;
    private final String descricao;
    private final LocalDateTime data;
    private final int maxParticipantes;

    /*
    armazena os voluntarios inscritos na acao
     */
    private final List<Voluntario> voluntarios;

    public Acao(int id,
                String titulo,
                String descricao,
                LocalDateTime data,
                int maxParticipantes) {
        /*
        guarda informacao recebida nos atributos correspondente a classe
         */
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.maxParticipantes = maxParticipantes;

        /*
        cria uma lista vazia pra armazenar os voluntarios
         */
        this.voluntarios = new ArrayList<>();
    }

    public abstract int calcularPontuacao();

    public abstract String getTipo();

    public abstract String getDetalhesEspecificos();

    /*
    inscreve um voluntario
     */
    public void inscreverVoluntario(Voluntario voluntario) {

        /*
        verifica se o voluntario ja esta escrito
         */
        if (voluntarios.contains(voluntario)) {

            /*
            se ja tiver inscrito lanca a excecao
             */
            throw new VoluntarioJaInscritoException(
                    "Voluntário já inscrito nesta ação."
            );
        }

        /*
        verifica se a quantidade de voluntarios ja chegou no maximo
         */
        if (voluntarios.size() >= maxParticipantes) {

            /*
            se tiver cheia lanca a excecao
             */
            throw new AcaoLotadaException(
                    "A ação já atingiu sua capacidade máxima."
            );
        }

        /*
        se o voluntario nao estiver inscrito e tiver espaco ele e adicionado na lista
         */
        voluntarios.add(voluntario);
    }

    /*
    retorna o id da acao
     */
    public int getId() {
        return id;
    }

    /*
    retorna o titulo da acao
     */
    public String getTitulo() {
        return titulo;
    }

    /*
    retorna a descricao da acao
     */
    public String getDescricao() {
        return descricao;
    }

    /*
    retorna a data e horario da acao
     */
    public LocalDateTime getData() {
        return data;
    }

    /*
    retorna o numero maximo de participantes
     */
    public int getMaxParticipantes() {
        return maxParticipantes;
    }

    /*
    retorna a lista de voluntarios ja inscritos
    Collections.unmodifiableList:somente para leitura da lista
     */
    public List<Voluntario> getVoluntarios() {
        return Collections.unmodifiableList(voluntarios);
    }

    /*
    verifica se determinado voluntario esta inscrito nessa acao
     */
    public boolean possuiVoluntario(Voluntario voluntario) {
    return true;
    }

    /*
    verifica se a acao esta cheia
     */
    public boolean estaLotada() {
        return true;
    }

    /*
    adiciona um voluntario diretamente a lista
     */
    public void adicionarVoluntario(Voluntario voluntario) {
    }

}