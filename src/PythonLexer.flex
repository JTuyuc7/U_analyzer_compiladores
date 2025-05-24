/*
 * ===============================================================================
 * ANALIZADOR LÉXICO PARA PYTHON - FASE 1 DEL COMPILADOR
 * ===============================================================================
 * 
 * ANÁLISIS LÉXICO (Lexical Analysis):
 * 
 * Esta es la PRIMERA FASE del compilador. Su función principal es convertir
 * el código fuente (secuencia de caracteres) en una secuencia de tokens.
 * 
 * RESPONSABILIDADES DEL ANALIZADOR LÉXICO:
 * 1. Reconocer patrones léxicos (palabras clave, operadores, identificadores)
 * 2. Eliminar espacios en blanco y comentarios
 * 3. Detectar errores léxicos (caracteres inválidos)
 * 4. Asignar categorías y atributos a cada token
 * 5. Proporcionar información de posición (línea, columna)
 * 6. Asignar colores para resaltado de sintaxis
 * 
 * ENTRADA: Código fuente como flujo de caracteres
 * SALIDA: Secuencia de tokens con sus atributos
 * 
 * HERRAMIENTA: JFlex - Generador de analizadores léxicos para Java
 * ===============================================================================
 */

import java_cup.runtime.*;
import java.awt.Color;

%%

/*
 * CONFIGURACIÓN DEL ANALIZADOR LÉXICO
 * 
 * %class PythonLexer    - Nombre de la clase generada
 * %unicode              - Soporte para caracteres Unicode
 * %line                 - Contador de líneas automático (yyline)
 * %column               - Contador de columnas automático (yycolumn)
 * %type Symbol          - Tipo de retorno de los métodos de análisis
 */
%class PythonLexer
%unicode
%line
%column
%type Symbol

%{
    /*
     * MÉTODO DE CREACIÓN DE TOKENS
     * 
     * Este método encapsula la creación de tokens con todos sus atributos:
     * - Tipo de símbolo (para el parser)
     * - Tipo de token (para clasificación)
     * - Valor del token
     * - Posición en el código fuente (línea, columna)
     * - Color para resaltado de sintaxis
     */
    private Symbol token(int symType, String tipo, Object valor, Color color) {
        Token t = new Token(tipo, valor, yyline + 1, yycolumn + 1, color);
        return new Symbol(symType, t);
    }

    /*
     * PALETA DE COLORES PARA RESALTADO DE SINTAXIS
     * 
     * Cada tipo de token tiene un color específico para facilitar
     * la lectura y comprensión del código:
     */
    private static final Color KEYWORD_COLOR   = new Color(147, 199, 99);  // Verde claro - palabras clave
    private static final Color OPERATOR_COLOR  = new Color(103, 140, 177); // Azul - operadores
    private static final Color STRING_COLOR    = new Color(214, 157, 133); // Naranja - cadenas de texto
    private static final Color COMMENT_COLOR   = new Color(128, 128, 128); // Gris - comentarios
    private static final Color NUMBER_COLOR    = new Color(122,  30, 183); // Púrpura - números
    private static final Color DELIMITER_COLOR = new Color(103, 140, 177); // Azul - delimitadores
    private static final Color BOOLEAN_COLOR   = new Color(147, 199, 99);  // Verde - valores booleanos
    private static final Color ERROR_COLOR     = new Color(229,  84,  81); // Rojo - errores
    private static final Color DEFAULT_COLOR   = new Color(204, 204, 204); // Gris claro - por defecto
%}

/*
 * ===============================================================================
 * DEFINICIONES DE PATRONES LÉXICOS
 * ===============================================================================
 * 
 * Aquí se definen las expresiones regulares que el analizador léxico
 * utiliza para reconocer diferentes tipos de tokens en el código fuente.
 * 
 * CATEGORÍAS DE TOKENS:
 * 1. Elementos estructurales (espacios, comentarios, terminadores)
 * 2. Identificadores y palabras reservadas
 * 3. Literales numéricos (enteros, flotantes, científicos)
 * 4. Literales de cadena
 * 5. Operadores (aritméticos, lógicos, relacionales)
 * 6. Delimitadores (paréntesis, corchetes, llaves, etc.)
 * ===============================================================================
 */

// ----------------------------
// ELEMENTOS ESTRUCTURALES
// ----------------------------
LineTerminator    = \r|\n|\r\n                    // Terminadores de línea
WhiteSpace        = [ \t\f]+                       // Espacios en blanco (ignorados)
Comment           = "#" [^\r\n]* ({LineTerminator})? // Comentarios de línea

