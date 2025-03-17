@echo off
echo Generating lexer from PythonLexer.flex...
java -jar lib/jflex-1.8.2.jar src/PythonLexer.flex
echo Done!
pause
