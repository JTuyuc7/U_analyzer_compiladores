import java_cup.runtime.*;
import java.awt.Color;

%%

%class PythonLexer
%unicode
%line
%column
%type Symbol

//%implements java_cup.runtime.Scanner
//%function next_token
//%type java_cup.runtime.Symbol


%{
    // Clase Token personalizada
    private Symbol token(int symType, String tipo, Object valor, Color color) {
        Token t = new Token(tipo, valor, yyline + 1, yycolumn + 1, color);
        return new Symbol(symType, t);
    }

    private static final Color KEYWORD_COLOR = new Color(147, 199, 99);    // Green
    private static final Color OPERATOR_COLOR = new Color(103, 140, 177);  // Blue
    private static final Color STRING_COLOR = new Color(214, 157, 133);    // Orange
    private static final Color COMMENT_COLOR = new Color(128, 128, 128);   // Gray
    private static final Color NUMBER_COLOR = new Color(122, 30, 183);     // Purple
    private static final Color DELIMITER_COLOR = new Color(103, 140, 177); // Blue
    private static final Color BOOLEAN_COLOR = new Color(147, 199, 99);    // Green
    private static final Color ERROR_COLOR = new Color(229, 84, 81);       // Red
    private static final Color DEFAULT_COLOR = new Color(204, 204, 204);   // Light Gray
%}

// ----------------------------
// Definiciones léxicas
// ----------------------------
LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]
Comment = "#" [^\r\n]* {LineTerminator}?
ValidIdentifier = [a-zA-Z_][a-zA-Z0-9_]*
Integer = 0 | [1-9][0-9]*
Float = {Integer}\.[0-9]+ | \.[0-9]+
Scientific = ({Integer}|{Float})[eE][-+]?[0-9]+
StringCharacter = [^\"\\\n\r]
EscapedChar = \\.
String = \"({StringCharacter}|{EscapedChar})*\"


// ----------------------------
// Reglas
// ----------------------------
%%

// Palabras clave
"def"       { return token(sym.DEF, "KEYWORD", yytext(), KEYWORD_COLOR); }
"return"    { return token(sym.RETURN, "KEYWORD", yytext(), KEYWORD_COLOR); }
"if"        { return token(sym.IF, "KEYWORD", yytext(), KEYWORD_COLOR); }
"else"      { return token(sym.ELSE, "KEYWORD", yytext(), KEYWORD_COLOR); }

// Operadores
"="         { return token(sym.ASSIGN, "OPERATOR", yytext(), OPERATOR_COLOR); }
"+"         { return token(sym.PLUS, "OPERATOR", yytext(), OPERATOR_COLOR); }
"-"         { return token(sym.MINUS, "OPERATOR", yytext(), OPERATOR_COLOR); }
"*"         { return token(sym.MULT, "OPERATOR", yytext(), OPERATOR_COLOR); }
"/"         { return token(sym.DIV, "OPERATOR", yytext(), OPERATOR_COLOR); }

// Delimitadores
":"         { return token(sym.COLON, "DELIMITER", yytext(), DELIMITER_COLOR); }
"("         { return token(sym.LPAREN, "DELIMITER", yytext(), DELIMITER_COLOR); }
")"         { return token(sym.RPAREN, "DELIMITER", yytext(), DELIMITER_COLOR); }
"\n"        { return token(sym.NEWLINE, "DELIMITER", yytext(), DELIMITER_COLOR); }

// Literales
{Integer}       { return token(sym.NUM, "INTEGER", Integer.parseInt(yytext()), NUMBER_COLOR); }
{Float}         { return token(sym.FLOAT, "FLOAT", Double.parseDouble(yytext()), NUMBER_COLOR); }
{String}        { return token(sym.STRING, "STRING", yytext(), STRING_COLOR); }

// Identificadores
{ValidIdentifier} { return token(sym.ID, "IDENTIFIER", yytext(), DEFAULT_COLOR); }

// Comentarios
{Comment}       { return token(sym.COMMENT, "COMMENT", yytext(), COMMENT_COLOR); }

// Espacios
{WhiteSpace}    { /* ignorar */ }

// Error de identificador
[0-9]+[a-zA-Z_]+ { return token(sym.ERROR, "ERROR", "Invalid identifier: " + yytext(), ERROR_COLOR); }

// Caracter inválido
[^]             { return token(sym.ERROR, "ERROR", "Invalid character: " + yytext(), ERROR_COLOR); }

// Fin de archivo
<<EOF>>         { return new Symbol(sym.EOF); }
