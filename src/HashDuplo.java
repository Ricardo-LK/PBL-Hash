public class HashDuplo extends Hash {
    @Override
    public EstatisticaHash hash(Registro[] tabelaHash, int tamanhoTabelaHash, int[] dados, int tamanhoConjuntoDados) {
        EstatisticaHash estatisticaHash = new EstatisticaHash();
        estatisticaHash.comecoInsercao = System.nanoTime();

        for (int i = 0; i < tamanhoConjuntoDados; i++) {
            int dado = dados[i];

            int hash1 = funcaoHash1(dado, tamanhoTabelaHash);
            int hash2 = 1 + (dado % (tamanhoTabelaHash - 1)); // duplo hash clássico

            int hash = hash1;
            int tentativa = 0;

            while (tabelaHash[hash] != null && tentativa < tamanhoTabelaHash) {
                estatisticaHash.colisoes++;
                tentativa++;
                hash = (hash1 + tentativa * hash2) % tamanhoTabelaHash;
                if (hash < 0) hash += tamanhoTabelaHash;
            }

            if (tabelaHash[hash] == null) {
                tabelaHash[hash] = new Registro(dado);
                estatisticaHash.elementosUnicosInseridos++;
            }
        }

        estatisticaHash.fimInsercao = System.nanoTime();
        estatisticaHash.tempoInsercao = estatisticaHash.fimInsercao - estatisticaHash.comecoInsercao;

        estatisticaHash.comecoBusca = System.nanoTime();

        for (int i = 0; i < tamanhoConjuntoDados; i++) {
            int dado = dados[i];

            int hash1 = funcaoHash1(dado, tamanhoTabelaHash);
            int hash2 = 1 + (dado % (tamanhoTabelaHash - 1));

            int tentativa = 0;
            int hash = hash1;

            Registro registro = tabelaHash[hash];
            while (registro != null && registro.getCodigoInteiro() != dado && tentativa < tamanhoTabelaHash) {
                tentativa++;
                hash = (hash1 + tentativa * hash2) % tamanhoTabelaHash;
                if (hash < 0) hash += tamanhoTabelaHash;
                registro = tabelaHash[hash];
            }

            if (registro != null && registro.getCodigoInteiro() == dado) estatisticaHash.buscasBemSucedidas++;
            else estatisticaHash.buscasMalSucedidas++;
        }

        estatisticaHash.fimBusca = System.nanoTime();
        estatisticaHash.tempoBusca = estatisticaHash.fimBusca - estatisticaHash.comecoBusca;

        return estatisticaHash;
    }

    public int funcaoHash1(int dado, int modulo) {
        int h = (dado ^ (dado >>> 16)) % modulo;
        return h < 0 ? -h % modulo : h;
    }
}
