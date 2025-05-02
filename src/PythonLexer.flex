import java_cup.runtime.*;
import java.awt.Color;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;

%%

%class PythonLexer
%unicode
%line
%column
%cup
%type Symbol
%function scan

%{
    // Token colors for syntax highlighting
    private static final Color KEYWORD_COLOR = new Color(147, 199, 99);    // Green
    private static final Color OPERATOR_COLOR = new Color(103, 140, 177);  // Blue
    private static final Color STRING_COLOR = new Color(214, 157, 133);    // Orange
    private static final Color COMMENT_COLOR = new Color(128, 128, 128);   // Gray
    private static final Color NUMBER_COLOR = new Color(122, 30, 183);     // Purple
    private static final Color DELIMITER_COLOR = new Color(103, 140, 177); // Blue
    private static final Color BOOLEAN_COLOR = new Color(147, 199, 99);    // Green
    private static final Color ERROR_COLOR = new Color(229, 84, 81);       // Red
    private static final Color DEFAULT_COLOR = new Color(204, 204, 204);   // Light Gray
    
    // Indentation tracking
    private int currentIndent = 0;
    private Stack<Integer> indentStack = new Stack<>();
    private Queue<Symbol> indentationTokens = new LinkedList<>();
    private boolean atLineStart = true;
    
    // Symbol creation helper methods
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
    
    // Token creation for syntax highlighting
    private Token token(String type, String value, Color color) {
        return new Token(type, value, yyline + 1, yycolumn + 1, color);
    }
    
    // Method to handle indentation
    private Symbol handleIndentation(int spaces) {
        if (spaces > currentIndent) {
            // Indent
            indentStack.push(currentIndent);
            currentIndent = spaces;
            return symbol(sym.INDENT);
        } else if (spaces < currentIndent) {
            // Dedent
            int dedents = 0;
            while (!indentStack.isEmpty() && spaces < currentIndent) {
                currentIndent = indentStack.pop();
                dedents++;
            }
            
            // Queue up multiple DEDENT tokens if needed
            for (int i = 1; i < dedents; i++) {
                indentationTokens.add(symbol(sym.DEDENT));
            }
            
            return symbol(sym.DEDENT);
        }
        
        return null;
    }
    
    // Method to get the next token, handling indentation
    public Symbol next_token() throws java.io.IOException {
        // Check if we have queued indentation tokens
        if (!indentationTokens.isEmpty()) {
            return indentationTokens.poll();
        }

        // Get the next token from the lexer
        Symbol token = scan();

        // If we're at the end of the file, return EOF
        if (token == null) {
            // Generate DEDENT tokens for any remaining indentation levels
            if (currentIndent > 0) {
                currentIndent = 0;
                return symbol(sym.DEDENT);
            }
            return symbol(sym.EOF);
        }

        return token;
    }
    
    // Compatibility method for the original code
    public Token yylex() throws java.io.IOException {
        Symbol symbol = next_token();
        if (symbol == null || symbol.value == null) {
            return null;
        }
        
        // Extract the Token from the Symbol
        if (symbol.value instanceof Token) {
            return (Token) symbol.value;
        }
        
        // If the Symbol doesn't contain a Token, create a default one
        return token("UNKNOWN", symbol.toString(), DEFAULT_COLOR);
    }
%}

// Whitespace and comments
LineTerminator = \r|\n|\r\n
WhiteSpace = [ \t\f]
Comment = "#" [^\r\n]* {LineTerminator}?

// Identifiers
ValidIdentifier = [a-zA-Z_][a-zA-Z0-9_]*

// Numbers
Integer = 0 | [1-9][0-9]*
Float = {Integer}\.[0-9]+ | \.[0-9]+
Scientific = ({Integer}|{Float})[eE][-+]?[0-9]+

