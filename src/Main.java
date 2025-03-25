import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static JTextArea inputArea;
    private static JTextPane outputPane;
    private static JTextArea errorArea;
    private static JTextArea tokenArea;
    private static List<Token> tokens = new ArrayList<>();
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            JFrame frame = new JFrame("Python Analyzer v1");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            
            // Create main panel
            JPanel mainPanel = new JPanel(new BorderLayout());
            
            // Create top panel for input and analyze button
            JPanel topPanel = new JPanel(new BorderLayout());
            
            // Input text area
            inputArea = new JTextArea();
            inputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            JScrollPane inputScrollPane = new JScrollPane(inputArea);
            
            // Analyze button
            JButton analyzeButton = new JButton("Analyze");
            analyzeButton.addActionListener(e -> analyzeCode());
            
            topPanel.add(inputScrollPane, BorderLayout.CENTER);
            topPanel.add(analyzeButton, BorderLayout.EAST);
            
            // Create bottom split pane for output and analysis
            JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            
            // Output text pane for highlighted code
            outputPane = new JTextPane();
            outputPane.setEditable(false);
            outputPane.setFont(new Font("Monospaced", Font.PLAIN, 14));
            outputPane.setBackground(new Color(94, 86, 86)); // Dark background
            JScrollPane outputScrollPane = new JScrollPane(outputPane);
            
            // Create a split pane for errors and tokens
            JSplitPane analysisSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            
            // Error text area
            errorArea = new JTextArea();
            errorArea.setEditable(false);
            errorArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            errorArea.setForeground(Color.RED);
            JScrollPane errorScrollPane = new JScrollPane(errorArea);
            
            // Token text area
            tokenArea = new JTextArea();
            tokenArea.setEditable(false);
            tokenArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            tokenArea.setForeground(Color.darkGray);
            JScrollPane tokenScrollPane = new JScrollPane(tokenArea);
            
            analysisSplitPane.setLeftComponent(errorScrollPane);
            analysisSplitPane.setRightComponent(tokenScrollPane);
            analysisSplitPane.setDividerLocation(300);
            
            bottomSplitPane.setTopComponent(outputScrollPane);
            bottomSplitPane.setBottomComponent(analysisSplitPane);
            bottomSplitPane.setDividerLocation(250);
            
            // Add components to main split pane
            JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            mainSplitPane.setTopComponent(topPanel);
            mainSplitPane.setBottomComponent(bottomSplitPane);
            mainSplitPane.setDividerLocation(200);
            
            mainPanel.add(mainSplitPane, BorderLayout.CENTER);
            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }
    
    private static void analyzeCode() {
        String input = inputArea.getText();
        tokens.clear();
        errorArea.setText("");
        tokenArea.setText("");
        
        try {
            // Create a new lexer
            PythonLexer lexer = new PythonLexer(new StringReader(input));
            Token token;
            
            // Create styled document for output
            StyledDocument doc = new DefaultStyledDocument();
            try {
                doc.insertString(0, input, null);
            } catch (BadLocationException e) {
                e.printStackTrace();
                return;
            }
            
            // Process all tokens
            StringBuilder errors = new StringBuilder();
            StringBuilder tokenInfo = new StringBuilder();
            while ((token = lexer.yylex()) != null) {
                tokens.add(token);
                
                // Handle errors
                if (token.isError()) {
                    errors.append(String.format("ERROR: %s%n", token.getValue()));
                } else {
                    tokenInfo.append(formatTokenInfo(token));
                }
                
                // Apply highlighting
                SyntaxHighlighter.highlight(doc, token);
            }
            
            // Update UI
            outputPane.setStyledDocument(doc);
            errorArea.setText(errors.toString());
            tokenArea.setText(tokenInfo.toString());
            
        } catch (IOException e) {
            errorArea.setText("Error analyzing code: " + e.getMessage());
        }
    }
    
    private static String formatTokenInfo(Token token) {
        String tokenType = token.getType().toString();
        String tokenValue = token.getValue();
        return String.format("%s: '%s'%n", tokenType, tokenValue);
    }
}
