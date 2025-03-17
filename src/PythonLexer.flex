import java_cup.runtime.*;

%%

%class PythonLexer
%unicode
%line
%column
%type Token

%{
    private Token token(String type, String value) {
        return new Token(type, value, yyline + 1, yycolumn + 1);
    }
%}

// Whitespace and comments
LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]
Comment = "#" [^\r\n]* {LineTerminator}?

// Identifiers and invalid identifiers
ValidIdentifier = [a-zA-Z_][a-zA-Z0-9_]*
InvalidIdentifier = [0-9]+{ValidIdentifier} | {ValidIdentifier}"def" | "defe" | {ValidIdentifier}[0-9]+{ValidIdentifier}

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
"and"           { return token("KEYWORD", yytext()); }
"as"            { return token("KEYWORD", yytext()); }
"assert"        { return token("KEYWORD", yytext()); }
"break"         { return token("KEYWORD", yytext()); }
"class"         { return token("KEYWORD", yytext()); }
"continue"      { return token("KEYWORD", yytext()); }
"def"           { return token("KEYWORD", yytext()); }
"del"           { return token("KEYWORD", yytext()); }
"elif"          { return token("KEYWORD", yytext()); }
"else"          { return token("KEYWORD", yytext()); }
"except"        { return token("KEYWORD", yytext()); }
"False"         { return token("BOOLEAN", yytext()); }
"finally"       { return token("KEYWORD", yytext()); }
"for"           { return token("KEYWORD", yytext()); }
"from"          { return token("KEYWORD", yytext()); }
"global"        { return token("KEYWORD", yytext()); }
"if"            { return token("KEYWORD", yytext()); }
"import"        { return token("KEYWORD", yytext()); }
"in"            { return token("KEYWORD", yytext()); }
"is"            { return token("KEYWORD", yytext()); }
"lambda"        { return token("KEYWORD", yytext()); }
"None"          { return token("KEYWORD", yytext()); }
"nonlocal"      { return token("KEYWORD", yytext()); }
"not"           { return token("KEYWORD", yytext()); }
"or"            { return token("KEYWORD", yytext()); }
"pass"          { return token("KEYWORD", yytext()); }
"raise"         { return token("KEYWORD", yytext()); }
"return"        { return token("KEYWORD", yytext()); }
"True"          { return token("BOOLEAN", yytext()); }
"try"           { return token("KEYWORD", yytext()); }
"while"         { return token("KEYWORD", yytext()); }
"with"          { return token("KEYWORD", yytext()); }
"yield"         { return token("KEYWORD", yytext()); }

// Operators
"+"             { return token("OPERATOR", yytext()); }
"-"             { return token("OPERATOR", yytext()); }
"*"             { return token("OPERATOR", yytext()); }
"/"             { return token("OPERATOR", yytext()); }
"//"            { return token("OPERATOR", yytext()); }
"%"             { return token("OPERATOR", yytext()); }
"**"            { return token("OPERATOR", yytext()); }
"="             { return token("OPERATOR", yytext()); }
"+="            { return token("OPERATOR", yytext()); }
"-="            { return token("OPERATOR", yytext()); }
"*="            { return token("OPERATOR", yytext()); }
"/="            { return token("OPERATOR", yytext()); }
"//="           { return token("OPERATOR", yytext()); }
"%="            { return token("OPERATOR", yytext()); }
"**="           { return token("OPERATOR", yytext()); }
"=="            { return token("OPERATOR", yytext()); }
"!="            { return token("OPERATOR", yytext()); }
"<"             { return token("OPERATOR", yytext()); }
">"             { return token("OPERATOR", yytext()); }
"<="            { return token("OPERATOR", yytext()); }
">="            { return token("OPERATOR", yytext()); }

// Delimiters
"("             { return token("DELIMITER", yytext()); }
")"             { return token("DELIMITER", yytext()); }
"["             { return token("DELIMITER", yytext()); }
"]"             { return token("DELIMITER", yytext()); }
"{"             { return token("DELIMITER", yytext()); }
"}"             { return token("DELIMITER", yytext()); }
","             { return token("DELIMITER", yytext()); }
":"             { return token("DELIMITER", yytext()); }
"."             { return token("DELIMITER", yytext()); }
";"             { return token("DELIMITER", yytext()); }

// Literals
{Integer}       { return token("INTEGER", yytext()); }
{Float}         { return token("FLOAT", yytext()); }
{Scientific}    { return token("SCIENTIFIC", yytext()); }
{String}        { return token("STRING", yytext()); }
{MultiLineString} { return token("STRING", yytext()); }

// Identifiers
{ValidIdentifier}    { return token("IDENTIFIER", yytext()); }
{InvalidIdentifier}  { return token("ERROR", yytext()); }

// Comments
{Comment}       { return token("COMMENT", yytext()); }

// Whitespace
{WhiteSpace}    { /* ignore */ }

// Error fallback
[^]             { return token("ERROR", yytext()); }
