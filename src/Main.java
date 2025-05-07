import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;

public class Main {
    private static JTextArea inputArea;
    private static JTextPane outputPane;
    private static JTextArea errorArea;
    private static JTextArea tokenArea;
    private static JTextArea parserOutputArea;
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
            
            // Create top panel for input and buttons
            JPanel topPanel = new JPanel(new BorderLayout());
            
            // Input text area
            inputArea = new JTextArea();
            inputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            JScrollPane inputScrollPane = new JScrollPane(inputArea);
            
            // Create button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            
            // Open File button
            JButton openFileButton = new JButton("Open File");
            openFileButton.addActionListener(e -> openFile());
            
            // Analyze button
            JButton analyzeButton = new JButton("Analyze");
            analyzeButton.addActionListener(e -> analyzeCode());
            
            buttonPanel.add(openFileButton);
            buttonPanel.add(analyzeButton);
            
            topPanel.add(inputScrollPane, BorderLayout.CENTER);
            topPanel.add(buttonPanel, BorderLayout.EAST);
            
            // Create bottom split pane for output and analysis
            JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            
            // Output text pane for highlighted code
            outputPane = new JTextPane();
            outputPane.setEditable(false);
            outputPane.setFont(new Font("Monospaced", Font.PLAIN, 14));
            outputPane.setBackground(new Color(251, 248, 248)); // Light background
            JScrollPane outputScrollPane = new JScrollPane(outputPane);
            
            // Create a tabbed pane for analysis results
            JTabbedPane analysisTabbedPane = new JTabbedPane();
            
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
            
            // Parser output area
            parserOutputArea = new JTextArea();
            parserOutputArea.setEditable(false);
            parserOutputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            parserOutputArea.setForeground(new Color(0, 100, 0)); // Dark green
            JScrollPane parserScrollPane = new JScrollPane(parserOutputArea);
            
            // Add tabs
            analysisTabbedPane.addTab("Errors", errorScrollPane);
            analysisTabbedPane.addTab("Tokens", tokenScrollPane);
            analysisTabbedPane.addTab("Parser Output", parserScrollPane);
            
            bottomSplitPane.setTopComponent(outputScrollPane);
            bottomSplitPane.setBottomComponent(analysisTabbedPane);
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
    
    private static void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Read the file content
                StringBuilder content = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                }
                // Set the content to input area
                inputArea.setText(content.toString());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, 
                    "Error reading file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void analyzeCode() {
        String input = inputArea.getText();
        tokens.clear();
        errorArea.setText("");
        tokenArea.setText("");
        parserOutputArea.setText("");
        
        try {
            // Create a stream to capture System.out for parser
            ByteArrayOutputStream parserOutputStream = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            PrintStream originalErr = System.err;
            
            // Create a styled document for output
            StyledDocument doc = new DefaultStyledDocument();
            try {
                doc.insertString(0, input, null);
            } catch (BadLocationException e) {
                e.printStackTrace();
                return;
            }
            
            // STEP 1: Lexical Analysis
            try {
                // Process all tokens and highlight
                StringBuilder tokenInfo = new StringBuilder();
                StringBuilder errors = new StringBuilder();
                
                // First pass: collect tokens for display and highlighting
                PythonLexer lexer = new PythonLexer(new StringReader(input));
                Symbol symbol;
                while (true) {
                    symbol = lexer.yylex();
                    if (symbol == null || symbol.value == null) break;
                    
                    if (symbol.value instanceof Token) {
                        Token token = (Token) symbol.value;
                        tokenInfo.append(formatTokenInfo(token, symbol.sym));
                        
                        // Apply highlighting to the document
                        try {
                            int startOffset = getOffsetForLineAndColumn(doc, token.linea, token.columna);
                            int length = token.valor.toString().length();
                            
                            SimpleAttributeSet attributes = new SimpleAttributeSet();
                            StyleConstants.setForeground(attributes, token.color);
                            doc.setCharacterAttributes(startOffset, length, attributes, true);
                        } catch (Exception e) {
                            System.err.println("Error highlighting token: " + e.getMessage());
                        }
                        
                        // Collect errors
                        if (symbol.sym == sym.ERROR) {
                            errors.append(String.format("Error at line %d, column %d: %s%n", 
                                token.linea, token.columna, token.valor));
                        }
                    }
                }
                
                tokenArea.setText(tokenInfo.toString());
                errorArea.setText(errors.toString());
            } catch (Exception e) {
                errorArea.setText("Lexical analysis error: " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
            // STEP 2: Parsing
            try {
                // Create separate streams for stdout and stderr
                ByteArrayOutputStream parserStdOut = new ByteArrayOutputStream();
                ByteArrayOutputStream parserStdErr = new ByteArrayOutputStream();
                
                // Redirect output streams
                System.setOut(new PrintStream(parserStdOut));
                System.setErr(new PrintStream(parserStdErr));
                
                // Create a new lexer and parser
                System.out.println("===== INICIANDO ANÁLISIS SINTÁCTICO =====");
                PythonLexer lexer = new PythonLexer(new StringReader(input));
                LexerAdapter lexerAdapter = new LexerAdapter(lexer);
                Parser parser = new Parser(lexerAdapter);
                
                try {
                    // Parse the input
                    parser.parse();
                    System.out.println("===== ANÁLISIS SINTÁCTICO COMPLETADO =====");
                } catch (Exception e) {
                    System.err.println("Parsing error: " + e.getMessage());
                    System.err.println("===== ERROR EN ANÁLISIS SINTÁCTICO =====");
                    e.printStackTrace(System.err);
                }
                
                // Get the parser output
                String parserOutput = parserStdOut.toString();
                String parserErrors = parserStdErr.toString();
                
                // Display output and errors
                if (!parserErrors.isEmpty()) {
                    parserOutputArea.setText(parserErrors + "\n\n" + parserOutput);
                } else {
                    parserOutputArea.setText(parserOutput);
                }
                
            } catch (Exception e) {
                parserOutputArea.setText("Error crítico en la configuración del parser: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // Restore original output streams
                System.setOut(originalOut);
                System.setErr(originalErr);
            }
            
            // Update the syntax-highlighted output
            outputPane.setStyledDocument(doc);
            
        } catch (Exception e) {
            errorArea.setText("Error analyzing code: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String formatTokenInfo(Token token, int symbolType) {
        return String.format("%-15s | Line: %-3d | Col: %-3d | Value: %s%n", 
            getSymbolName(symbolType), token.linea, token.columna, token.valor.toString());
    }
    
    private static String getSymbolName(int symbolType) {
        try {
            return sym.terminalNames[symbolType];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "UNKNOWN(" + symbolType + ")";
        }
    }
    
    private static int getOffsetForLineAndColumn(Document doc, int line, int column) throws BadLocationException {
        Element root = doc.getDefaultRootElement();
        // Adjust for 1-based line numbers to 0-based element indices
        Element lineElement = root.getElement(line - 1);
        if (lineElement == null) {
            throw new BadLocationException("Invalid line number: " + line, 0);
        }
        int startOffset = lineElement.getStartOffset();
        // Adjust for 1-based column numbers
        return startOffset + (column - 1);
    }
}
