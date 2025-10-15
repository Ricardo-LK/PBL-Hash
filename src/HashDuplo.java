public class HashDuplo extends Hash {
    @Override
    public EstatisticaHash hash(Registro[] tabelaHash, int tamanhoTabelaHash, int[] dados, int tamanhoConjuntoDados) {
        EstatisticaHash estatisticaHash = new EstatisticaHash();
        estatisticaHash.comecoInsercao = System.nanoTime();

        // Pré-cálculos
        int tamanhoMenosUm = tamanhoTabelaHash - 1;
        int limiteInsercao = (int)(tamanhoTabelaHash * 0.9); // Para em 90% de carga para evitar degradação

        // Inserção
        for (int i = 0; i < tamanhoConjuntoDados && estatisticaHash.elementosUnicosInseridos < limiteInsercao; i++) {
            int dado = dados[i];
            int hash1 = funcaoHash1(dado, tamanhoTabelaHash);
            int hash2 = 1 + (dado % tamanhoMenosUm); // calculado uma vez por elemento

            int hash = hash1;
            int tentativa = 0;
            boolean inserido = false;

            // Limita tentativas para evitar degradação
            int maxTentativas = tamanhoTabelaHash / 2;

            while (!inserido && tentativa < maxTentativas) {
                if (hash < 0) hash += tamanhoTabelaHash;
                if (hash >= tamanhoTabelaHash) hash %= tamanhoTabelaHash;

                if (tabelaHash[hash] == null) {
                    tabelaHash[hash] = new Registro(dado);
                    estatisticaHash.elementosUnicosInseridos++;
                    inserido = true;
                } else {
                    // DUplicata
                    if (tabelaHash[hash].getCodigoInteiro() == dado) {
                        break;
                    }
                    estatisticaHash.colisoes++;
                    tentativa++;

                    hash = hash1 + tentativa * hash2;

                    if (hash >= tamanhoTabelaHash || hash < 0) {
                        hash %= tamanhoTabelaHash;
                        if (hash < 0) hash += tamanhoTabelaHash;
                    }
                }
            }
        }

        estatisticaHash.fimInsercao = System.nanoTime();
        estatisticaHash.tempoInsercao = estatisticaHash.fimInsercao - estatisticaHash.comecoInsercao;

        // Maior, menor e média de gaps (otimizado)
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
            int hash1 = funcaoHash1(dado, tamanhoTabelaHash);
            int hash2 = 1 + (dado % tamanhoMenosUm);

            int tentativa = 0;
            int hash = hash1;
            boolean encontrado = false;

            int maxTentativasBusca = tamanhoTabelaHash / 2;

            while (!encontrado && tentativa < maxTentativasBusca) {
                if (hash < 0) hash += tamanhoTabelaHash;
                if (hash >= tamanhoTabelaHash) hash %= tamanhoTabelaHash;

                Registro registro = tabelaHash[hash];
                if (registro == null) {
                    // Cluster quebrado (elemento não existe)
                    break;
                } else if (registro.getCodigoInteiro() == dado) {
                    encontrado = true;
                    estatisticaHash.buscasBemSucedidas++;
                } else {
                    tentativa++;
                    // Mesmo cálculo da inserção
                    hash = hash1 + tentativa * hash2;

                    if (hash >= tamanhoTabelaHash || hash < 0) {
                        hash %= tamanhoTabelaHash;
                        if (hash < 0) hash += tamanhoTabelaHash;
                    }
                }
            }

            if (!encontrado) {
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

    public int funcaoHash1(int dado, int modulo) {
        int h = (dado ^ (dado >>> 16));
        h = h % modulo;
        return h < 0 ? h + modulo : h;
    }
}