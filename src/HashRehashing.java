public class HashRehashing extends Hash {
    @Override
    public EstatisticaHash hash(Registro[] tabelaHash, int tamanhoTabelaHash, int[] dados, int tamanhoConjuntoDados) {
        EstatisticaHash estatisticaHash = new EstatisticaHash();

        estatisticaHash.comecoInsercao = System.nanoTime();

        for (int i = 0; i < tamanhoConjuntoDados; i++) {
            if (estatisticaHash.elementosUnicosInseridos == tamanhoTabelaHash) break;

            int dado = dados[i];
            int hash1 = funcaoHash(dado, tamanhoTabelaHash);
            int hash = hash1;
            int tentativa = 0;

            while (tabelaHash[hash] != null && tentativa < tamanhoTabelaHash) {
                estatisticaHash.colisoes++;
                tentativa++;
                hash = funcaoRehash(hash1, tentativa, tamanhoTabelaHash);
            }

            if (tabelaHash[hash] == null) {
                tabelaHash[hash] = new Registro(dado);
                estatisticaHash.elementosUnicosInseridos++;
            }
        }

        estatisticaHash.fimInsercao = System.nanoTime();
        estatisticaHash.tempoInsercao = estatisticaHash.fimInsercao - estatisticaHash.comecoInsercao;

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
            int hash1 = funcaoHash(dado, tamanhoTabelaHash);
            int hash = hash1;
            int tentativa = 0;

            Registro registro = tabelaHash[hash];
            while (registro != null && registro.getCodigoInteiro() != dado && tentativa < tamanhoTabelaHash) {
                tentativa++;
                hash = funcaoRehash(hash1, tentativa, tamanhoTabelaHash);
                registro = tabelaHash[hash];
            }

            if (registro != null && registro.getCodigoInteiro() == dado) estatisticaHash.buscasBemSucedidas++;
            else estatisticaHash.buscasMalSucedidas++;
        }

        estatisticaHash.fimBusca = System.nanoTime();
        estatisticaHash.tempoBusca = estatisticaHash.fimBusca - estatisticaHash.comecoBusca;

        return estatisticaHash;
    }

    public int funcaoHash(int dado, int modulo) {
        int h = (dado ^ (dado >>> 16)) % modulo;
        return h < 0 ? -h % modulo : h;
    }

    public int funcaoRehash(int hash1, long tentativa, int modulo) {
        int r = (hash1 + (int) tentativa * (int) tentativa) % modulo;
        return r < 0 ? -r % modulo : r;
    }
}
