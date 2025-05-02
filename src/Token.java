import java.awt.Color;

public class Token {
    private String type;
    private String value;
    private int line;
    private int column;
    private Color color;
    private boolean isError;

    public Token(String type, String value, int line, int column, Color color) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
        this.color = color;
        this.isError = type.equals("ERROR");
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public Color getColor() {
        return color;
    }

    public boolean isError() {
        return isError;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}
