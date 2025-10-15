public class HashEncadeamento extends Hash {
    @Override
    public EstatisticaHash hash(Registro[] tabelaHash, int tamanhoTabelaHash, int[] dados, int tamanhoConjuntoDados) {
        EstatisticaHash estatisticaHash = new EstatisticaHash();

        estatisticaHash.comecoInsercao = System.nanoTime();

        // Inserção otimizada
        for (int i = 0; i < tamanhoConjuntoDados; i++) {
            int dado = dados[i];
            int hash = funcaoHash(dado, tamanhoTabelaHash);

            if (tabelaHash[hash] == null) {
                tabelaHash[hash] = new Registro(dado);
                estatisticaHash.elementosUnicosInseridos++;
            } else {
                estatisticaHash.colisoes++;
                Registro atual = tabelaHash[hash];
                Registro anterior = null;

                // Percorre a lista verificando duplicatas
                while (atual != null) {
                    if (atual.getCodigoInteiro() == dado) {
                        break; // Duplicata encontrada
                    }
                    anterior = atual;
                    atual = atual.getProximo();
                    if (atual != null) {
                        estatisticaHash.colisoes++;
                    }
                }

                // Só insere se não for duplicata
                if (atual == null && anterior != null) {
                    anterior.setProximo(new Registro(dado));
                    estatisticaHash.elementosUnicosInseridos++;
                }
            }
        }

        estatisticaHash.fimInsercao = System.nanoTime();
        estatisticaHash.tempoInsercao = estatisticaHash.fimInsercao - estatisticaHash.comecoInsercao;

        // Array para armazenar os tamanhos das cadeias
        int[] tamanhosCadeias = new int[tamanhoTabelaHash];
        int maiorTamanho1 = -1, maiorTamanho2 = -1, maiorTamanho3 = -1;
        int indiceMaior1 = -1, indiceMaior2 = -1, indiceMaior3 = -1;

        // Calcular tamanho de cada cadeia e encontrar as 3 maiores em uma única passagem
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

            // Atualiza as 3 maiores cadeias durante a passagem
            if (tamanhoCadeia > maiorTamanho1) {
                maiorTamanho3 = maiorTamanho2;
                indiceMaior3 = indiceMaior2;
                maiorTamanho2 = maiorTamanho1;
                indiceMaior2 = indiceMaior1;
                maiorTamanho1 = tamanhoCadeia;
                indiceMaior1 = i;
            } else if (tamanhoCadeia > maiorTamanho2) {
                maiorTamanho3 = maiorTamanho2;
                indiceMaior3 = indiceMaior2;
                maiorTamanho2 = tamanhoCadeia;
                indiceMaior2 = i;
            } else if (tamanhoCadeia > maiorTamanho3) {
                maiorTamanho3 = tamanhoCadeia;
                indiceMaior3 = i;
            }
        }

        // Exibe as 3 maiores cadeias
        System.out.println("\t\tAs 3 maiores listas encadeadas:");
        if (maiorTamanho1 > 0) {
            System.out.printf("\t\t1º: índice %d com %d elementos%n", indiceMaior1, maiorTamanho1);
        }
        if (maiorTamanho2 > 0) {
            System.out.printf("\t\t2º: índice %d com %d elementos%n", indiceMaior2, maiorTamanho2);
        }
        if (maiorTamanho3 > 0) {
            System.out.printf("\t\t3º: índice %d com %d elementos%n", indiceMaior3, maiorTamanho3);
        }

        // Maior, menor e média de gaps
        estatisticaHash.maiorGap = 0;
        estatisticaHash.menorGap = 2147483647;
        estatisticaHash.mediaGap = 0;
        estatisticaHash.qtdeGaps = 0;

        int gapAtual = 0;
        boolean emGap = false;

        for (int i = 0; i < tamanhoTabelaHash; i++) {
            if (tabelaHash[i] == null) {
                gapAtual++;
                emGap = true;
            } else {
                if (emGap) {
                    atualizarEstatisticasGap(estatisticaHash, gapAtual);
                    gapAtual = 0;
                    emGap = false;
                }
            }
        }

        // Gap no final da tabela
        if (emGap) {
            atualizarEstatisticasGap(estatisticaHash, gapAtual);
        }

        // Calcula média e trata caso sem gaps
        if (estatisticaHash.qtdeGaps > 0) {
            estatisticaHash.mediaGap = estatisticaHash.mediaGap / (double) estatisticaHash.qtdeGaps;
        } else {
            estatisticaHash.maiorGap = 0;
            estatisticaHash.menorGap = 0;
            estatisticaHash.mediaGap = 0;
        }

        if (estatisticaHash.menorGap == 2147483647) {
            estatisticaHash.menorGap = 0;
        }

        // Busca
        estatisticaHash.comecoBusca = System.nanoTime();

        for (int i = 0; i < tamanhoConjuntoDados; i++) {
            int dado = dados[i];
            int hash = funcaoHash(dado, tamanhoTabelaHash);
            Registro registro = tabelaHash[hash];

            while (registro != null) {
                if (registro.getCodigoInteiro() == dado) {
                    estatisticaHash.buscasBemSucedidas++;
                    break;
                }
                estatisticaHash.colisoes++;
                registro = registro.getProximo();
            }

            if (registro == null) {
                estatisticaHash.buscasMalSucedidas++;
            }
        }

        estatisticaHash.fimBusca = System.nanoTime();
        estatisticaHash.tempoBusca = estatisticaHash.fimBusca - estatisticaHash.comecoBusca;

        return estatisticaHash;
    }

    // Método auxiliar para atualizar estatísticas de gaps
    private void atualizarEstatisticasGap(EstatisticaHash estatistica, int gap) {
        if (gap > estatistica.maiorGap) {
            estatistica.maiorGap = gap;
        }
        if (gap < estatistica.menorGap) {
            estatistica.menorGap = gap;
        }
        estatistica.mediaGap += gap;
        estatistica.qtdeGaps++;
    }

    public int funcaoHash(int dado, int modulo) {
        int h = (dado ^ (dado >>> 16));
        h = h % modulo;
        return h < 0 ? h + modulo : h;
    }
}