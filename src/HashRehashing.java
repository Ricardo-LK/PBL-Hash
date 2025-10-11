public class HashRehashing extends Hash {
    @Override
    public EstatisticaHash hash(Registro[] tabelaHash, int tamanhoTabelaHash, int[] dados, int tamanhoConjuntoDados) {
        EstatisticaHash estatisticaHash = new EstatisticaHash();
        estatisticaHash.comecoInsercao = System.nanoTime();

        for (int i = 0; i < tamanhoConjuntoDados; i++) {
            int dado = dados[i];
            int hash = funcaoHash(dado, tamanhoTabelaHash);

            boolean parar = false;
            while (tabelaHash[hash] != null && !parar) {
                // Se o contador for igual o tamanho da tabela hash, então a tabela está cheia.
                if (estatisticaHash.elementosInseridos >= tamanhoTabelaHash) {
                    // Não há mais espaço na tabela hash.
                    parar = true;
                } else {
                    // Colisão
                    estatisticaHash.colisoes++;
                    hash = funcaoRehash(hash, estatisticaHash.colisoes, tamanhoTabelaHash);
                }
            }

            tabelaHash[hash] = new Registro(dado);
            estatisticaHash.elementosInseridos++;
        }

        estatisticaHash.fimInsercao = System.nanoTime();
        estatisticaHash.duracaoInsercao = estatisticaHash.fimInsercao - estatisticaHash.comecoInsercao;
        return estatisticaHash;
    }

    public int funcaoHash(int dado, int modulo) {
        int h = (dado ^ (dado >>> 16)) % modulo;

        if (h < 0) return -h;
        else return h;
    }

    public int funcaoRehash(int hash, int colisoes, int modulo) {
        int r = (hash + colisoes * colisoes) % modulo;

        if (r < 0) return -r;
        else return r;
    }
}
