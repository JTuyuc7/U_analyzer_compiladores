import javax.swing.text.*;

public class SyntaxHighlighter {
    private static final StyleContext styleContext = StyleContext.getDefaultStyleContext();
    
    public static void highlight(StyledDocument doc, Token token) {
        try {
            // Calculate the position in the document based on line and column
            Element root = doc.getDefaultRootElement();
            int pos = root.getElement(token.getLine() - 1).getStartOffset() + token.getColumn() - 1;
            
            // Create attribute set with the token's color
            AttributeSet style = styleContext.addAttribute(
                styleContext.getEmptySet(), 
                StyleConstants.Foreground, 
                token.getColor()
            );
            
            // Apply the style to the token
            doc.setCharacterAttributes(pos, token.getValue().length(), style, true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