// Strings
StringCharacter = [^\"\\\n\r]
EscapedChar = \\.
String = \"({StringCharacter}|{EscapedChar})*\"
MultiLineString = \"\"\"([^\"\\]|\\.|\"|\"\")*\"\"\"

// Indentation
Indent = [ \t]*

// States
%state INDENT_CHECK

%%

// Handle indentation at the beginning of a line
<YYINITIAL> {
    {Indent} {
        if (atLineStart) {
            int spaces = yytext().length();
            Symbol indentToken = handleIndentation(spaces);
            atLineStart = false;
            if (indentToken != null) {
                return indentToken;
            }
        }
    }
    
    {LineTerminator} {
        atLineStart = true;
        return symbol(sym.NEWLINE);
    }
}

// Keywords
"and"           { return symbol(sym.AND, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"as"            { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"assert"        { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"break"         { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"class"         { return symbol(sym.CLASS, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"continue"      { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"def"           { return symbol(sym.DEF, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"del"           { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"elif"          { return symbol(sym.ELIF, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"else"          { return symbol(sym.ELSE, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"except"        { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"False"         { return symbol(sym.BOOLEAN, token("BOOLEAN", yytext(), BOOLEAN_COLOR)); }
"finally"       { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"for"           { return symbol(sym.FOR, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"from"          { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"global"        { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"if"            { return symbol(sym.IF, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"import"        { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"in"            { return symbol(sym.IN, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"is"            { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"lambda"        { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"None"          { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"nonlocal"      { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"not"           { return symbol(sym.NOT, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"or"            { return symbol(sym.OR, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"pass"          { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"print"         { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"raise"         { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"return"        { return symbol(sym.RETURN, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"True"          { return symbol(sym.BOOLEAN, token("BOOLEAN", yytext(), BOOLEAN_COLOR)); }
"try"           { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"while"         { return symbol(sym.WHILE, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"with"          { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }
"yield"         { return symbol(sym.IDENTIFIER, token("KEYWORD", yytext(), KEYWORD_COLOR)); }

// Operators
"+"             { return symbol(sym.PLUS, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"-"             { return symbol(sym.MINUS, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"*"             { return symbol(sym.TIMES, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"/"             { return symbol(sym.DIVIDE, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"//"            { return symbol(sym.IDENTIFIER, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"%"             { return symbol(sym.MODULO, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"**"            { return symbol(sym.POWER, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"="             { return symbol(sym.ASSIGN, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"+="            { return symbol(sym.IDENTIFIER, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"-="            { return symbol(sym.IDENTIFIER, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"*="            { return symbol(sym.IDENTIFIER, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"/="            { return symbol(sym.IDENTIFIER, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"//="           { return symbol(sym.IDENTIFIER, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"%="            { return symbol(sym.IDENTIFIER, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"**="           { return symbol(sym.IDENTIFIER, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"=="            { return symbol(sym.EQUALS, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"!="            { return symbol(sym.NOT_EQUALS, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"<"             { return symbol(sym.LESS, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
">"             { return symbol(sym.GREATER, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
"<="            { return symbol(sym.LESS_EQUALS, token("OPERATOR", yytext(), OPERATOR_COLOR)); }
">="            { return symbol(sym.GREATER_EQUALS, token("OPERATOR", yytext(), OPERATOR_COLOR)); }

// Delimiters
"("             { return symbol(sym.LPAREN, token("DELIMITER", yytext(), DELIMITER_COLOR)); }
")"             { return symbol(sym.RPAREN, token("DELIMITER", yytext(), DELIMITER_COLOR)); }
"["             { return symbol(sym.LBRACKET, token("DELIMITER", yytext(), DELIMITER_COLOR)); }
"]"             { return symbol(sym.RBRACKET, token("DELIMITER", yytext(), DELIMITER_COLOR)); }
"{"             { return symbol(sym.LBRACE, token("DELIMITER", yytext(), DELIMITER_COLOR)); }
"}"             { return symbol(sym.RBRACE, token("DELIMITER", yytext(), DELIMITER_COLOR)); }
","             { return symbol(sym.COMMA, token("DELIMITER", yytext(), DELIMITER_COLOR)); }
":"             { return symbol(sym.COLON, token("DELIMITER", yytext(), DELIMITER_COLOR)); }
"."             { return symbol(sym.DOT, token("DELIMITER", yytext(), DELIMITER_COLOR)); }
";"             { return symbol(sym.SEMICOLON, token("DELIMITER", yytext(), DELIMITER_COLOR)); }

// Literals
{Integer}           { return symbol(sym.INTEGER, token("INTEGER", yytext(), NUMBER_COLOR)); }
{Float}             { return symbol(sym.FLOAT, token("FLOAT", yytext(), NUMBER_COLOR)); }
{Scientific}        { return symbol(sym.FLOAT, token("SCIENTIFIC", yytext(), NUMBER_COLOR)); }
{String}            { return symbol(sym.STRING, token("STRING", yytext(), STRING_COLOR)); }
{MultiLineString}   { return symbol(sym.STRING, token("STRING", yytext(), STRING_COLOR)); }

// Identifiers
{ValidIdentifier}   { return symbol(sym.IDENTIFIER, token("IDENTIFIER", yytext(), DEFAULT_COLOR)); }

// Invalid identifiers (ej. 2var)
[0-9]+[a-zA-Z_]+     { return symbol(sym.error, token("ERROR", "Invalid identifier: " + yytext(), ERROR_COLOR)); }

// Comments
{Comment}           { return symbol(sym.COMMENT, token("COMMENT", yytext(), COMMENT_COLOR)); }

// Whitespace
{WhiteSpace}        { /* Ignore whitespace */ }

// Catch-all for unknown symbols
[^]                 { return symbol(sym.error, token("ERROR", "Invalid character: " + yytext(), ERROR_COLOR)); }
