import javax.swing.text.*;
import java.awt.Color;

public class SyntaxHighlighter {
    private static final StyleContext styleContext = StyleContext.getDefaultStyleContext();
    
    // Define colors for different token types
    private static final Color KEYWORD_COLOR = new Color(147, 199, 99);    // Green
    private static final Color OPERATOR_COLOR = new Color(103, 140, 177);  // Blue
    private static final Color STRING_COLOR = new Color(214, 157, 133);    // Orange
    private static final Color COMMENT_COLOR = new Color(128, 128, 128);   // Gray
    private static final Color NUMBER_COLOR = new Color(104, 151, 187);    // Light Blue
    private static final Color DELIMITER_COLOR = new Color(103, 140, 177); // Blue
    private static final Color BOOLEAN_COLOR = new Color(147, 199, 99);    // Green
    private static final Color ERROR_COLOR = new Color(229, 84, 81);       // Red
    private static final Color DEFAULT_COLOR = new Color(204, 204, 204);   // Light Gray
    
    // Create attribute sets for each token type
    private static final AttributeSet KEYWORD_STYLE = styleContext.addAttribute(
            styleContext.getEmptySet(), StyleConstants.Foreground, KEYWORD_COLOR);
    private static final AttributeSet OPERATOR_STYLE = styleContext.addAttribute(
            styleContext.getEmptySet(), StyleConstants.Foreground, OPERATOR_COLOR);
    private static final AttributeSet STRING_STYLE = styleContext.addAttribute(
            styleContext.getEmptySet(), StyleConstants.Foreground, STRING_COLOR);
    private static final AttributeSet COMMENT_STYLE = styleContext.addAttribute(
            styleContext.getEmptySet(), StyleConstants.Foreground, COMMENT_COLOR);
    private static final AttributeSet NUMBER_STYLE = styleContext.addAttribute(
            styleContext.getEmptySet(), StyleConstants.Foreground, NUMBER_COLOR);
    private static final AttributeSet DELIMITER_STYLE = styleContext.addAttribute(
            styleContext.getEmptySet(), StyleConstants.Foreground, DELIMITER_COLOR);
    private static final AttributeSet BOOLEAN_STYLE = styleContext.addAttribute(
            styleContext.getEmptySet(), StyleConstants.Foreground, BOOLEAN_COLOR);
    private static final AttributeSet ERROR_STYLE = styleContext.addAttribute(
            styleContext.getEmptySet(), StyleConstants.Foreground, ERROR_COLOR);
    private static final AttributeSet DEFAULT_STYLE = styleContext.addAttribute(
            styleContext.getEmptySet(), StyleConstants.Foreground, DEFAULT_COLOR);
    
    public static AttributeSet getStyle(String tokenType) {
        return switch (tokenType) {
            case "KEYWORD" -> KEYWORD_STYLE;
            case "OPERATOR" -> OPERATOR_STYLE;
            case "STRING" -> STRING_STYLE;
            case "COMMENT" -> COMMENT_STYLE;
            case "INTEGER", "FLOAT", "SCIENTIFIC" -> NUMBER_STYLE;
            case "DELIMITER" -> DELIMITER_STYLE;
            case "BOOLEAN" -> BOOLEAN_STYLE;
            case "ERROR" -> ERROR_STYLE;
            default -> DEFAULT_STYLE;
        };
    }
    
    public static void highlight(StyledDocument doc, Token token) {
        try {
            // Calculate the position in the document based on line and column
            Element root = doc.getDefaultRootElement();
            int pos = root.getElement(token.getLine() - 1).getStartOffset() + token.getColumn() - 1;
            
            // Apply the style to the token
            doc.setCharacterAttributes(pos, token.getValue().length(), getStyle(token.getType()), true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
