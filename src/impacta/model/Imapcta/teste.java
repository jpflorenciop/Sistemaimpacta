package impacta.model.Imapcta;

import excecoes.AcaoLotadaException;
import excecoes.EmailDuplicadoException;
import impacta.model.Impacta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;


class ImpactaTest {

    private Impacta sistema;

    @BeforeEach
    public void setUp() {
        sistema = new Impacta();
    }

    @Test
    public void testCalculoPontuacaoPolimorfico() {
        // Plantio: 5 + (2 * 10 mudas) = 25 pontos
        int idPlantio = sistema.cadastrarPlantio("Plantio", "Descrição", "2026-10-10T10:00:00", 5, 10);

        // Mutirão: 4 * 3 horas = 12 pontos
        int idMutirao = sistema.cadastrarMutirao("Mutirão", "Descrição", "2026-10-11T10:00:00", 5, 3);

        // Oficina com kit: (3 * 2 horas) + 10 bônus = 16 pontos
        int idOficinaComKit = sistema.cadastrarOficina("Oficina Kit", "Descrição", "2026-10-12T10:00:00", 5, 2, true);

        // Oficina sem kit: (3 * 2 horas) = 6 pontos
        int idOficinaSemKit = sistema.cadastrarOficina("Oficina Sem Kit", "Descrição", "2026-10-13T10:00:00", 5, 2, false);

        sistema.cadastrarVoluntario("João", "joao@email.com", "111");

        sistema.inscreverVoluntario("joao@email.com", idPlantio);
        sistema.inscreverVoluntario("joao@email.com", idMutirao);
        sistema.inscreverVoluntario("joao@email.com", idOficinaComKit);
        sistema.inscreverVoluntario("joao@email.com", idOficinaSemKit);

        // Total retornado deve bater exatamente com a soma: 25 + 12 + 16 + 6 = 59 pontos
        String detalhesVoluntario = sistema.exibirVoluntario("joao@email.com");
        assertTrue(detalhesVoluntario.contains("Pontuação: 59"));
    }

    @Test
    public void testOrdenacaoRankingVoluntarios() {
        sistema.cadastrarVoluntario("Ana", "ana@email.com", "001");
        sistema.cadastrarVoluntario("Bruno", "bruno@email.com", "002");
        sistema.cadastrarVoluntario("Carlos", "carlos@email.com", "003");

        int idPlantio = sistema.cadastrarPlantio("Plantio A", "Desc", "2026-10-10T10:00:00", 10, 20); // 45 pts
        int idMutirao = sistema.cadastrarMutirao("Mutirão B", "Desc", "2026-10-11T10:00:00", 10, 5);  // 20 pts

        // Carlos: 45 pts
        sistema.inscreverVoluntario("carlos@email.com", idPlantio);

        // Bruno: 20 pts
        sistema.inscreverVoluntario("bruno@email.com", idMutirao);

        // Ana: 0 pts

        String[] ranking = sistema.listarVoluntarios();

        // Carlos em 1º (45 pts), Bruno em 2º (20 pts), Ana em 3º (0 pts)
        assertTrue(ranking[0].contains("Carlos"));
        assertTrue(ranking[1].contains("Bruno"));
        assertTrue(ranking[2].contains("Ana"));
    }

    @Test
    public void testDesempatePorNomeRanking() {
        sistema.cadastrarVoluntario("Zuleica", "zuleica@email.com", "001");
        sistema.cadastrarVoluntario("Alice", "alice@email.com", "002");

        int idMutirao = sistema.cadastrarMutirao("Mutirão", "Desc", "2026-10-11T10:00:00", 10, 5); // 20 pts

        sistema.inscreverVoluntario("zuleica@email.com", idMutirao);
        sistema.inscreverVoluntario("alice@email.com", idMutirao);

        String[] ranking = sistema.listarVoluntarios();

        // Ambas possuem 20 pts, então Alice vem em 1º por ordem alfabética
        assertTrue(ranking[0].contains("Alice"));
        assertTrue(ranking[1].contains("Zuleica"));
    }

    @Test
    public void testExcecoesInscricao() {
        sistema.cadastrarVoluntario("Maria", "maria@email.com", "123");
        int idAcao = sistema.cadastrarPlantio("Acao Pequeña", "Desc", "2026-10-10T10:00:00", 1, 5);

        sistema.inscreverVoluntario("maria@email.com", idAcao);



        // Tentar inscrever um segundo aluno em uma ação de capacidade 1
        sistema.cadastrarVoluntario("Pedro", "pedro@email.com", "124");
        assertThrows(AcaoLotadaException.class, () -> {
            sistema.inscreverVoluntario("pedro@email.com", idAcao);
        });
    }

