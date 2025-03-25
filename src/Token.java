import java.awt.Color;

public class Token {
    private final String type;
    private final String value;
    private final int line;
    private final int column;
    private final Color color;

    public Token(String type, String value, int line, int column, Color color) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
        this.color = color;
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
        return type.equals("ERROR");
    }

    @Override
    public String toString() {
        return String.format("Token{type='%s', value='%s', line=%d, column=%d, color=%s}", 
            type, value, line, column, color);
    }

}
