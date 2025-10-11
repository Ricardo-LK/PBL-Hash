public class HashDuplo extends Hash {
    @Override
    public EstatisticaHash hash(Registro[] tabelaHash, int tamanhoTabelaHash, int[] dados, int tamanhoConjuntoDados) {
        EstatisticaHash estatisticaHash = new EstatisticaHash();
        estatisticaHash.comecoInsercao = System.nanoTime();

        for (int i = 0; i < tamanhoConjuntoDados; i++) {
            int dado = dados[i];

            int hash1 = funcaoHash1(dado, tamanhoTabelaHash);

            // Hash2 sempre ímpar para garantir coprimidade com potências de 2
            int hash2 = 1 + ((dado & 0x7FFFFFFF) % (tamanhoTabelaHash - 1));
            if (hash2 % 2 == 0) hash2++; // Garante ímpar

            int hash = hash1;
            int tentativa = 0;

            while (tabelaHash[hash] != null) {
                if (estatisticaHash.elementosInseridos >= tamanhoTabelaHash) {
                    // Não há mais espaço na tabela hash.
                    estatisticaHash.fimInsercao = System.nanoTime();
                    estatisticaHash.duracaoInsercao = estatisticaHash.fimInsercao - estatisticaHash.comecoInsercao;
                    return estatisticaHash;
                } else {
                    estatisticaHash.colisoes++;
                    tentativa++;
                    hash = (hash1 + tentativa * hash2) % tamanhoTabelaHash;
                    if (hash < 0) hash += tamanhoTabelaHash;
                }
            }

            // Só insere se o slot estiver vazio
            if (tabelaHash[hash] == null) {
                tabelaHash[hash] = new Registro(dado);
                estatisticaHash.elementosInseridos++;
            }
        }

        estatisticaHash.fimInsercao = System.nanoTime();
        estatisticaHash.duracaoInsercao = estatisticaHash.fimInsercao - estatisticaHash.comecoInsercao;
        return estatisticaHash;
    }

    public int funcaoHash1(int dado, int modulo) {
        int h = (dado ^ (dado >>> 16)) % modulo;
        return h < 0 ? -h % modulo : h;
    }
}