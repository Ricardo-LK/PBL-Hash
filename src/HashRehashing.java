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

        // === Busca ===
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