    @Test
    public void testExcecaoEmailDuplicado() {
        sistema.cadastrarVoluntario("Lucas", "lucas@email.com", "111");

        assertThrows(EmailDuplicadoException.class, () -> {
            sistema.cadastrarVoluntario("Outro Lucas", "lucas@email.com", "222");
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar e-mail duplicado (mesmo com nomes e matrículas diferentes)")
    public void testEmailDuplicadoLancaExcecao() {
        sistema.cadastrarVoluntario("João Silva", "joao@impacta.org", "1001");

        assertThrows(EmailDuplicadoException.class, () -> {
            sistema.cadastrarVoluntario("João Pedro", "joao@impacta.org", "1002");
        }, "Tentativa de cadastrar e-mail duplicado deve lançar EmailDuplicadoException.");
    }

    @Test
    @DisplayName("Exibição de voluntário inexistente deve retornar mensagem amigável")
    public void testExibirVoluntarioInexistente() {
        String resultado = sistema.exibirVoluntario("fantasma@impacta.org");
        assertNotNull(resultado);
        assertTrue(resultado.toLowerCase().contains("não encontrado"),
                "Deve informar que o voluntário não foi encontrado.");
    }

    // ==========================================
    // 2. CÁLCULO POLIMÓRFICO DE PONTUAÇÃO
    // ==========================================

    @Test
    @DisplayName("Cálculo de pontuação - Plantio de Mudas (5 base + 2 por muda)")
    public void testCalculoPlantioMudas() {
        // 0 mudas = 5 pontos
        int id1 = sistema.cadastrarPlantio("Plantio Zero", "Desc", "2026-10-10T10:00:00", 10, 0);
        // 15 mudas = 5 + (2 * 15) = 35 pontos
        int id2 = sistema.cadastrarPlantio("Plantio Grande", "Desc", "2026-10-10T10:00:00", 10, 15);

        sistema.cadastrarVoluntario("Ana", "ana@impacta.org", "101");

        sistema.inscreverVoluntario("ana@impacta.org", id1);
        assertTrue(sistema.exibirVoluntario("ana@impacta.org").contains("Pontuação: 5"));

        sistema.inscreverVoluntario("ana@impacta.org", id2);
        assertTrue(sistema.exibirVoluntario("ana@impacta.org").contains("Pontuação: 40")); // 5 + 35
    }

    @Test
    @DisplayName("Cálculo de pontuação - Mutirão de Reciclagem (4 por hora)")
    public void testCalculoMutiraoReciclagem() {
        // 5 horas = 20 pontos
        int id = sistema.cadastrarMutirao("Recicla", "Desc", "2026-10-10T10:00:00", 5, 5);

        sistema.cadastrarVoluntario("Beto", "beto@impacta.org", "102");
        sistema.inscreverVoluntario("beto@impacta.org", id);

        assertTrue(sistema.exibirVoluntario("beto@impacta.org").contains("Pontuação: 20"));
    }

    @Test
    @DisplayName("Cálculo de pontuação - Oficina Ecológica (3/h + 10 bônus kit)")
    public void testCalculoOficinaEcologica() {
        // 4h sem kit = 12 pontos
        int idSemKit = sistema.cadastrarOficina("Oficina A", "Desc", "2026-10-10T10:00:00", 5, 4, false);
        // 4h com kit = 12 + 10 = 22 pontos
        int idComKit = sistema.cadastrarOficina("Oficina B", "Desc", "2026-10-10T10:00:00", 5, 4, true);

        sistema.cadastrarVoluntario("Caio", "caio@impacta.org", "103");

        sistema.inscreverVoluntario("caio@impacta.org", idSemKit);
        assertTrue(sistema.exibirVoluntario("caio@impacta.org").contains("Pontuação: 12"));

        sistema.inscreverVoluntario("caio@impacta.org", idComKit);
        assertTrue(sistema.exibirVoluntario("caio@impacta.org").contains("Pontuação: 34")); // 12 + 22
    }

    // ==========================================
    // 3. REGRAS E EXCEÇÕES DE INSCRIÇÃO
    // ==========================================

//    @Test
//    @DisplayName("Deve lançar exceção ao tentar inscrever voluntário duas vezes na mesma ação")
//    public void testDuplaInscricaoLancaExcecao() {
//        int idAcao = sistema.cadastrarMutirao("Mutirão Bairro", "Desc", "2026-10-10T09:00:00", 10, 2);
//        sistema.cadastrarVoluntario("Daniela", "dani@impacta.org", "104");
//
//        sistema.inscreverVoluntario("dani@impacta.org", idAcao);
//
//        assertThrows(DuplaInscricaoException.class, () -> {
//            sistema.inscreverVoluntario("dani@impacta.org", idAcao);
//        }, "Inscrever o mesmo e-mail duas vezes na mesma ação deve lançar DuplaInscricaoException.");
//    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar inscrever voluntário em ação com capacidade esgotada")
    public void testAcaoLotadaLancaExcecao() {
        // Vagas max = 2
        int idAcao = sistema.cadastrarMutirao("Mutirão Fechado", "Desc", "2026-10-10T09:00:00", 2, 2);

        sistema.cadastrarVoluntario("V1", "v1@impacta.org", "1");
        sistema.cadastrarVoluntario("V2", "v2@impacta.org", "2");
        sistema.cadastrarVoluntario("V3", "v3@impacta.org", "3");

        sistema.inscreverVoluntario("v1@impacta.org", idAcao);
        sistema.inscreverVoluntario("v2@impacta.org", idAcao);

        assertThrows(AcaoLotadaException.class, () -> {
            sistema.inscreverVoluntario("v3@impacta.org", idAcao);
        }, "Inscrição além do limite máximo deve lançar AcaoLotadaException.");
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para e-mail ou idAcao inexistentes na inscrição")
    public void testInscricaoComDadosInexistentes() {
        int idAcao = sistema.cadastrarMutirao("Mutirão", "Desc", "2026-10-10T09:00:00", 5, 2);
        sistema.cadastrarVoluntario("Eduardo", "edu@impacta.org", "105");

        // E-mail inexistente
        assertThrows(IllegalArgumentException.class, () -> {
            sistema.inscreverVoluntario("inexistente@impacta.org", idAcao);
        });

        // Ação inexistente
        assertThrows(IllegalArgumentException.class, () -> {
            sistema.inscreverVoluntario("edu@impacta.org", 9999);
        });
    }

    // ==========================================
    // 4. RANKING E ORDENAÇÃO
    // ==========================================

    @Test
    @DisplayName("Ranking deve considerar ordenação decrescente de pontuação e desempate alfabético (case-insensitive)")
    public void testRankingPontuacaoEDesempate() {
        sistema.cadastrarVoluntario("beatriz", "bea@impacta.org", "1"); // Pontos: 10 (desempate B)
        sistema.cadastrarVoluntario("Amanda", "amanda@impacta.org", "2"); // Pontos: 10 (desempate A)
        sistema.cadastrarVoluntario("Carlos", "carlos@impacta.org", "3"); // Pontos: 50
        sistema.cadastrarVoluntario("Daniel", "daniel@impacta.org", "4"); // Pontos: 0

        int acao50 = sistema.cadastrarPlantio("Plantio", "Desc", "2026-10-10T10:00:00", 10, 22); // 5 + 44 = 49 + 5 = 50 pts (aprox) -> 5 + 2*22.5
        int acao10 = sistema.cadastrarOficina("Oficina", "Desc", "2026-10-10T10:00:00", 10, 0, true); // 0 + 10 = 10 pts

        // Inscrevendo para gerar as pontuações
        sistema.inscreverVoluntario("carlos@impacta.org", acao50); // 49 pontos
        sistema.inscreverVoluntario("bea@impacta.org", acao10);     // 10 pontos
        sistema.inscreverVoluntario("amanda@impacta.org", acao10);  // 10 pontos

        String[] ranking = sistema.listarVoluntarios();

        assertEquals(4, ranking.length, "O ranking deve listar todos os 4 voluntários.");
        assertTrue(ranking[0].contains("Carlos"), "1º lugar deve ser Carlos (maior pontuação).");
        assertTrue(ranking[1].contains("Amanda"), "2º lugar deve ser Amanda (empate em 10 pontos, A vem antes de B).");
        assertTrue(ranking[2].contains("beatriz"), "3º lugar deve ser beatriz (empate em 10 pontos).");
        assertTrue(ranking[3].contains("Daniel"), "4º lugar deve ser Daniel (0 pontos).");
    }

    @Test
    @DisplayName("Incremento correto na quantidade de ações e pontuação acumulada do voluntário")
    public void testAcumuloDeAcoesEPontos() {
        sistema.cadastrarVoluntario("Fernanda", "nanda@impacta.org", "106");

        int acao1 = sistema.cadastrarMutirao("M1", "Desc", "2026-10-10T10:00:00", 5, 2); // 8 pts
        int acao2 = sistema.cadastrarMutirao("M2", "Desc", "2026-10-10T10:00:00", 5, 3); // 12 pts

        sistema.inscreverVoluntario("nanda@impacta.org", acao1);
        sistema.inscreverVoluntario("nanda@impacta.org", acao2);

        String detalhes = sistema.exibirVoluntario("nanda@impacta.org");
        assertTrue(detalhes.contains("Ações Participadas: 2"), "Deve registrar 2 ações.");
        assertTrue(detalhes.contains("Pontuação: 20"), "Deve acumular 20 pontos.");
    }

    // ==========================================
    // 5. TRATAMENTO DE FORMATO DE DATA
    // ==========================================

    @Test
    @DisplayName("Deve lançar exceção de parse de data quando a String de data for inválida")
    public void testDataInvalidaLancaExcecao() {
        assertThrows(DateTimeParseException.class, () -> {
            sistema.cadastrarMutirao("Mutirão", "Desc", "10/10/2026 10:00", 5, 2);
        }, "Formatos fora do padrão ISO-8601 (yyyy-MM-ddTHH:mm:ss) devem falhar.");
    }
}