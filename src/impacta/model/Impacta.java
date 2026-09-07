package impacta.model;

import excecoes.AcaoLotadaException;
import excecoes.EmailDuplicadoException;
import excecoes.VoluntarioJaInscritoException;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Impacta {

    /*
    guarda os voluntatios usando o email
     */
    private final Map<String, Voluntario> voluntarios;

    /*
    guarda as acoes usando o id
     */
    private final Map<Integer, Acao> acoes;

    /*
    controla  o proximo id que sera dado a acao
     */
    private int proximoIdAcao;

    public Impacta() {
        voluntarios = new HashMap<>();
        acoes = new HashMap<>();
        proximoIdAcao = 1;
    }


    public boolean cadastrarVoluntario(
            String nome,
            String email,
            String matricula) {

        /*
        nao permite cadastrar dois voluntarios com o mesmo email
         */
        if (voluntarios.containsKey(email)) {
            throw new EmailDuplicadoException(
                    "Já existe um voluntário cadastrado com este e-mail.");
        }

        Voluntario voluntario =
                new Voluntario(nome, email, matricula);

        voluntarios.put(email, voluntario);

        return true;
    }

    public String exibirVoluntario(String email) {
        Voluntario voluntario = voluntarios.get(email);

        /*
        se o email nao estiver cadastrado retorna null
         */
        if (voluntario == null) {
            return null;
        }

        return "Nome: " + voluntario.getNome()
                + ", E-mail: " + voluntario.getEmail()
                + ", Matrícula: " + voluntario.getMatricula()
                + ", Ações: " + voluntario.getQuantidadeAcoes()
                + ", Pontuação: " + voluntario.getPontuacaoImpacto();
    }

    public String[] listarVoluntarios() {

        /*
        retorna os valores do hashmap em lista pra poder ordenar
         */
        List<Voluntario> lista = new ArrayList<>(voluntarios.values());

        /*
        ordema pela pontuacao e se tiver empate pelo nome
         */
        lista.sort(
                Comparator
                        .comparingInt(Voluntario::getPontuacaoImpacto)
                        .reversed()
                        .thenComparing(
                                Voluntario::getNome,
                                String.CASE_INSENSITIVE_ORDER
                        )
        );

        String[] resultado = new String[lista.size()];

        for (int i = 0; i < lista.size(); i++) {
            Voluntario voluntario = lista.get(i);

            resultado[i] =
                    voluntario.getNome()
                            + " - "
                            + voluntario.getPontuacaoImpacto()
                            + " pontos";
        }

        return resultado;
    }


    public int cadastrarPlantio(
            String titulo,
            String descricao,
            String data,
            int maxParticipantes,
            int qtdMudas) {

        /*
        pega o id atual e incrementa para proxima acao
         */
        int id = proximoIdAcao++;

        Plantio plantio = new Plantio(
                id,
                titulo,
                descricao,
                LocalDateTime.parse(data),
                maxParticipantes,
                qtdMudas
        );

        /*
        salva o plantio do hashmap usando o id
         */
        acoes.put(id, plantio);

        return id;
    }

    public int cadastrarMutirao(
            String titulo,
            String descricao,
            String data,
            int maxParticipantes,
            int duracaoHoras) {

        int id = proximoIdAcao++;

        Mutirao mutirao = new Mutirao(
                id,
                titulo,
                descricao,
                LocalDateTime.parse(data),
                maxParticipantes,
                duracaoHoras
        );

        acoes.put(id, mutirao);

        return id;
    }

    public int cadastrarOficina(
            String titulo,
            String descricao,
            String data,
            int maxParticipantes,
            int duracaoHoras,
            boolean kitMaterial) {

        int id = proximoIdAcao++;

        Oficina oficina = new Oficina(
                id,
                titulo,
                descricao,
                LocalDateTime.parse(data),
                maxParticipantes,
                duracaoHoras,
                kitMaterial
        );

        acoes.put(id, oficina);

        return id;
    }

    public boolean inscreverVoluntario(
            String emailVoluntario,
            int idAcao) {

        /*
        procura o voluntario pelo email e pela acao do id
         */
        Voluntario voluntario = voluntarios.get(emailVoluntario);
        Acao acao = acoes.get(idAcao);

        /*
        se algum deles nao existir nao faz a inscricao
         */
        if (voluntario == null || acao == null) {
            return false;
        }

        /*
        impede que o mesmo voluntario seja inscrito duas vezes
         */
        if (acao.possuiVoluntario(voluntario)) {
            throw new VoluntarioJaInscritoException(
                    "O voluntário já está inscrito nesta ação.");
        }

        /*
        impede a inscricao quando a acao atingir o limite
         */
        if (acao.estaLotada()) {
            throw new AcaoLotadaException(
                    "A ação já atingiu sua capacidade máxima.");
        }

        /*
        adiciona a acao em voluntario e vice versa
         */
        acao.adicionarVoluntario(voluntario);
        voluntario.adicionarAcao(acao);

        return true;
    }

    public String exibirDetalhesAcao(int idAcao) {
        Acao acao = acoes.get(idAcao);

        /*
        busca a acao pelo id
         */
        if (acao == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        /*
        monta o texto com as informacoes da acao
        sb.append:adiciona dados no final de uma string
         */
        sb.append("ID: ").append(acao.getId()).append("\n");
        sb.append("Título: ").append(acao.getTitulo()).append("\n");
        sb.append("Descrição: ").append(acao.getDescricao()).append("\n");
        sb.append("Data: ").append(acao.getData()).append("\n");
        sb.append("Pontuação: ").append(acao.calcularPontuacao()).append("\n");
        sb.append("Capacidade: ")
                .append(acao.getVoluntarios().size())
                .append("/")
                .append(acao.getMaxParticipantes())
                .append("\n");
        /*
        pra cada acao ter informacoes especificas
         */
        sb.append(acao.getDetalhesEspecificos()).append("\n");

        sb.append("Voluntários inscritos:");

        if (acao.getVoluntarios().isEmpty()) {
            sb.append(" Nenhum");
        } else {

            /*
            percorre todos os voluntarios inscritos na acao
             */
            for (Voluntario voluntario : acao.getVoluntarios()) {
                sb.append("\n- ")
                        .append(voluntario.getNome())
                        .append(" (")
                        .append(voluntario.getEmail())
                        .append(")");
            }
        }

        return sb.toString();
    }
}