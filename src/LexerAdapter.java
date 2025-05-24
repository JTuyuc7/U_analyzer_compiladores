/*
 * ===============================================================================
 * ADAPTADOR DEL ANALIZADOR LÉXICO - INTERFAZ ENTRE LEXER Y PARSER
 * ===============================================================================
 * 
 * FASES DEL COMPILADOR: Conecta el Análisis Léxico (Fase 1) con el Sintáctico (Fase 2)
 * 
 * Esta clase actúa como un adaptador entre el analizador léxico generado por JFlex
 * y el analizador sintáctico generado por CUP. Es necesario porque:
 * 
 * 1. COMPATIBILIDAD DE INTERFACES:
 *    - JFlex genera lexers con una interfaz específica
 *    - CUP espera un Scanner con una interfaz diferente
 *    - Este adaptador traduce entre ambas interfaces
 * 
 * 2. MANEJO DE TOKENS:
 *    - Procesa los tokens del lexer antes de enviarlos al parser
 *    - Maneja casos especiales como EOF y errores
 *    - Proporciona depuración y logging del flujo de tokens
 * 
 * 3. CONTROL DE FLUJO:
 *    - Gestiona el final del archivo (EOF)
 *    - Maneja errores del lexer de manera robusta
 *    - Facilita la depuración del proceso de análisis
 * 
 * RESPONSABILIDADES:
 * - Implementar la interfaz Scanner requerida por CUP
 * - Convertir tokens de JFlex al formato esperado por CUP
 * - Manejar condiciones especiales (EOF, errores)
 * - Proporcionar logging para depuración
 * ===============================================================================
 */

import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;

/**
 * ADAPTADOR ENTRE EL LEXER (JFLEX) Y EL PARSER (CUP)
 * 
 * Esta clase es crucial para la integración de las herramientas de generación
 * automática de compiladores, permitiendo que trabajen juntas de manera seamless.
 */
public class LexerAdapter implements Scanner {
    // COMPONENTES DEL ADAPTADOR
    private PythonLexer lexer;           // Analizador léxico generado por JFlex
    private Symbol currentSymbol = null; // Token actual siendo procesado
    private boolean debugMode = true;    // Flag para activar/desactivar depuración
    
    /**
     * CONSTRUCTOR DEL ADAPTADOR
     * 
     * Inicializa el adaptador con una instancia del lexer.
     * Este es el punto de conexión entre las dos fases del compilador.
     */
    public LexerAdapter(PythonLexer lexer) {
        this.lexer = lexer;
    }
      /**
     * MÉTODO PRINCIPAL DEL SCANNER - OBTENER SIGUIENTE TOKEN
     * 
     * Este método implementa la interfaz Scanner requerida por CUP.
     * Es llamado por el parser cada vez que necesita el siguiente token.
     * 
     * PROCESO:
     * 1. Obtiene el token del lexer
     * 2. Realiza logging para depuración
     * 3. Maneja casos especiales (EOF, errores)
     * 4. Retorna el token en el formato esperado por CUP
     */
    @Override
    public Symbol next_token() throws Exception {
        try {
            // ===================================================================
            // OBTENCIÓN DEL TOKEN DEL ANALIZADOR LÉXICO
            // ===================================================================
            currentSymbol = lexer.yylex(); // Llamada al lexer generado por JFlex
            
            // ===================================================================
            // LOGGING Y DEPURACIÓN DEL FLUJO DE TOKENS
            // ===================================================================
            if (debugMode && currentSymbol != null) {
                // Obtener nombre del tipo de token para logging
                String tokenName = "UNKNOWN";
                try {
                    tokenName = sym.terminalNames[currentSymbol.sym];
                } catch (Exception e) {
                    // Ignora errores de índice de array
                }
                
                // Extraer valor del token para logging
                String tokenValue = currentSymbol.value != null ? 
                    (currentSymbol.value instanceof Token ? 
                        ((Token)currentSymbol.value).valor.toString() : 
                        currentSymbol.value.toString()) : 
                    "null";
                
                // Log del token (comentado para reducir verbosidad)
//                System.out.println("DEBUG TOKEN: " + tokenName + " - Value: " + tokenValue);
            }
            
            // ===================================================================
            // MANEJO DE FINAL DE ARCHIVO Y TOKENS NULOS
            // ===================================================================
            if (currentSymbol == null || currentSymbol.value == null) {
//                if (debugMode) System.out.println("DEBUG: Returning EOF token");
                return new Symbol(sym.EOF); // Token EOF para indicar fin de entrada
            }
            
            // Retorna el token para procesamiento por el parser
            return currentSymbol;
            
        } catch (Exception e) {
            // ===================================================================
            // MANEJO DE ERRORES EN EL ADAPTADOR
            // ===================================================================
            System.err.println("❌ Error en LexerAdapter: " + e.getMessage());
            e.printStackTrace();
            // Retorna símbolo de error para que el parser pueda manejarlo
            return new Symbol(sym.error);
        }
    }
}
