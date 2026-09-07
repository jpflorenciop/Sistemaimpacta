package impacta.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Voluntario {

    private final String nome;
    private final String email;
    private final String matricula;

    private final List<Acao> acoes;

    /*
    construtor
    recebe dados da parte de voluntarios e cria uma lista vazia pra armazenar acoes
     */
    public Voluntario(String nome, String email, String matricula) {
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
    /*
    inicializa as acoes
     */
        this.acoes = new ArrayList<>();
    }

    /*
    adiciona uma nova ação a lista de acoes do voluntario.
     */
    public void adicionarAcao(Acao acao) {
        acoes.add(acao);
    }

    /*
    retorna o nome do voluntario
     */
    public String getNome() {
        return nome;
    }

    /*
    retorna o email do voluntario
     */
    public String getEmail() {
        return email;
    }

    /*
    retorna a matricola do voluntario
     */
    public String getMatricula() {
        return matricula;
    }

    /*
    retorna a quantidade de acoes do voluntario
    size():quantida de coisas na lista
     */
    public int getQuantidadeAcoes() {
        return acoes.size();
    }

    /*
    calcula a pontuacao do voluntario
    pra cada acao chama o metodo calcular pontuacao
    sum():calcula o total da soma
     */
    public int getPontuacaoImpacto() {
        return acoes.stream()
                .mapToInt(Acao::calcularPontuacao)
                .sum();
    }

    /*
    retorna a lista de acoes dos voluntarios
    Collections.unmodifiableList(acoes):impede que outra parte altere diretamente a lista original
     */
    public List<Acao> getAcoes() {
        return Collections.unmodifiableList(acoes);
    }

    /*
    compara objetos pra verificar se sao iguais quanto tem o mesmo email
     */
    @Override
    public boolean equals(Object obj) {

        /*
        se for o mesmo objeto retorna que sao iguais
         */
        if (this == obj) {
            return true;
        }
        /*
        verifica se o objeto e realmente voluntario
        se nao for nao podem ser considerados iguais(compelmtenta a parte de cima desse)
         */
        if (!(obj instanceof Voluntario)) {
            return false;
        }

        Voluntario outro = (Voluntario) obj;
        /*
        compara os email dos voluntarios
        se for o mesmo sao considerados igauis
         */
        return email.equals(outro.email);
    }
    /*
    retorna o codigo baseado no email
     */
    @Override
    public int hashCode() {
        return email.hashCode();
    }
    /*
    define como o objeto vai ser representado
     */
    @Override
    public String toString() {
        return nome;
    }
}