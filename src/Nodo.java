public class Nodo {
    public String tipo;
    public Object valor;

    public Nodo(String tipo) {
        this.tipo = tipo;
    }

    public Nodo(String tipo, Object valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public String toString() {
        return tipo + (valor != null ? " : " + valor : "");
    }
}
