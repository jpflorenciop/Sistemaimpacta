package impacta.model;

import java.time.LocalDateTime;

/*
herda da classe acao
 */
public class Oficina extends Acao {

    /*
    guarda a duracao da oficina em horas
    final:o valor nao nao pode ser alterado depois que a oficina for criada
    */
    private final int duracaoHoras;
    /*
    Indica se a oficina oferece material
     */
    private final boolean kitMaterial;

    /*
    recebe os dados gerais da acao e as informacoes especificas da oficina
     */
    public Oficina(int id,
                   String titulo,
                   String descricao,
                   LocalDateTime data,
                   int maxParticipantes,
                   int duracaoHoras,
                   boolean kitMaterial) {

        /*
        chama o contrutor da classe acao(pai)
         */
        super(id, titulo, descricao, data, maxParticipantes);

        /*
        armazena a duracao da oficina
         */
        this.duracaoHoras = duracaoHoras;
        /*
        armazena se a oficina possui ou nao material
         */
        this.kitMaterial = kitMaterial;
    }

    /*
    calcula a pontuacao de impacto da oficina
     */
    @Override
    public int calcularPontuacao() {
        return (3 * duracaoHoras) + (kitMaterial ? 10 : 0);
    }

    /*
    retorna o tipo da acao
    sobrescreve um metodo definido pela classe acao
     */
    @Override
    public String getTipo() {
        return "Oficina Ecológica";
    }
    /*
    retorna as informacoes especificas da oficina(duracao em horas e se possui material)
     */
    @Override
    public String getDetalhesEspecificos() {
        return "duracaoHoras=" + duracaoHoras
                + ";kitMaterial=" + kitMaterial;
    }

}