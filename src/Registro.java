public class Registro {
    private Registro proximo; // Usado somente quando usando encadeamento
    private String codigo;
    private int codigoInteiro;

    public Registro() {
        this.proximo = null;
        this.codigo = "";
        this.codigoInteiro = -1;
    }

    public Registro(int codigoInteiro) {
        this.proximo = null;
        this.codigoInteiro = codigoInteiro;
        this.setCodigo(codigoInteiro);
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
        this.codigoInteiro = numero;

        char[] codigo = new char[9];

        for (int i = 8; i >= 0; i--) {
            codigo[i] = (char) ('0' + (numero % 10));
            numero = numero / 10;
        }

        this.codigo = new String(codigo);
    }

    public int getCodigoInteiro() {
        return codigoInteiro;
    }

    @Override
    public String toString() {
        return "Registro{" + "proximo=" + proximo.getCodigo() + ", codigo='" + codigo + '\'' + '}';
    }
}
