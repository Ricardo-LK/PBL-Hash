public class Registro {
    private String codigo;

    public Registro() {}

    public void setCodigo(int numero) {
        char[] codigo = new char[9];

        for (int i = 8; i >= 0; i--) {
            codigo[i] = (char)('0' + (numero % 10));
            numero = numero / 10;
        }

        this.codigo = new String(codigo);
    }

    public String getCodigo() {
        return codigo;
    }

    @Override
    public String toString() {
        return "Registro = " + codigo;
    }
}
