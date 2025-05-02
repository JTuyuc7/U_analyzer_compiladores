@echo off
echo Generating Python parser...

REM Set paths to the JFlex and CUP JAR files
set JFLEX_JAR=lib\jflex-1.8.2.jar
REM Update this path to point to your CUP JAR file
set CUP_JAR=path\to\cup.jar
set CUP_RUNTIME_JAR=lib\cup_runtime.jar

REM Generate the lexer
echo Generating lexer...
java -jar %JFLEX_JAR% src\PythonLexer.flex

REM Generate the parser
echo Generating parser...
java -jar %CUP_JAR% -package src -parser parser -symbols sym -nowarn src\Python.cup

REM Move the generated files to the src directory
echo Moving generated files...
if exist parser.java move parser.java src\
if exist sym.java move sym.java src\

echo Done!
echo.
echo IMPORTANT: Before running this batch file, make sure to update the CUP_JAR path
echo to point to your CUP JAR file location.
