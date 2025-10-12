public class HashEncadeamento extends Hash {
    @Override
    public EstatisticaHash hash(Registro[] tabelaHash, int tamanhoTabelaHash, int[] dados, int tamanhoConjuntoDados) {
        EstatisticaHash estatisticaHash = new EstatisticaHash();

        estatisticaHash.comecoInsercao = System.nanoTime();

        // Inserção
        for (int i = 0; i < tamanhoConjuntoDados; i++) {
            int dado = dados[i];
            int hash = funcaoHash(dado, tamanhoTabelaHash);

            if (tabelaHash[hash] == null) {
                tabelaHash[hash] = new Registro(dado);
            } else {
                estatisticaHash.colisoes++;
                Registro atual = tabelaHash[hash];
                while (atual.getProximo() != null) {
                    estatisticaHash.colisoes++;
                    atual = atual.getProximo();
                }
                atual.setProximo(new Registro(dado));
            }

            estatisticaHash.elementosUnicosInseridos++;
        }

        estatisticaHash.fimInsercao = System.nanoTime();
        estatisticaHash.tempoInsercao = estatisticaHash.fimInsercao - estatisticaHash.comecoInsercao;

        // Array para armazenar os tamanhos das cadeias
        int[] tamanhosCadeias = new int[tamanhoTabelaHash];

        // Calcular tamanho de cada cadeia
        for (int i = 0; i < tamanhoTabelaHash; i++) {
            int tamanhoCadeia = 0;
            if (tabelaHash[i] != null) {
                Registro atual = tabelaHash[i];
                while (atual != null) {
                    tamanhoCadeia++;
                    atual = atual.getProximo();
                }
            }
            tamanhosCadeias[i] = tamanhoCadeia;
        }

        // Encontrar as 3 maiores cadeias
        System.out.println("\t\tAs 3 maiores listas encadeadas:");
        for (int posicao = 1; posicao <= 3; posicao++) {
            int maiorIndex = -1;
            int maiorTamanho = -1;

            for (int i = 0; i < tamanhoTabelaHash; i++) {
                if (tamanhosCadeias[i] > maiorTamanho) {
                    maiorTamanho = tamanhosCadeias[i];
                    maiorIndex = i;
                }
            }

            if (maiorTamanho > 0) {
                System.out.printf("\t\t%dº: índice %d com %d elementos%n", posicao, maiorIndex, maiorTamanho);
                // Marcar como processado
                tamanhosCadeias[maiorIndex] = -1;
            }
        }

        // Maior, menor e média de gaps
        estatisticaHash.maiorGap = 0;
        estatisticaHash.menorGap = 2_147_483_647;
        estatisticaHash.mediaGap = 0;
        estatisticaHash.qtdeGaps = 0;

        int gapAtual = 0;

        for (int i = 0; i < tamanhoTabelaHash; i++) {
            if (tabelaHash[i] == null) {
                gapAtual++;
            } else {
                if (gapAtual > 0) {
                    // Atualiza estatísticas para cada gap encontrado
                    if (gapAtual > estatisticaHash.maiorGap) {
                        estatisticaHash.maiorGap = gapAtual;
                    }
                    if (gapAtual < estatisticaHash.menorGap) {
                        estatisticaHash.menorGap = gapAtual;
                    }
                    estatisticaHash.mediaGap += gapAtual;
                    estatisticaHash.qtdeGaps++;
                    gapAtual = 0;
                }
            }
        }

        // Gap no final da tabela
        if (gapAtual > 0) {
            if (gapAtual > estatisticaHash.maiorGap) {
                estatisticaHash.maiorGap = gapAtual;
            }
            if (gapAtual < estatisticaHash.menorGap) {
                estatisticaHash.menorGap = gapAtual;
            }
            estatisticaHash.mediaGap += gapAtual;
            estatisticaHash.qtdeGaps++;
        }

        // Calcula média e trata caso sem gaps
        if (estatisticaHash.qtdeGaps > 0) {
            estatisticaHash.mediaGap = estatisticaHash.mediaGap / (double) estatisticaHash.qtdeGaps;
        } else {
            estatisticaHash.maiorGap = 0;
            estatisticaHash.menorGap = 0;
            estatisticaHash.mediaGap = 0;
        }

        if (estatisticaHash.menorGap == 2_147_483_647) {
            estatisticaHash.menorGap = 0;
        }

        // Busca
        estatisticaHash.comecoBusca = System.nanoTime();

        for (int i = 0; i < tamanhoConjuntoDados; i++) {
            int dado = dados[i];
            int hash = funcaoHash(dado, tamanhoTabelaHash);
            Registro registro = tabelaHash[hash];

            while (registro != null && registro.getCodigoInteiro() != dado) {
                estatisticaHash.colisoes++;
                registro = registro.getProximo();
            }

            if (registro == null) estatisticaHash.buscasMalSucedidas++;
            else estatisticaHash.buscasBemSucedidas++;
        }

        estatisticaHash.fimBusca = System.nanoTime();
        estatisticaHash.tempoBusca = estatisticaHash.fimBusca - estatisticaHash.comecoBusca;

        return estatisticaHash;
    }

    public int funcaoHash(int dado, int modulo) {
        int h = (dado ^ (dado >>> 16)) % modulo;
        return h < 0 ? -h % modulo : h;
    }
}
