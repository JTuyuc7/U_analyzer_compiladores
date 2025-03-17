# Python Analyzer v1

A lexical analyzer for Python with syntax highlighting and error reporting.

## Setup Instructions

1. Download required libraries:
   - Download [JFlex 1.8.2](https://www.jflex.de/release/jflex-1.8.2.jar) and save it to `lib/jflex-1.8.2.jar`
   - Download [CUP Runtime](https://www.jflex.de/release/cup_runtime.jar) and save it to `lib/cup_runtime.jar`

2. Generate the lexer:
   - Run `generate_lexer.bat` to generate the PythonLexer.java file from the flex specification

3. Open the project in IntelliJ IDEA:
   - The project should automatically recognize the libraries in the lib folder
   - If not, follow these steps to add the libraries manually:
     a. Right-click on the project root in the Project view
     b. Select "Open Module Settings" (F4)
     c. Go to the "Libraries" section
     d. Click the "+" button and choose "Java"
     e. Navigate to the project's "lib" folder and select both JAR files (jflex-1.8.2.jar and cup_runtime.jar)
     f. Click "OK" to add the selected files
     g. Make sure the library is added to your module, then click "Apply" and "OK"

## Features

- Syntax highlighting for Python code
- Real-time error detection
- Line and column information for errors
- Support for:
  - Keywords
  - Operators
  - Delimiters
  - Strings (including multi-line)
  - Numbers (integer, float, scientific notation)
  - Comments
  - Identifiers

## Usage

1. Enter or paste Python code in the top text area
2. Click "Analyze" to process the code
3. View the syntax-highlighted code in the middle panel
4. Check for any errors in the bottom panel

## Color Scheme

- Keywords: Green
- Operators: Blue
- Strings: Orange
- Comments: Gray
- Numbers: Light Blue
- Delimiters: Blue
- Booleans: Green
- Errors: Red
- Default: Light Gray

## Project Structure

- `src/Main.java`: Main application class with GUI implementation
- `src/Token.java`: Token class representing lexical tokens
- `src/PythonLexer.flex`: JFlex specification for Python lexical analysis
- `src/SyntaxHighlighter.java`: Syntax highlighting implementation
- `generate_lexer.bat`: Script to generate PythonLexer.java from the flex specification

## Future Improvements

- Add support for more advanced Python features (e.g., f-strings, decorators)
- Implement a parser for more comprehensive code analysis
- Add code completion suggestions
- Improve error reporting with more specific error messages
- Implement a dark mode toggle for the GUI

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

> [!NOTE] For general notes
> [!TIP] For helpful suggestions
> [!IMPORTANT] For critical information
> [!CAUTION] For potential risks
