import java.awt.Color;
public class Token {
    public final String tipo;     // Ej: "KEYWORD", "OPERATOR", etc.
    public final Object valor;    // Ej: "def", 5, "a"
    public final int linea;
    public final int columna;
    public final java.awt.Color color;

    public Token(String tipo, Object valor, int linea, int columna, Color color) {
        this.tipo = tipo;
        this.valor = valor;
        this.linea = linea;
        this.columna = columna;
        this.color = color;
    }

    public String getType() {
        return tipo;
    }

    public String getValue() {
        return valor.toString();
    }

    public int getLine() {
        return linea;
    }

    public int getColumn() {
        return columna;
    }

    public Color getColor() {
        return color;
    }

    public boolean isError() {
        return tipo.equals("ERROR");
    }

    public String toString() {
        return tipo + " → " + valor + " (" + linea + "," + columna + ")";
    }
}