// ----------------------------
// IDENTIFICADORES Y PALABRAS CLAVE
// ----------------------------
ValidIdentifier   = [a-zA-Z_][a-zA-Z0-9_]*        // Identificadores válidos (variables, funciones)

// ----------------------------
// LITERALES NUMÉRICOS
// ----------------------------
Integer           = 0|[1-9][0-9]*                 // Números enteros
Float             = {Integer}\.[0-9]+|\.[0-9]+    // Números de punto flotante
Scientific        = ({Integer}|{Float})([eE][-+]?[0-9]+)? // Notación científica

// ----------------------------
// LITERALES DE CADENA
// ----------------------------
StringCharacter   = [^\"\\\\\n\r]                 // Caracteres válidos en cadenas
EscapedChar       = \\.                            // Caracteres de escape
String            = \"({StringCharacter}|{EscapedChar})\"* // Cadenas de texto

// ----------------------------
// PALABRAS RESERVADAS DEL LENGUAJE PYTHON
// ----------------------------
Keyword           = "and"|"as"|"assert"|"break"|"class"|"continue"|"def"|"del"|"elif"|"else"|"except"|"False"|"finally"|"for"|"from"|"global"|"if"|"import"|"in"|"is"|"lambda"|"None"|"nonlocal"|"not"|"or"|"pass"|"raise"|"return"|"True"|"try"|"while"|"with"|"yield"|"async"|"await"

// ----------------------------
// OPERADORES
// ----------------------------
Operator          = "=="|"!="|"<="|">="|"<<"|">>"|"\*\*"|"//"|"<"|">"|"="|"\+"|"-"|"\*"|"/"|"%"|"&"|"\|"|"\^"|"~"

// ----------------------------
// DELIMITADORES
// ----------------------------
Delimiter         = "->"|"\("|"\)"|"\["|"\]"|"\{"|"\}"|":"|","|"\."|";"|"@"

%%

/*
 * ===============================================================================
 * REGLAS DE RECONOCIMIENTO DE TOKENS
 * ===============================================================================
 * 
 * Estas reglas definen qué hacer cuando el analizador léxico encuentra
 * cada patrón definido anteriormente. Cada regla:
 * 
 * 1. Especifica el patrón a reconocer
 * 2. Ejecuta una acción (crear un token)
 * 3. Asigna el tipo de símbolo correspondiente
 * 4. Establece el color para resaltado de sintaxis
 * 
 * PROCESO DE RECONOCIMIENTO:
 * - El lexer lee caracteres del código fuente
 * - Intenta hacer coincidir con los patrones definidos
 * - Cuando encuentra una coincidencia, ejecuta la acción correspondiente
 * - Retorna un Symbol que contiene el token creado
 * ===============================================================================
 */

// ----------------------------
// PALABRAS CLAVE DE PYTHON
// ----------------------------
// Las palabras reservadas del lenguaje tienen precedencia sobre los identificadores
{Keyword}    { return token(sym.KEYWORD,   "KEYWORD",   yytext(), KEYWORD_COLOR); }

// ----------------------------
// LITERALES BOOLEANOS Y NULOS
// ----------------------------
// Valores especiales del lenguaje (también son keywords pero con tratamiento especial)
"True"      { return token(sym.BOOLEAN,   "BOOLEAN",   Boolean.TRUE, BOOLEAN_COLOR); }
"False"     { return token(sym.BOOLEAN,   "BOOLEAN",   Boolean.FALSE, BOOLEAN_COLOR); }
"None"      { return token(sym.NONE,      "NONE",      null,          BOOLEAN_COLOR); }

// ----------------------------
// OPERADORES ARITMÉTICOS Y LÓGICOS
// ----------------------------
// Operadores de comparación
"=="        { return token(sym.EQ,        "OPERATOR", yytext(), OPERATOR_COLOR); }
"!="        { return token(sym.NEQ,       "OPERATOR", yytext(), OPERATOR_COLOR); }
"<="        { return token(sym.LE,        "OPERATOR", yytext(), OPERATOR_COLOR); }
">="        { return token(sym.GE,        "OPERATOR", yytext(), OPERATOR_COLOR); }
"<"         { return token(sym.LT,        "OPERATOR", yytext(), OPERATOR_COLOR); }
">"         { return token(sym.GT,        "OPERATOR", yytext(), OPERATOR_COLOR); }

// Operadores bit a bit
"<<"        { return token(sym.LSH,       "OPERATOR", yytext(), OPERATOR_COLOR); }
">>"        { return token(sym.RSH,       "OPERATOR", yytext(), OPERATOR_COLOR); }
"&"         { return token(sym.BITAND,    "OPERATOR", yytext(), OPERATOR_COLOR); }
"|"         { return token(sym.BITOR,     "OPERATOR", yytext(), OPERATOR_COLOR); }
"^"         { return token(sym.BITXOR,    "OPERATOR", yytext(), OPERATOR_COLOR); }
"~"         { return token(sym.BITNOT,    "OPERATOR", yytext(), OPERATOR_COLOR); }

// Operadores aritméticos
"**"        { return token(sym.POW,       "OPERATOR", yytext(), OPERATOR_COLOR); }
"//"        { return token(sym.FLOORDIV,  "OPERATOR", yytext(), OPERATOR_COLOR); }
"="         { return token(sym.ASSIGN,    "OPERATOR", yytext(), OPERATOR_COLOR); }
"+"         { return token(sym.PLUS,      "OPERATOR", yytext(), OPERATOR_COLOR); }
"-"         { return token(sym.MINUS,     "OPERATOR", yytext(), OPERATOR_COLOR); }
"*"         { return token(sym.MULT,      "OPERATOR", yytext(), OPERATOR_COLOR); }
"/"         { return token(sym.DIV,       "OPERATOR", yytext(), OPERATOR_COLOR); }
"%"         { return token(sym.MOD,       "OPERATOR", yytext(), OPERATOR_COLOR); }

// ----------------------------
// DELIMITADORES Y PUNTUACIÓN
// ----------------------------
"->"        { return token(sym.ARROW,     "DELIMITER", yytext(), DELIMITER_COLOR); }
"("         { return token(sym.LPAREN,   "DELIMITER", yytext(), DELIMITER_COLOR); }
")"         { return token(sym.RPAREN,   "DELIMITER", yytext(), DELIMITER_COLOR); }
"["         { return token(sym.LBRACK,   "DELIMITER", yytext(), DELIMITER_COLOR); }
"]"         { return token(sym.RBRACK,   "DELIMITER", yytext(), DELIMITER_COLOR); }
"{"         { return token(sym.LBRACE,   "DELIMITER", yytext(), DELIMITER_COLOR); }
"}"         { return token(sym.RBRACE,   "DELIMITER", yytext(), DELIMITER_COLOR); }
":"         { return token(sym.COLON,    "DELIMITER", yytext(), DELIMITER_COLOR); }
","         { return token(sym.COMMA,    "DELIMITER", yytext(), DELIMITER_COLOR); }
"."         { return token(sym.DOT,      "DELIMITER", yytext(), DELIMITER_COLOR); }
";"         { return token(sym.SEMI,     "DELIMITER", yytext(), DELIMITER_COLOR); }
"@"         { return token(sym.AT,       "DELIMITER", yytext(), DELIMITER_COLOR); }

// ----------------------------
// LITERALES NUMÉRICOS
// ----------------------------
// Los números en notación científica tienen precedencia sobre otros patrones numéricos
{Scientific} { return token(sym.NUM,      "NUMBER",    Double.parseDouble(yytext()), NUMBER_COLOR); }

// ----------------------------
// LITERALES DE CADENA
// ----------------------------
{String}     { return token(sym.STRING,   "STRING",    yytext(), STRING_COLOR); }

// ----------------------------
// IDENTIFICADORES
// ----------------------------
// Variables, nombres de funciones, clases, etc.
{ValidIdentifier} { return token(sym.ID,    "IDENTIFIER", yytext(), DEFAULT_COLOR); }

// ----------------------------
// COMENTARIOS
// ----------------------------
{Comment}    { return token(sym.COMMENT,  "COMMENT",   yytext(), COMMENT_COLOR); }

// ----------------------------
// CONTROL DE ESTRUCTURA
// ----------------------------
{LineTerminator}  { return token(sym.NEWLINE, "NEWLINE", yytext(), DELIMITER_COLOR); }

// ----------------------------
// ESPACIOS EN BLANCO (IGNORADOS)
// ----------------------------
{WhiteSpace} { /* Los espacios en blanco se ignoran en el análisis léxico */ }

// ----------------------------
// MANEJO DE ERRORES LÉXICOS
// ----------------------------
// Identificadores inválidos (que empiezan con número)
[0-9]+[a-zA-Z_]+ { return token(sym.ERROR, "ERROR", "Identificador inválido: " + yytext(), ERROR_COLOR); }

// Cualquier carácter no reconocido
[^\s]         { return token(sym.ERROR,   "ERROR", "Carácter inválido: " + yytext(), ERROR_COLOR); }

// ----------------------------
// FIN DE ARCHIVO
// ----------------------------
<<EOF>>     { return new Symbol(sym.EOF); }
