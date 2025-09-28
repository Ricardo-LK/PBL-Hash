import java.util.Random;

public class Main {

    private final static int UM_MIL = 1000;
    private final static int DEZ_MIL = 10000;
    private final static int CEM_MIL = 100000;
    private final static int UM_MILHAO = 1000000;
    private final static int DEZ_MILHOES = 10000000;

    /**
     * Podemos plotar as seguintes métricas:
     * - Tempo de inserção x Tamanho da Tabela Hash x Tamanho do conjunto de dados x Função Hash
     * - Tempo de busca x Tamanho da Tabela Hash x Tamanho do conjunto de dados x Função Hash
     * - Número de colisões x Tamanho da Tabela Hash x Tamanho do conjunto de dados x Função hash
     * - Lacunas x Tamanho da Tabela Hash x Tamanho do conjunto de dados x Função hash
     * - Uso de memória x Tamanho da Tabela Hash x Tamanho de conjunto de dados x Função Hash
     */

    public static void main(String[] args) {
        Random rand = new Random(2025);

        // Funções Hash
        Hash[] modulosHash = {new HashRehashing(), new HashEncadeamento(), new HashPolinomial()};

        // Tamanhos das tabelas hash e dos conjuntos de dados
        int[] tamanhosTabelasHash = {UM_MIL, DEZ_MIL, CEM_MIL};

        // Arrays com os dados a serem utilizados
        int[] conjuntoCemMilDados = new int[CEM_MIL];
        int[] conjuntoUmMilhaoDados = new int[UM_MILHAO];
        int[] conjuntoDezMilhoesDados = new int[DEZ_MILHOES];
        int[][] conjuntosDeDados = {conjuntoCemMilDados, conjuntoUmMilhaoDados, conjuntoDezMilhoesDados};

        // Popula cada array de dados
        for (int i = 0; i < CEM_MIL; i++)
            conjuntoCemMilDados[i] = rand.nextInt(0, 999999999);

        for (int i = 0; i < UM_MILHAO; i++)
            conjuntoUmMilhaoDados[i] = rand.nextInt(0, 999999999);

        for (int i = 0; i < DEZ_MILHOES; i++)
            conjuntoDezMilhoesDados[i] = rand.nextInt(0, 999999999);


        for (int i = 0; i < 3; i++) {
            // Escolhe a função de Hash
            Hash moduloHash = modulosHash[i];

            // Para os 3 tamanhos de tabela hash
            for (int j = 0; j < 3; j++) {
                Registro[] tabelaHash = new Registro[tamanhosTabelasHash[j]];

                // Para cada conjunto de dados
                for (int k = 0; k < 3; k++) {
                    // Precisa limpar a tabela hash antes!
                    for (int n = 0; n < tamanhosTabelasHash[j]; n++) tabelaHash[n] = null;

                    int[] conjuntoDeDados = conjuntosDeDados[k];

                    // Utiliza a função hash atual para inserir na respectiva tabela hash
                    moduloHash.hash(tabelaHash, conjuntoDeDados);
                }
            }
        }
    }
}