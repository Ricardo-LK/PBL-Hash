public class HashRehashing extends Hash {
    @Override
    public EstatisticaHash hash(Registro[] tabelaHash, int tamanhoTabelaHash, int[] dados, int tamanhoConjuntoDados) {
        EstatisticaHash estatisticaHash = new EstatisticaHash();
        estatisticaHash.comecoInsercao = System.nanoTime();

        // Para quando a tabela está 90% cheia para evitar degradação
        int limiteInsercao = (int) (tamanhoTabelaHash * 0.9);

        for (int i = 0; i < tamanhoConjuntoDados && estatisticaHash.elementosUnicosInseridos < limiteInsercao; i++) {
            int dado = dados[i];
            int hash1 = funcaoHash(dado, tamanhoTabelaHash);
            int hash = hash1;
            int tentativa = 0;
            boolean inserido = false;

            // Limita o número máximo de tentativas
            int maxTentativas = tamanhoTabelaHash / 2;

            while (!inserido && tentativa < maxTentativas) {
                if (tabelaHash[hash] == null) {
                    tabelaHash[hash] = new Registro(dado);
                    estatisticaHash.elementosUnicosInseridos++;
                    inserido = true;
                } else {
                    // Verificação rápida de duplicata
                    if (tabelaHash[hash].getCodigoInteiro() == dado) {
                        break;
                    }
                    estatisticaHash.colisoes++;
                    tentativa++;
                    hash = funcaoRehash(hash1, tentativa, tamanhoTabelaHash);
                }
            }
        }

        estatisticaHash.fimInsercao = System.nanoTime();
        estatisticaHash.tempoInsercao = estatisticaHash.fimInsercao - estatisticaHash.comecoInsercao;

        // Calculando gap
        estatisticaHash.maiorGap = 0;
        estatisticaHash.menorGap = 2147483647;
        estatisticaHash.mediaGap = 0;
        estatisticaHash.qtdeGaps = 0;

        int gapAtual = 0;

        for (int i = 0; i < tamanhoTabelaHash; i++) {
            if (tabelaHash[i] == null) {
                gapAtual++;
            } else {
                if (gapAtual > 0) {
                    atualizarGaps(estatisticaHash, gapAtual);
                    gapAtual = 0;
                }
            }
        }

        if (gapAtual > 0) {
            atualizarGaps(estatisticaHash, gapAtual);
        }

        // Sem gaps
        if (estatisticaHash.qtdeGaps == 0) {
            estatisticaHash.maiorGap = 0;
            estatisticaHash.menorGap = 0;
            estatisticaHash.mediaGap = 0;
        } else {
            estatisticaHash.mediaGap = estatisticaHash.mediaGap / estatisticaHash.qtdeGaps;
        }

        if (estatisticaHash.menorGap == 2147483647) {
            estatisticaHash.menorGap = 0;
        }

        // Busca
        estatisticaHash.comecoBusca = System.nanoTime();

        for (int i = 0; i < tamanhoConjuntoDados; i++) {
            int dado = dados[i];
            int hash1 = funcaoHash(dado, tamanhoTabelaHash);
            int hash = hash1;
            int tentativa = 0;
            boolean encontrado = false;

            int maxTentativasBusca = tamanhoTabelaHash;

            while (!encontrado && tentativa < maxTentativasBusca) {
                Registro registro = tabelaHash[hash];
                if (registro == null) {
                    // Cluster quebrado - elemento não existe
                    break;
                } else if (registro.getCodigoInteiro() == dado) {
                    encontrado = true;
                    estatisticaHash.buscasBemSucedidas++;
                } else {
                    tentativa++;
                    hash = funcaoRehash(hash1, tentativa, tamanhoTabelaHash);
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

    private void atualizarGaps(EstatisticaHash estatistica, int gap) {
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
        int h = (dado ^ (dado >>> 16)) % modulo;
        return h < 0 ? -h % modulo : h;
    }

    public int funcaoRehash(int hash1, int tentativa, int modulo) {
        int incremento = tentativa * tentativa;
        int r = hash1 + incremento;
        if (r >= modulo) r %= modulo;
        return r;
    }
}