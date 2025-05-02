

/**
 * Represents a Python symbol with type information and position.
 */
public class PythonSymbol {
    public Object value;
    public Type type;
    public int line;
    public int column;
    
    public PythonSymbol(Object value, Type type, int line, int column) {
        this.value = value;
        this.type = type;
        this.line = line;
        this.column = column;
    }
    
    @Override
    public String toString() {
        return value + " (" + type + ")";
    }
}
