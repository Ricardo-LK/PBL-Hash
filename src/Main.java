import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class Main {

    private final static int CEM_MIL = 100000;
    private final static int UM_MILHAO = 1000000;
    private final static int DEZ_MILHOES = 10000000;

    public static void main(String[] args) {
        Random rand = new Random(2025);

        // Funções Hash
        Hash[] modulosHash = {new HashRehashing(), new HashEncadeamento(), new HashDuplo()};

        // Tamanhos das tabelas hash e dos conjuntos de dados
        int[] tamanhosTabelasHash = {CEM_MIL, UM_MILHAO, DEZ_MILHOES};
        int[] tamanhosConjuntosDados = {CEM_MIL, UM_MILHAO, DEZ_MILHOES};

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
            String tempoInsercaoPorConjuntoCSV = "funcaoHash,tamanhoConjuntoDados,tempoInsercao\n";
            String tempoBuscaPorConjuntoCSV = "funcaoHash,tamanhoConjuntoDados,tempoBusca\n";
            String colisoesPorConjuntoCSV = "funcaoHash,tamanhoConjuntoDados,colisoes\n";
            String maiorGapPorConjuntoCSV = "funcaoHash,tamanhoConjuntoDados,maiorGap\n";

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

                    System.out.println("\t\tTESTANDO COM " + tamanhoConjuntoDeDados + " ELEMENTOS...");

                    // Utiliza a função hash atual para inserir na respectiva tabela hash
                    EstatisticaHash estatisticaHash = moduloHash.hash(tabelaHash, tamanhoTabelaHash, conjuntoDeDados, tamanhoConjuntoDeDados);

                    tempoInsercaoPorConjuntoCSV += moduloHash.getClass().getName() + "," + tamanhoConjuntoDeDados + "," + estatisticaHash.tempoInsercao + "\n";
                    tempoBuscaPorConjuntoCSV += moduloHash.getClass().getName() + "," + tamanhoConjuntoDeDados + "," + estatisticaHash.tempoBusca + "\n";
                    colisoesPorConjuntoCSV += moduloHash.getClass().getName() + "," + tamanhoConjuntoDeDados + "," + estatisticaHash.colisoes + "\n";
                    maiorGapPorConjuntoCSV += moduloHash.getClass().getName() + "," + tamanhoConjuntoDeDados + "," + estatisticaHash.maiorGap + "\n";

                    System.out.println("\t\tElementos Únicos Inseridos: " + estatisticaHash.elementosUnicosInseridos);
                    System.out.println("\t\tColisões: " + estatisticaHash.colisoes);
                    System.out.println("\t\tTempo de inserção total: " + estatisticaHash.tempoInsercao + "ns");
                    System.out.println("\t\tBuscas bem-sucedidas: " + estatisticaHash.buscasBemSucedidas);
                    System.out.println("\t\tBuscas mal-sucedidas: " + estatisticaHash.buscasMalSucedidas);
                    System.out.println("\t\tTempo de busca total: " + estatisticaHash.tempoBusca + "ns");
                    System.out.println("\t\tQuantidade de gaps: " + estatisticaHash.qtdeGaps);
                    System.out.println("\t\tMaior gap: " + estatisticaHash.maiorGap);
                    System.out.println("\t\tMenor gap: " + estatisticaHash.menorGap);
                    System.out.println("\t\tMedia gap: " + estatisticaHash.mediaGap);
                    System.out.println();
                }
            }

            // Exportando os dados com BufferedWriter e FileWriter.
            String diretorio = "plots/" + tamanhoTabelaHash + "/";

            // Criando diretório se não existe
            try {
                Files.createDirectories(Path.of(diretorio));
            } catch (IOException e) {
                System.err.println("Falha ao criar diretório: " + e.getMessage());
            }

            // Tempo de Inserção
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(diretorio + "tempoInsercao.csv"))) {
                writer.write(tempoInsercaoPorConjuntoCSV);
            } catch (IOException e) {
                System.err.println("Falha ao escrever arquivo de saída: " + e.getMessage());
            }

            // Tempo de Busca
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(diretorio + "tempoBusca.csv"))) {
                writer.write(tempoBuscaPorConjuntoCSV);
            } catch (IOException e) {
                System.err.println("Falha ao escrever arquivo de saída: " + e.getMessage());
            }

            // Colisões
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(diretorio + "colisoes.csv"))) {
                writer.write(colisoesPorConjuntoCSV);
            } catch (IOException e) {
                System.err.println("Falha ao escrever arquivo de saída: " + e.getMessage());
            }

            // Maior Gap
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(diretorio + "maiorGap.csv"))) {
                writer.write(maiorGapPorConjuntoCSV);
            } catch (IOException e) {
                System.err.println("Falha ao escrever arquivo de saída: " + e.getMessage());
            }
        }
    }
}