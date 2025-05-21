import java_cup.runtime.*;
import java.awt.Color;

%%

%class PythonLexer
%unicode
%line
%column
%type Symbol

%{
    // Token factory
    private Symbol token(int symType, String tipo, Object valor, Color color) {
        Token t = new Token(tipo, valor, yyline + 1, yycolumn + 1, color);
        return new Symbol(symType, t);
    }

    private static final Color KEYWORD_COLOR   = new Color(147, 199, 99);
    private static final Color OPERATOR_COLOR  = new Color(103, 140, 177);
    private static final Color STRING_COLOR    = new Color(214, 157, 133);
    private static final Color COMMENT_COLOR   = new Color(128, 128, 128);
    private static final Color NUMBER_COLOR    = new Color(122,  30, 183);
    private static final Color DELIMITER_COLOR = new Color(103, 140, 177);
    private static final Color BOOLEAN_COLOR   = new Color(147, 199, 99);
    private static final Color ERROR_COLOR     = new Color(229,  84,  81);
    private static final Color DEFAULT_COLOR   = new Color(204, 204, 204);
%}

// ----------------------------
// Definitions
// ----------------------------
LineTerminator    = \r|\n|\r\n
WhiteSpace        = [ \t\f]+
Comment           = "#" [^\r\n]* ({LineTerminator})?
ValidIdentifier   = [a-zA-Z_][a-zA-Z0-9_]*
Integer           = 0|[1-9][0-9]*
Float             = {Integer}\.[0-9]+|\.[0-9]+
Scientific        = ({Integer}|{Float})([eE][-+]?[0-9]+)?
StringCharacter   = [^\"\\\\\n\r]
EscapedChar       = \\.
String            = \"({StringCharacter}|{EscapedChar})*\"

// Keyword, operator and delimiter definitions
Keyword           = "and"|"as"|"assert"|"break"|"class"|"continue"|"def"|"del"|"elif"|"else"|"except"|"False"|"finally"|"for"|"from"|"global"|"if"|"import"|"in"|"is"|"lambda"|"None"|"nonlocal"|"not"|"or"|"pass"|"raise"|"return"|"True"|"try"|"while"|"with"|"yield"|"async"|"await"
Operator          = "=="|"!="|"<="|">="|"<<"|">>"|"\*\*"|"//"|"<"|">"|"="|"\+"|"-"|"\*"|"/"|"%"|"&"|"\|"|"\^"|"~"
Delimiter         = "->"|"\("|"\)"|"\["|"\]"|"\{"|"\}"|":"|","|"\."|";"|"@"

%%

// ----------------------------
// Rules
// ----------------------------

// Keywords (all colored the same)
{Keyword}    { return token(sym.KEYWORD,   "KEYWORD",   yytext(), KEYWORD_COLOR); }

// Boolean literals (also keywords but highlighted for clarity)
"True"      { return token(sym.BOOLEAN,   "BOOLEAN",   Boolean.TRUE, BOOLEAN_COLOR); }
"False"     { return token(sym.BOOLEAN,   "BOOLEAN",   Boolean.FALSE, BOOLEAN_COLOR); }
"None"      { return token(sym.NONE,      "NONE",      null,          BOOLEAN_COLOR); }

// Operators
"=="        { return token(sym.EQ,        "OPERATOR", yytext(), OPERATOR_COLOR); }
"!="        { return token(sym.NEQ,       "OPERATOR", yytext(), OPERATOR_COLOR); }
"<="        { return token(sym.LE,        "OPERATOR", yytext(), OPERATOR_COLOR); }
">="        { return token(sym.GE,        "OPERATOR", yytext(), OPERATOR_COLOR); }
"<<"        { return token(sym.LSH,       "OPERATOR", yytext(), OPERATOR_COLOR); }
">>"        { return token(sym.RSH,       "OPERATOR", yytext(), OPERATOR_COLOR); }
"**"        { return token(sym.POW,       "OPERATOR", yytext(), OPERATOR_COLOR); }
"//"        { return token(sym.FLOORDIV,  "OPERATOR", yytext(), OPERATOR_COLOR); }
"<"         { return token(sym.LT,        "OPERATOR", yytext(), OPERATOR_COLOR); }
">"         { return token(sym.GT,        "OPERATOR", yytext(), OPERATOR_COLOR); }
"="         { return token(sym.ASSIGN,    "OPERATOR", yytext(), OPERATOR_COLOR); }
"+"         { return token(sym.PLUS,      "OPERATOR", yytext(), OPERATOR_COLOR); }
"-"         { return token(sym.MINUS,     "OPERATOR", yytext(), OPERATOR_COLOR); }
"*"         { return token(sym.MULT,      "OPERATOR", yytext(), OPERATOR_COLOR); }
"/"         { return token(sym.DIV,       "OPERATOR", yytext(), OPERATOR_COLOR); }
"%"         { return token(sym.MOD,       "OPERATOR", yytext(), OPERATOR_COLOR); }
"&"         { return token(sym.BITAND,    "OPERATOR", yytext(), OPERATOR_COLOR); }
"|"         { return token(sym.BITOR,     "OPERATOR", yytext(), OPERATOR_COLOR); }
"^"         { return token(sym.BITXOR,    "OPERATOR", yytext(), OPERATOR_COLOR); }
"~"         { return token(sym.BITNOT,    "OPERATOR", yytext(), OPERATOR_COLOR); }

// Delimiters
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

// Numeric literals
{Scientific} { return token(sym.NUM,      "NUMBER",    Double.parseDouble(yytext()), NUMBER_COLOR); }

// String literals
{String}     { return token(sym.STRING,   "STRING",    yytext(), STRING_COLOR); }

// Identifiers
{ValidIdentifier} { return token(sym.ID,    "IDENTIFIER", yytext(), DEFAULT_COLOR); }

// Comments
{Comment}    { return token(sym.COMMENT,  "COMMENT",   yytext(), COMMENT_COLOR); }

{LineTerminator}  { return token(sym.NEWLINE, "NEWLINE", yytext(), DELIMITER_COLOR); }

// Whitespace (ignored)
{WhiteSpace} { /* skip */ }

// Invalid identifiers and characters
[0-9]+[a-zA-Z_]+ { return token(sym.ERROR, "ERROR", "Invalid identifier: " + yytext(), ERROR_COLOR); }
[^\s]         { return token(sym.ERROR,   "ERROR", "Invalid char: " + yytext(), ERROR_COLOR); }

// End of file
<<EOF>>     { return new Symbol(sym.EOF); }
