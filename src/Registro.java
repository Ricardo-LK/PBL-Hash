public class Registro {
    private Registro proximo; // Usado somente quando usando encadeamento
    private String codigo;

    public Registro() {
        this.proximo = null;
    }

    public Registro getProximo() {
        return proximo;
    }

    public void setProximo(Registro proximo) {
        this.proximo = proximo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(int numero) {
        char[] codigo = new char[9];

        for (int i = 8; i >= 0; i--) {
            codigo[i] = (char) ('0' + (numero % 10));
            numero = numero / 10;
        }

        this.codigo = new String(codigo);
    }

    @Override
    public String toString() {
        return "Registro{" + "proximo=" + proximo.getCodigo() + ", codigo='" + codigo + '\'' + '}';
    }
}
