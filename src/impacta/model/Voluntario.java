package impacta.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Voluntario {

    private final String nome;
    private final String email;
    private final String matricula;

    private final List<Acao> acoes;

    public Voluntario(String nome, String email, String matricula) {
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
        this.acoes = new ArrayList<>();
    }

    public void adicionarAcao(Acao acao) {
        acoes.add(acao);
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getMatricula() {
        return matricula;
    }

    public int getQuantidadeAcoes() {
        return acoes.size();
    }

    public int getPontuacaoImpacto() {
        return acoes.stream()
                .mapToInt(Acao::calcularPontuacao)
                .sum();
    }

    public List<Acao> getAcoes() {
        return Collections.unmodifiableList(acoes);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Voluntario)) {
            return false;
        }

        Voluntario outro = (Voluntario) obj;

        return email.equals(outro.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return nome;
    }
}