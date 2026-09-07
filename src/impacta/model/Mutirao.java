package impacta.model;

import java.time.LocalDateTime;

/*
classe mutirao herda da classe acao
 */
public class Mutirao extends Acao {

    private final int duracaoHoras;

    /*
    recebe todas as informacaoes que precisa pra criar mutirao
     */
    public Mutirao(int id,
                   String titulo,
                   String descricao,
                   LocalDateTime data,
                   int maxParticipantes,
                   int duracaoHoras) {

        super(id, titulo, descricao, data, maxParticipantes);

        /*
        guarda a duracao do mutirao no atributo da classe
         */
        this.duracaoHoras = duracaoHoras;
    }

    /*
    calcula a pontuacao de impacto do mutirao
     */
    @Override
    public int calcularPontuacao() {
        return 4 * duracaoHoras;
    }

    /*
    retorna o tipo da acao
     */
    @Override
    public String getTipo() {
        return "Mutirão de Reciclagem";
    }

    /*
    retorna informacoes especificas do mutirao
    retorna a duracao em texto
     */
    @Override
    public String getDetalhesEspecificos() {
        return "duracaoHoras=" + duracaoHoras;
    }

    /*
    retorna a duracao do mutirao
    permite que outras classes vejam quantas horas o mutirao tem
     */
    public int getDuracaoHoras() {
        return duracaoHoras;
    }
}