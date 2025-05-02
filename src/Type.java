//package src;

/**
 * Represents a Python type with compatibility checking.
 */
public class Type {
    private String name;
    
    public Type(String name) {
        this.name = name;
    }
    
    /**
     * Checks if this type is compatible with another type for operations.
     */
    public boolean isCompatible(Type other) {
        // Basic type compatibility rules
        if (name.equals(other.name)) return true;
        
        // Numeric types are compatible with each other
        if ((name.equals("int") || name.equals("float")) && 
            (other.name.equals("int") || other.name.equals("float"))) {
            return true;
        }
        
        // String concatenation with + operator
        if (name.equals("string") && other.name.equals("string")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Determines the result type of an operation between this type and another.
     */
    public Type getResultType(Type other, String operator) {
        // Addition
        if (operator.equals("+")) {
            // String concatenation
            if (name.equals("string") && other.name.equals("string")) {
                return new Type("string");
            }
            
            // Numeric addition
            if (name.equals("float") || other.name.equals("float")) {
                return new Type("float");
            }
            
            if (name.equals("int") && other.name.equals("int")) {
                return new Type("int");
            }
        }
        
        // Subtraction, multiplication, division
        if (operator.equals("-") || operator.equals("*") || operator.equals("/")) {
            // String multiplication (only for *)
            if (operator.equals("*") && 
                ((name.equals("string") && other.name.equals("int")) || 
                 (name.equals("int") && other.name.equals("string")))) {
                return new Type("string");
            }
            
            // Numeric operations
            if (name.equals("float") || other.name.equals("float")) {
                return new Type("float");
            }
            
            if (name.equals("int") && other.name.equals("int")) {
                // Division always returns float in Python 3
                if (operator.equals("/")) {
                    return new Type("float");
                }
                return new Type("int");
            }
        }
        
        // Modulo and power
        if (operator.equals("%") || operator.equals("**")) {
            if (name.equals("float") || other.name.equals("float")) {
                return new Type("float");
            }
            
            if (name.equals("int") && other.name.equals("int")) {
                return new Type("int");
            }
        }
        
        // Default to error type
        return new Type("error");
    }
    
    @Override
    public String toString() {
        return name;
    }
}
