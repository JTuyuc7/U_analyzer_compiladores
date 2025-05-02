import javax.swing.text.StyledDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleContext;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.Color;

public class SyntaxHighlighter {
    public static void highlight(StyledDocument doc, Token token) {
        try {
            // Calculate position in document
            int startLine = token.getLine() - 1;  // Adjust for 1-based line numbers
            int startCol = token.getColumn() - 1;  // Adjust for 1-based column numbers
            
            // Find the start position in the document
            String text = doc.getText(0, doc.getLength());
            String[] lines = text.split("\n", -1); // -1 to keep empty trailing lines
            
            // Calculate position - improved for accuracy
            int position = 0;
            for (int i = 0; i < startLine && i < lines.length; i++) {
                position += lines[i].length() + 1; // +1 for the newline character
            }
            
            // Make sure we don't go out of bounds
            if (startLine < lines.length) {
                position += Math.min(startCol, lines[startLine].length());
                
                // Apply the style
                Style style = doc.addStyle(token.getType(), null);
                StyleConstants.setForeground(style, token.getColor());
                
                // Calculate token length and make sure we don't exceed document bounds
                int length = Math.min(token.getValue().length(), 
                                     doc.getLength() - position);
                
                if (length > 0 && position >= 0 && position < doc.getLength()) {
                    doc.setCharacterAttributes(position, length, style, true);
                }
            }
        } catch (BadLocationException e) {
            System.err.println("Error highlighting token: " + e.getMessage());
        }
    }
}
