//package src;

import java.util.HashMap;
import java.util.Map;

/**
 * Symbol table for tracking variables and their types.
 */
public class SymbolTable {
    private Map<String, Type> symbols = new HashMap<>();
    
    /**
     * Add or update a variable in the symbol table.
     * 
     * @param name The variable name
     * @param type The variable type
     */
    public void put(String name, Type type) {
        symbols.put(name, type);
    }
    
    /**
     * Get the type of a variable.
     * 
     * @param name The variable name
     * @return The variable type, or null if not found
     */
    public Type get(String name) {
        return symbols.get(name);
    }
    
    /**
     * Check if a variable exists in the symbol table.
     * 
     * @param name The variable name
     * @return true if the variable exists, false otherwise
     */
    public boolean contains(String name) {
        return symbols.containsKey(name);
    }
    
    /**
     * Clear the symbol table.
     */
    public void clear() {
        symbols.clear();
    }
    
    /**
     * Get the number of variables in the symbol table.
     * 
     * @return The number of variables
     */
    public int size() {
        return symbols.size();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Symbol Table:\n");
        for (Map.Entry<String, Type> entry : symbols.entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
