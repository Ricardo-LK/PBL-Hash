public class HashEncadeamento extends Hash {
    @Override
    public EstatisticaHash hash(Registro[] tabelaHash, int tamanhoTabelaHash, int[] dados, int tamanhoConjuntoDados) {
        EstatisticaHash estatisticaHash = new EstatisticaHash();
        estatisticaHash.comecoInsercao = System.nanoTime();

        for (int i = 0; i < tamanhoConjuntoDados; i++) {
            int dado = dados[i];
            int hash = funcaoHash(dado, tamanhoTabelaHash);

            if (tabelaHash[hash] == null) {
                tabelaHash[hash] = new Registro(dado);
                estatisticaHash.elementosInseridos++;

            } else {
                Registro novoRegistro = new Registro(dado);
                novoRegistro.setProximo(tabelaHash[hash]);
                tabelaHash[hash] =  novoRegistro;

                estatisticaHash.elementosInseridos++;
                estatisticaHash.colisoes++;

            }
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
}