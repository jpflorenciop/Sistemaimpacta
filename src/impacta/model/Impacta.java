package impacta.model;

import excecoes.EmailDuplicadoException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Impacta {

    /*
    guarda os voluntarios usando o email
    */
    private final Map<String, Voluntario> voluntarios;

    /*
    guarda as acoes usando o id
    */
    private final Map<Integer, Acao> acoes;

    /*
    controla o proximo id que sera dado a acao
    */
    private int proximoIdAcao;

    public Impacta() {
        voluntarios = new HashMap<>();
        acoes = new HashMap<>();
        proximoIdAcao = 1;
    }

    /*
    cadastra um novo voluntario
    */
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

    /*
    exibe as informacoes de um voluntario
    */
    public String exibirVoluntario(String email) {

        Voluntario voluntario = voluntarios.get(email);

        /*
        se o voluntario nao existir, retorna uma mensagem
        */
        if (voluntario == null) {
            return "Voluntário não encontrado.";
        }

        return "Nome: " + voluntario.getNome()
                + ", E-mail: " + voluntario.getEmail()
                + ", Matrícula: " + voluntario.getMatricula()
                + ", Ações Participadas: " + voluntario.getQuantidadeAcoes()
                + ", Pontuação: " + voluntario.getPontuacaoImpacto();
    }

    /*
    lista os voluntarios em ordem de pontuacao.
    Em caso de empate, ordena pelo nome.
    */
    public String[] listarVoluntarios() {

        List<Voluntario> lista =
                new ArrayList<>(voluntarios.values());

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

    /*
    cadastra um plantio
    */
    public int cadastrarPlantio(
            String titulo,
            String descricao,
            String data,
            int maxParticipantes,
            int qtdMudas) {

        int id = proximoIdAcao++;

        Plantio plantio = new Plantio(
                id,
                titulo,
                descricao,
                LocalDateTime.parse(data),
                maxParticipantes,
                qtdMudas
        );

        acoes.put(id, plantio);

        return id;
    }

    /*
    cadastra um mutirao
    */
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

    /*
    cadastra uma oficina
    */
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

    /*
    inscreve um voluntario em uma acao
    */
    public boolean inscreverVoluntario(
            String emailVoluntario,
            int idAcao) {

        /*
        procura o voluntario pelo email
        */
        Voluntario voluntario =
                voluntarios.get(emailVoluntario);

        /*
        procura a acao pelo id
        */
        Acao acao =
                acoes.get(idAcao);

        /*
        se o voluntario nao existir, lanca excecao
        */
        if (voluntario == null) {
            throw new IllegalArgumentException(
                    "Voluntário não encontrado."
            );
        }

        /*
        se a acao nao existir, lanca excecao
        */
        if (acao == null) {
            throw new IllegalArgumentException(
                    "Ação não encontrada."
            );
        }

        /*
        a propria Acao controla:
        - voluntario ja inscrito
        - acao lotada
        - adicao do voluntario
        - adicao da acao ao voluntario
        */
        acao.inscreverVoluntario(voluntario);

        return true;
    }

    /*
    exibe os detalhes de uma acao
    */
    public String exibirDetalhesAcao(int idAcao) {

        Acao acao = acoes.get(idAcao);

        /*
        busca a acao pelo id
        */
        if (acao == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("ID: ")
                .append(acao.getId())
                .append("\n");

        sb.append("Título: ")
                .append(acao.getTitulo())
                .append("\n");

        sb.append("Descrição: ")
                .append(acao.getDescricao())
                .append("\n");

        sb.append("Data: ")
                .append(acao.getData())
                .append("\n");

        sb.append("Pontuação: ")
                .append(acao.calcularPontuacao())
                .append("\n");

        sb.append("Capacidade: ")
                .append(acao.getVoluntarios().size())
                .append("/")
                .append(acao.getMaxParticipantes())
                .append("\n");

        sb.append(acao.getDetalhesEspecificos())
                .append("\n");

        sb.append("Voluntários inscritos:");

        if (acao.getVoluntarios().isEmpty()) {

            sb.append(" Nenhum");

        } else {

            for (Voluntario voluntario :
                    acao.getVoluntarios()) {

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

