package impacta.model;

import java.time.LocalDateTime;

/*
herda da classe acao
 */
public class Plantio extends Acao {

    /*
    armazena a quantidade de mudas que vao ser plantadas
     */
    private final int qtdMudas;


    /*
    recebe os dados gerais e a quantidade de mudas
     */
    public Plantio(int id,
                   String titulo,
                   String descricao,
                   LocalDateTime data,
                   int maxParticipantes,
                   int qtdMudas) {


        /*
        chama o construtor da classe acao(pai)
         */
        super(id, titulo, descricao, data, maxParticipantes);

        /*
        armazena a quantidade de mudas recebidas pelo construtor
         */
        this.qtdMudas = qtdMudas;
    }

    /*
    calcula a pontuacao de impacto do plantio
     */
    @Override
    public int calcularPontuacao() {
        return 5 + (2 * qtdMudas);
    }

    /*
    retorna o tipo de acao

     */
    @Override
    public String getTipo() {
        return "Plantio de Mudas";
    }

    /*
    retorna detalhes especificos dessa acao
     */
    @Override
    public String getDetalhesEspecificos() {
        return "qtdMudas=" + qtdMudas;
    }

    /*
    retorna a quantidade de mudas do plantio
    permite que outras classes vejam essa informacao
     */
    public int getQtdMudas() {
        return qtdMudas;
    }
}