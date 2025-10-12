import java.util.Random;

public class Main {

    private final static int CEM = 100;
    private final static int UM_MIL = 1000;
    private final static int DEZ_MIL = 10000;
    private final static int CEM_MIL = 100000;
    private final static int UM_MILHAO = 1000000;
    private final static int DEZ_MILHOES = 10000000;

    public static void main(String[] args) {
        Random rand = new Random(2025);

        // Funções Hash
        Hash[] modulosHash = {new HashRehashing(), new HashEncadeamento(), new HashDuplo()};

        // Tamanhos das tabelas hash e dos conjuntos de dados
        int[] tamanhosTabelasHash = {UM_MIL, DEZ_MIL, CEM_MIL};
        int[] tamanhosConjuntosDados = {UM_MIL, DEZ_MIL, CEM_MIL};

        // Arrays com os dados a serem utilizados
        int[] conjuntoDeDados1 = new int[tamanhosConjuntosDados[0]];
        int[] conjuntoDeDados2 = new int[tamanhosConjuntosDados[1]];
        int[] conjuntoDeDados3 = new int[tamanhosConjuntosDados[2]];
        int[][] conjuntosDeDados = {conjuntoDeDados1, conjuntoDeDados2, conjuntoDeDados3};

        // Popula cada array de dados
        for (int i = 0; i < tamanhosConjuntosDados[0]; i++)
            conjuntoDeDados1[i] = rand.nextInt(1, 2_147_483_647);

        for (int i = 0; i < tamanhosConjuntosDados[1]; i++)
            conjuntoDeDados2[i] = rand.nextInt(1, 2_147_483_647);

        for (int i = 0; i < tamanhosConjuntosDados[2]; i++)
            conjuntoDeDados3[i] = rand.nextInt(1, 2_147_483_647);

        // Para os 3 tamanhos de tabela hash
        for (int i = 0; i < 3; i++) {
            int tamanhoTabelaHash = tamanhosTabelasHash[i];
            Registro[] tabelaHash = new Registro[tamanhoTabelaHash];
            String tempoPorConjuntoCSV = "funcaoHash,tamanhoConjuntoDados,tempo\n";
            String colisoesPorConjuntoCSV = "funcaoHash,tamanhoConjuntoDados,colisoes\n";
            String comparacoesPorConjuntoCSV = "funcaoHash,tamanhoConjuntoDados,comparacoes\n";

            System.out.println("=========== TABELA HASH DE " + tamanhoTabelaHash + " ELEMENTOS ===========");

            // Escolhe a função de Hash
            for (int j = 0; j < 3; j++) {
                Hash moduloHash = modulosHash[j];

                System.out.println("\t---------- FUNÇÃO HASH " + moduloHash.getClass().getName() + " ----------");

                // Para cada conjunto de dados
                for (int k = 0; k < 3; k++) {
                    // Precisa limpar a tabela hash antes!
                    for (int n = 0; n < tamanhoTabelaHash; n++) tabelaHash[n] = null;

                    int[] conjuntoDeDados = conjuntosDeDados[k];
                    int tamanhoConjuntoDeDados = tamanhosConjuntosDados[k];

                    System.out.println("\t\tINSERINDO " + tamanhoConjuntoDeDados + " ELEMENTOS...");

                    // Utiliza a função hash atual para inserir na respectiva tabela hash
                    EstatisticaHash estatisticaHash = moduloHash.hash(tabelaHash, tamanhoTabelaHash, conjuntoDeDados, tamanhoConjuntoDeDados);

                    tempoPorConjuntoCSV += moduloHash.getClass().getName() + "," + tamanhoConjuntoDeDados + "," + estatisticaHash.tempoInsercao + "\n";
                    colisoesPorConjuntoCSV += moduloHash.getClass().getName() + "," + tamanhoConjuntoDeDados + "," + estatisticaHash.colisoes + "\n";

                    System.out.println("\t\tElementos Únicos Inseridos: " + estatisticaHash.elementosUnicosInseridos);
                    if (moduloHash.getClass() == HashEncadeamento.class)
                        System.out.println("\t\tComprimento da maior cadeia: " + estatisticaHash.comprimentoMaiorCadeia);
                    System.out.println("\t\tColisões: " + estatisticaHash.colisoes);
                    System.out.println("\t\tTempo de inserção total: " + estatisticaHash.tempoInsercao + "ns");
                    System.out.println("\t\tBuscas bem-sucedidas: " + estatisticaHash.buscasBemSucedidas);
                    System.out.println("\t\tBuscas mal-sucedidas: " + estatisticaHash.buscasMalSucedidas);
                    System.out.println("\t\tTempo de busca total: " + estatisticaHash.tempoBusca + "ns\n");
                }
            }

            System.out.println(tempoPorConjuntoCSV);
            System.out.println();
            System.out.println(colisoesPorConjuntoCSV);
        }
    }
}