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
            frame.setSize(1000, 800);
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
            
            // Create bottom split pane for output and errors
            JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            
            // Output text pane for highlighted code
            outputPane = new JTextPane();
            outputPane.setEditable(false);
            outputPane.setFont(new Font("Monospaced", Font.PLAIN, 14));
            outputPane.setBackground(new Color(30, 30, 30)); // Dark background
            JScrollPane outputScrollPane = new JScrollPane(outputPane);
            
            // Error text area
            errorArea = new JTextArea();
            errorArea.setEditable(false);
            errorArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            errorArea.setForeground(Color.RED);
            JScrollPane errorScrollPane = new JScrollPane(errorArea);
            
            bottomSplitPane.setTopComponent(outputScrollPane);
            bottomSplitPane.setBottomComponent(errorScrollPane);
            bottomSplitPane.setDividerLocation(350);
            
            // Add components to main split pane
            JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            mainSplitPane.setTopComponent(topPanel);
            mainSplitPane.setBottomComponent(bottomSplitPane);
            mainSplitPane.setDividerLocation(300);
            
            mainPanel.add(mainSplitPane, BorderLayout.CENTER);
            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }
    
    private static void analyzeCode() {
        String input = inputArea.getText();
        tokens.clear();
        errorArea.setText("");
        
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
            while ((token = lexer.yylex()) != null) {
                tokens.add(token);
                
                // Handle errors
                if (token.isError()) {
                    String errorMsg;
                    String value = token.getValue();
                    
                    if (value.matches("[0-9]+.*")) {
                        errorMsg = "Identifier cannot start with a number";
                    } else if (value.equals("defe")) {
                        errorMsg = "Did you mean 'def'?";
                    } else if (value.contains("def")) {
                        errorMsg = "Invalid function definition";
                    } else {
                        errorMsg = "Invalid token";
                    }
                    
                    errors.append(String.format("Error at line %d, column %d: %s - '%s'%n",
                            token.getLine(), token.getColumn(), errorMsg, token.getValue()));
                }
                
                // Apply highlighting
                SyntaxHighlighter.highlight(doc, token);
            }
            
            // Update UI
            outputPane.setStyledDocument(doc);
            errorArea.setText(errors.toString());
            
        } catch (IOException e) {
            errorArea.setText("Error analyzing code: " + e.getMessage());
        }
    }
}
