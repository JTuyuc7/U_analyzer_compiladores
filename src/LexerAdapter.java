import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;

public class LexerAdapter implements Scanner {
    private PythonLexer lexer;
    private Symbol currentSymbol = null;
    private boolean debugMode = true; // Activar para depuración
    
    public LexerAdapter(PythonLexer lexer) {
        this.lexer = lexer;
    }
    
    @Override
    public Symbol next_token() throws Exception {
        try {
            currentSymbol = lexer.yylex();
            
            // Log del token para depuración
            if (debugMode && currentSymbol != null) {
                String tokenName = "UNKNOWN";
                try {
                    tokenName = sym.terminalNames[currentSymbol.sym];
                } catch (Exception e) {
                    // Ignore array index errors
                }
                
                String tokenValue = currentSymbol.value != null ? 
                    (currentSymbol.value instanceof Token ? 
                        ((Token)currentSymbol.value).valor.toString() : 
                        currentSymbol.value.toString()) : 
                    "null";
                
                System.out.println("DEBUG TOKEN: " + tokenName + " - Value: " + tokenValue);
            }
            
            // Si llegamos al final, enviamos EOF
            if (currentSymbol == null || currentSymbol.value == null) {
                if (debugMode) System.out.println("DEBUG: Returning EOF token");
                return new Symbol(sym.EOF);
            }
            
            return currentSymbol;
        } catch (Exception e) {
            System.err.println("LexerAdapter error: " + e.getMessage());
            e.printStackTrace();
            // Devolver un símbolo de error para que el parser pueda manejarlo
            return new Symbol(sym.error);
        }
    }
}
