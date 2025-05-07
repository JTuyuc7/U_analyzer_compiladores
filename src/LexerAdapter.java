import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;

public class LexerAdapter implements Scanner {
    private PythonLexer lexer;
    private Symbol currentSymbol = null;

    public LexerAdapter(PythonLexer lexer) {
        this.lexer = lexer;
    }

    @Override
    public Symbol next_token() throws Exception {
        try {
            currentSymbol = lexer.yylex();

            // Si llegamos al final, enviamos EOF
            if (currentSymbol == null || currentSymbol.value == null) {
                return new Symbol(sym.EOF);
            }

            return currentSymbol;
        } catch (Exception e) {
            System.err.println("LexerAdapter error: " + e.getMessage());
            // Devolver un símbolo de error para que el parser pueda manejarlo
            return new Symbol(sym.error);
        }
    }
}