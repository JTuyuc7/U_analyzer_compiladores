/*
 * ===============================================================================
 * CLASE TOKEN - REPRESENTACIÓN DE UNIDADES LÉXICAS
 * ===============================================================================
 * 
 * FASE DEL COMPILADOR: ANÁLISIS LÉXICO (Fase 1)
 * 
 * Esta clase encapsula toda la información de un token generado por el
 * analizador léxico. Un token representa una unidad léxica significativa
 * del código fuente.
 * 
 * ATRIBUTOS DE UN TOKEN:
 * 1. tipo: Categoría del token (KEYWORD, OPERATOR, IDENTIFIER, etc.)
 * 2. valor: Contenido específico del token (ej: "def", 42, "variable_name")
 * 3. linea: Posición de línea en el código fuente (para errores y depuración)
 * 4. columna: Posición de columna en el código fuente
 * 5. color: Color para resaltado de sintaxis en la interfaz gráfica
 * 
 * IMPORTANCIA EN EL COMPILADOR:
 * - Los tokens son la salida del análisis léxico
 * - Son la entrada para el análisis sintáctico
 * - Contienen información crucial para el reporte de errores
 * - Facilitan el resaltado de sintaxis
 * ===============================================================================
 */

import java.awt.Color;

/**
 * REPRESENTACIÓN DE UN TOKEN EN EL ANÁLISIS LÉXICO
 * 
 * Esta clase actúa como un contenedor de datos que transfiere información
 * entre las fases del compilador, particularmente del lexer al parser.
 */
public class Token {
    // ATRIBUTOS INMUTABLES DEL TOKEN
    public final String tipo;     // Clasificación del token (ej: "KEYWORD", "OPERATOR")
    public final Object valor;    // Valor específico (ej: "def", 5, "variable")
    public final int linea;       // Número de línea (base 1)
    public final int columna;     // Número de columna (base 1)
    public final java.awt.Color color; // Color para interfaz gráfica

    /**
     * CONSTRUCTOR DEL TOKEN
     * 
     * Crea un nuevo token con todos sus atributos. Este constructor es
     * llamado por el analizador léxico cada vez que reconoce un patrón.
     */
    public Token(String tipo, Object valor, int linea, int columna, Color color) {
        this.tipo = tipo;
        this.valor = valor;
        this.linea = linea;
        this.columna = columna;
        this.color = color;
    }    // ===================================================================
    // MÉTODOS DE ACCESO (GETTERS)
    // ===================================================================
    // Proporcionan acceso controlado a los atributos del token
    
    /**
     * OBTENER TIPO DE TOKEN
     * Retorna la categoría del token (KEYWORD, OPERATOR, etc.)
     */
    public String getType() {
        return tipo;
    }

    /**
     * OBTENER VALOR DEL TOKEN
     * Retorna el contenido específico del token como cadena
     */
    public String getValue() {
        return valor.toString();
    }

    /**
     * OBTENER LÍNEA DEL TOKEN
     * Retorna el número de línea donde aparece el token
     */
    public int getLine() {
        return linea;
    }

    /**
     * OBTENER COLUMNA DEL TOKEN
     * Retorna el número de columna donde aparece el token
     */
    public int getColumn() {
        return columna;
    }

    /**
     * OBTENER COLOR DEL TOKEN
     * Retorna el color asignado para resaltado de sintaxis
     */
    public Color getColor() {
        return color;
    }

    // ===================================================================
    // MÉTODOS DE UTILIDAD
    // ===================================================================
    
    /**
     * VERIFICAR SI ES TOKEN DE ERROR
     * Determina si este token representa un error léxico
     */
    public boolean isError() {
        return tipo.equals("ERROR");
    }

    /**
     * REPRESENTACIÓN EN CADENA DEL TOKEN
     * Proporciona una representación legible para depuración y logs
     */
    public String toString() {
        return tipo + " → " + valor + " (" + linea + "," + columna + ")";
    }
}
