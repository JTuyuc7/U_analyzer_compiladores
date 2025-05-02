# Python Analyzer v1

A Java application for analyzing Python code, including lexical, syntactic, and semantic validation.

## Features

- Lexical analysis: Tokenizes Python code and identifies lexical errors
- Syntax highlighting: Displays Python code with syntax highlighting
- Syntactic validation: Checks for proper Python syntax
- Semantic validation:
  - Type checking: Validates that operands in operations have compatible types
  - Indentation validation: Checks for proper Python indentation (4 spaces per level)

## Setup

1. Make sure you have Java installed on your system.
2. Update the `generate_parser.bat` file with the correct path to your CUP JAR file:
   ```
   set CUP_JAR=path\to\cup.jar
   ```
3. Run the `generate_parser.bat` file to generate the lexer and parser:
   ```
   generate_parser.bat
   ```
4. Compile the Java files:
   ```
   javac -cp lib/cup_runtime.jar src/*.java
   ```
5. Run the application:
   ```
   java -cp lib/cup_runtime.jar;. src.Main
   ```

## Usage

1. Enter Python code in the input area or load a Python file using the "Open File" button.
2. Click the "Analyze" button to analyze the code.
3. The application will display:
   - The code with syntax highlighting
   - Lexical errors (invalid tokens)
   - Syntax errors (invalid syntax)
   - Semantic errors (type mismatches, indentation issues)

## Example

Try analyzing the following Python code to see type checking in action:

```python
# Type checking example
x = 10
y = "hello"
z = x + y  # Type mismatch: cannot add int and string
```

And this example to see indentation validation:

```python
# Indentation validation example
def example():
    if True:
      print("Wrong indentation")  # Should be 8 spaces, not 6
    else:
        print("Correct indentation")
```

## Project Structure

- `src/Main.java`: The main application with GUI
- `src/PythonLexer.flex`: JFlex specification for the lexical analyzer
- `src/PythonLexer.java`: Generated lexer from JFlex
- `src/Python.cup`: CUP specification for the parser
- `src/parser.java`: Generated parser from CUP
- `src/sym.java`: Generated symbol constants from CUP
- `src/PythonParser.java`: Wrapper class for the parser
- `src/PythonSymbol.java`: Class for symbols with type information
- `src/Type.java`: Class for type checking
- `src/SymbolTable.java`: Class for tracking variables and their types
- `src/SyntaxHighlighter.java`: Class for syntax highlighting
- `src/Token.java`: Class for tokens

## Extending the Analyzer

You can extend the analyzer by:

1. Adding more grammar rules to `Python.cup`
2. Adding more type checking rules to `Type.java`
3. Adding more semantic validations to `Python.cup`
