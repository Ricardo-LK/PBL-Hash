import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random rand = new Random(2025);

        // Tabelas Hash
        int[] tamanhosTabela = {1000, 10000, 100000};

        // Conjuntos de dados
        // Cem mil
        Registro[] conjuntoCemMil = new Registro[100000];
        for (int i = 0; i < conjuntoCemMil.length; i++) {
            conjuntoCemMil[i] = new Registro();
            conjuntoCemMil[i].setCodigo(rand.nextInt(1000000000));
        }

        // Hashes
    }
}