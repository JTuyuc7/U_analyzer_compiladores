public class Token {
    private final String type;
    private final String value;
    private final int line;
    private final int column;
    
    public Token(String type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
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
    
    @Override
    public String toString() {
        return String.format("Token{type='%s', value='%s', line=%d, column=%d}",
                type, value, line, column);
    }
    
    public boolean isError() {
        return "ERROR".equals(type);
    }
}
