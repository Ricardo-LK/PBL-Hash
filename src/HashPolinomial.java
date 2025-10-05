public class HashPolinomial extends Hash {
    @Override
    public EstatisticaHash hash(Registro[] tabelaHash, int tamanhoTabelaHash, int[] dados, int tamanhoConjuntoDados) {
        // Se der colisão?
        System.out.println("Hash polinomial não implementado.");

        return null;
    }

    private long funcaoHash(String dado, long modulo) {
        long h = 0;
        char[] chars = dado.toCharArray();
        int tamanhoDado = dado.length();

        for (int i = 0; i < tamanhoDado; i++) {
            char c = chars[i];

            h = (h * 31 + (long) c) % modulo;
        }

        return h;
    }
}
