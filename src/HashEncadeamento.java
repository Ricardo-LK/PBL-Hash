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
                estatisticaHash.colisoes++; // apenas 1 por inserção que colide
                Registro atual = tabelaHash[hash];
                while (atual.getProximo() != null) {
                    atual = atual.getProximo();
                }
                atual.setProximo(new Registro(dado));
            }

            estatisticaHash.elementosUnicosInseridos++;
        }

        estatisticaHash.fimInsercao = System.nanoTime();
        estatisticaHash.tempoInsercao = estatisticaHash.fimInsercao - estatisticaHash.comecoInsercao;

        // Calcula maior cadeia
        int maiorCadeia = 0;
        for (int i = 0; i < tamanhoTabelaHash; i++) {
            int tamanhoCadeia = 0;
            if (tabelaHash[i] == null) continue;
            Registro atual = tabelaHash[i];
            while (atual != null) {
                tamanhoCadeia++;
                atual = atual.getProximo();
            }
            if (tamanhoCadeia > maiorCadeia) maiorCadeia = tamanhoCadeia;
        }
        estatisticaHash.comprimentoMaiorCadeia = maiorCadeia;

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

            if (registro == null)
                estatisticaHash.buscasMalSucedidas++;
            else
                estatisticaHash.buscasBemSucedidas++;
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
