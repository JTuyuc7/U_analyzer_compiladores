import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PythonParser {
    private String input;
    private List<String> syntaxErrors;
    private List<String> semanticErrors;

    public PythonParser(String input) {
        this.input = input;
        this.syntaxErrors = new ArrayList<>();
        this.semanticErrors = new ArrayList<>();
    }

    public void parse() {
        // Split the input into lines
        String[] lines = input.split("\n");
        Stack<Integer> indentStack = new Stack<>();
        indentStack.push(0); // Initial indentation level is 0
        
        int lineNumber = 1;
        boolean expectingIndent = false;
        int currentBlockIndent = -1;

        for (String line : lines) {
            // Skip empty lines
            if (line.trim().isEmpty()) {
                lineNumber++;
                continue;
            }

            // Count leading spaces
            int indentLevel = countLeadingSpaces(line);
            
            // Check for indentation issues
            if (expectingIndent) {
                if (indentLevel <= indentStack.peek()) {
                    syntaxErrors.add(String.format("INDENTATION ERROR at line %d: Expected an indented block after ':'", lineNumber));
                }
                expectingIndent = false;
                currentBlockIndent = indentLevel;
            } else if (indentLevel > indentStack.peek()) {
                // Unexpected indentation increase
                if (currentBlockIndent != -1 && indentLevel != currentBlockIndent) {
                    syntaxErrors.add(String.format("INDENTATION ERROR at line %d: Inconsistent indentation level", lineNumber));
                }
            } else if (indentLevel < indentStack.peek()) {
                // Handle dedent - make sure it aligns with a previous level
                boolean validDedent = false;
                Stack<Integer> tempStack = new Stack<>();
                
                while (!indentStack.isEmpty()) {
                    int level = indentStack.pop();
                    if (level == indentLevel) {
                        validDedent = true;
                        break;
                    }
                    tempStack.push(level);
                }
                
                // If not a valid dedent, report error
                if (!validDedent) {
                    syntaxErrors.add(String.format("INDENTATION ERROR at line %d: Invalid dedent level", lineNumber));
                    // Restore stack
                    while (!tempStack.isEmpty()) {
                        indentStack.push(tempStack.pop());
                    }
                }
                
                // Update current block indent if needed
                if (indentStack.size() <= 1) {
                    currentBlockIndent = -1;
                } else if (!indentStack.isEmpty()) {
                    currentBlockIndent = indentStack.peek();
                }
            }
            
            // Check if this line ends with a colon (potential new block)
            if (line.trim().endsWith(":")) {
                expectingIndent = true;
                indentStack.push(indentLevel);
            }

            // Check for inconsistent indentation within the same block
            if (!expectingIndent && currentBlockIndent != -1 && 
                indentLevel > indentStack.peek() && indentLevel != currentBlockIndent) {
                syntaxErrors.add(String.format("INDENTATION ERROR at line %d: Indentation doesn't match block level", lineNumber));
            }
            
            lineNumber++;
        }
    }
    
    private int countLeadingSpaces(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') {
                count++;
            } else if (c == '\t') {
                count += 4; // Standard Python tab width
            } else {
                break;
            }
        }
        return count;
    }

    public List<String> getSyntaxErrors() {
        return syntaxErrors;
    }

    public List<String> getSemanticErrors() {
        return semanticErrors;
    }
}
