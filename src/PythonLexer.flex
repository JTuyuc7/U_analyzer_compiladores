import java_cup.runtime.*;

%%

%class PythonLexer
%unicode
%line
%column
%type Token

%{
    private Token token(String type, String value, Color color) {
        return new Token(type, value, yyline + 1, yycolumn + 1, color);
    }

    private static final Color KEYWORD_COLOR = new Color(147, 199, 99);    // Green
    private static final Color OPERATOR_COLOR = new Color(103, 140, 177);  // Blue
    private static final Color STRING_COLOR = new Color(214, 157, 133);    // Orange
    private static final Color COMMENT_COLOR = new Color(128, 128, 128);   // Gray
    private static final Color NUMBER_COLOR = new Color(104, 151, 187);    // Light Blue
    private static final Color DELIMITER_COLOR = new Color(103, 140, 177); // Blue
    private static final Color BOOLEAN_COLOR = new Color(147, 199, 99);    // Green
    private static final Color ERROR_COLOR = new Color(229, 84, 81);       // Red
    private static final Color DEFAULT_COLOR = new Color(204, 204, 204);   // Light Gray
%}

// Whitespace and comments
LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]
Comment = "#" [^\r\n]* {LineTerminator}?

// Identifiers and invalid identifiers
ValidIdentifier = [a-zA-Z_][a-zA-Z0-9_]*
InvalidIdentifier = [0-9]+{ValidIdentifier} | {ValidIdentifier}"def" | "defe" | {ValidIdentifier}[0-9]+{ValidIdentifier}
InvalidKeyword = "defe"

// Numbers
Integer = 0 | [1-9][0-9]*
Float = {Integer}\.[0-9]+ | \.[0-9]+
Scientific = ({Integer}|{Float})[eE][-+]?[0-9]+

// Strings
StringCharacter = [^\r\n\"\\]
String = \"({StringCharacter})*\"
MultiLineString = \"\"\"[^\"]*\"\"\"

%%

// Keywords
"and"           { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"as"            { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"assert"        { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"break"         { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"class"         { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"continue"      { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"def"           { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"del"           { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"elif"          { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"else"          { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"except"        { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"False"         { return token("BOOLEAN", yytext(), BOOLEAN_COLOR); }
"finally"       { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"for"           { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"from"          { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"global"        { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"if"            { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"import"        { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"in"            { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"is"            { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"lambda"        { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"None"          { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"nonlocal"      { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"not"           { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"or"            { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"pass"          { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"raise"         { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"return"        { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"True"          { return token("BOOLEAN", yytext(), BOOLEAN_COLOR); }
"try"           { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"while"         { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"with"          { return token("KEYWORD", yytext(), KEYWORD_COLOR); }
"yield"         { return token("KEYWORD", yytext(), KEYWORD_COLOR); }

// Operators
"+"             { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"-"             { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"*"             { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"/"             { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"//"            { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"%"             { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"**"            { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"="             { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"+="            { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"-="            { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"*="            { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"/="            { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"//="           { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"%="            { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"**="           { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"=="            { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"!="            { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"<"             { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
">"             { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
"<="            { return token("OPERATOR", yytext(), OPERATOR_COLOR); }
">="            { return token("OPERATOR", yytext(), OPERATOR_COLOR); }

// Delimiters
"("             { return token("DELIMITER", yytext(), DELIMITER_COLOR); }
")"             { return token("DELIMITER", yytext(), DELIMITER_COLOR); }
"["             { return token("DELIMITER", yytext(), DELIMITER_COLOR); }
"]"             { return token("DELIMITER", yytext(), DELIMITER_COLOR); }
"{"             { return token("DELIMITER", yytext(), DELIMITER_COLOR); }
"}"             { return token("DELIMITER", yytext(), DELIMITER_COLOR); }
","             { return token("DELIMITER", yytext(), DELIMITER_COLOR); }
":"             { return token("DELIMITER", yytext(), DELIMITER_COLOR); }
"."             { return token("DELIMITER", yytext(), DELIMITER_COLOR); }
";"             { return token("DELIMITER", yytext(), DELIMITER_COLOR); }

// Literals
{Integer}       { return token("INTEGER", yytext(), NUMBER_COLOR); }
{Float}         { return token("FLOAT", yytext(), NUMBER_COLOR); }
{Scientific}    { return token("SCIENTIFIC", yytext(), NUMBER_COLOR); }
{String}        { return token("STRING", yytext(), STRING_COLOR); }
{MultiLineString} { return token("STRING", yytext(), STRING_COLOR); }

// Identifiers
{ValidIdentifier}    { return token("IDENTIFIER", yytext(), DEFAULT_COLOR); }
{InvalidIdentifier}  { return token("ERROR", "Invalid identifier: " + yytext(), ERROR_COLOR); }
{InvalidKeyword}     { return token("ERROR", "Did you mean 'def'?", ERROR_COLOR); }

// Comments
{Comment}       { return token("COMMENT", yytext(), COMMENT_COLOR); }

// Whitespace
{WhiteSpace}    { /* ignore */ }

// Error fallback
[^]             { return token("ERROR", "Invalid character: " + yytext(), ERROR_COLOR); }
