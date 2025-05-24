/*
 * ===============================================================================
 * ANALIZADOR DE PYTHON - IMPLEMENTACIÓN DE LAS FASES DE UN COMPILADOR
 * ===============================================================================
 * 
 * Este proyecto implementa las tres fases principales de un compilador:
 * 
 * 1. ANÁLISIS LÉXICO (Lexical Analysis):
 *    - Convierte el código fuente en tokens
 *    - Identifica palabras clave, operadores, identificadores, literales, etc.
 *    - Implementado en: PythonLexer.flex, Token.java
 * 
 * 2. ANÁLISIS SINTÁCTICO (Syntax Analysis / Parsing):
 *    - Verifica que los tokens sigan la gramática del lenguaje
 *    - Construye un árbol sintáctico abstracto (AST)
 *    - Implementado en: parser.cup, Parser.java, LexerAdapter.java
 * 
 * 3. ANÁLISIS SEMÁNTICO (Semantic Analysis):
 *    - Verifica la consistencia de tipos
 *    - Maneja tabla de símbolos
 *    - Detecta errores semánticos
 *    - Implementado en: parser.cup (acciones semánticas), Nodo.java
 * 
 * ===============================================================================
 */

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;

/*
 * CLASE PRINCIPAL - INTERFAZ GRÁFICA Y COORDINADOR DE LAS FASES DEL COMPILADOR
 * 
 * Esta clase actúa como el punto de entrada principal y coordinador de todas
 * las fases del compilador. Proporciona una interfaz gráfica para visualizar
 * los resultados de cada fase.
 */
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
              // ===================================================================
            // FASE 1: ANÁLISIS LÉXICO (LEXICAL ANALYSIS)
            // ===================================================================
            // 
            // OBJETIVO: Convertir el código fuente en una secuencia de tokens
            // 
            // PROCESO:
            // 1. Lee el código carácter por carácter
            // 2. Agrupa caracteres en unidades léxicas (tokens)
            // 3. Clasifica cada token (palabra clave, operador, identificador, etc.)
            // 4. Asigna colores para resaltado de sintaxis
            // 5. Detecta errores léxicos (caracteres inválidos, identificadores mal formados)
            //
            // ENTRADA: Código fuente como cadena de caracteres
            // SALIDA: Secuencia de tokens con sus atributos (tipo, valor, posición, color)
            // ===================================================================
            try {
                System.out.println("=== INICIANDO ANÁLISIS LÉXICO ===");
                
                // Estructuras para almacenar resultados del análisis léxico
                StringBuilder tokenInfo = new StringBuilder();
                StringBuilder errors = new StringBuilder();
                
                // CREACIÓN DEL ANALIZADOR LÉXICO
                // El lexer es generado por JFlex a partir de PythonLexer.flex
                PythonLexer lexer = new PythonLexer(new StringReader(input));
                Symbol symbol;
                
                // PROCESO DE TOKENIZACIÓN
                // Iteramos sobre todo el código fuente para extraer tokens
                while (true) {
                    symbol = lexer.yylex(); // Obtiene el siguiente token
                    if (symbol == null || symbol.value == null) break;
                    
                    if (symbol.value instanceof Token) {
                        Token token = (Token) symbol.value;
                        
                        // RECOLECCIÓN DE INFORMACIÓN DE TOKENS
                        // Formatea la información del token para mostrar al usuario
                        tokenInfo.append(formatTokenInfo(token, symbol.sym));
                        
                        // RESALTADO DE SINTAXIS
                        // Aplica colores basados en el tipo de token
                        try {
                            int startOffset = getOffsetForLineAndColumn(doc, token.linea, token.columna);
                            int length = token.valor.toString().length();
                            
                            SimpleAttributeSet attributes = new SimpleAttributeSet();
                            StyleConstants.setForeground(attributes, token.color);
                            doc.setCharacterAttributes(startOffset, length, attributes, true);
                        } catch (Exception e) {
                            System.err.println("Error highlighting token: " + e.getMessage());
                        }
                        
                        // DETECCIÓN DE ERRORES LÉXICOS
                        // Recolecta tokens de error identificados por el lexer
                        if (symbol.sym == sym.ERROR) {
                            errors.append(String.format("❌ Error léxico en línea %d, columna %d: %s%n", 
                                token.linea, token.columna, token.valor));
                        }
                    }
                }
                
                // Muestra los resultados del análisis léxico
                tokenArea.setText(tokenInfo.toString());
                errorArea.setText(errors.toString());
                System.out.println("=== ANÁLISIS LÉXICO COMPLETADO ===");
                
            } catch (Exception e) {
                errorArea.setText("Error crítico en análisis léxico: " + e.getMessage());
                e.printStackTrace();
                return;
            }
              // ===================================================================
            // FASE 2: ANÁLISIS SINTÁCTICO (SYNTAX ANALYSIS / PARSING)
            // ===================================================================
            //
            // OBJETIVO: Verificar que la secuencia de tokens siga la gramática del lenguaje
            //
            // PROCESO:
            // 1. Toma los tokens del análisis léxico como entrada
            // 2. Aplica las reglas de la gramática definidas en parser.cup
            // 3. Construye un árbol sintáctico abstracto (AST) implícito
            // 4. Detecta errores sintácticos (tokens inesperados, estructura inválida)
            //
            // ENTRADA: Secuencia de tokens del análisis léxico
            // SALIDA: Confirmación de estructura sintáctica válida o errores sintácticos
            // ===================================================================
            try {
                System.out.println("=== INICIANDO ANÁLISIS SINTÁCTICO ===");
                
                // Captura de salida para mostrar el proceso del parser
                ByteArrayOutputStream parserStdOut = new ByteArrayOutputStream();
                ByteArrayOutputStream parserStdErr = new ByteArrayOutputStream();
                
                // Redirección temporal de streams para capturar salida del parser
                System.setOut(new PrintStream(parserStdOut));
                System.setErr(new PrintStream(parserStdErr));
                
                // CREACIÓN DEL ANALIZADOR SINTÁCTICO
                // El parser es generado por Cup a partir de parser.cup
                PythonLexer lexer = new PythonLexer(new StringReader(input));
                LexerAdapter lexerAdapter = new LexerAdapter(lexer); // Adaptador para Cup
                Parser parser = new Parser(lexerAdapter);
                
                try {
                    // PROCESO DE PARSING
                    // Aplica las reglas gramaticales y ejecuta acciones semánticas
                    System.out.println("Aplicando reglas gramaticales...");
                    parser.parse(); // Aquí ocurre el análisis sintáctico principal
                    System.out.println("=== ANÁLISIS SINTÁCTICO COMPLETADO SIN ERRORES ===");
                } catch (Exception e) {
                    // MANEJO DE ERRORES SINTÁCTICOS
                    System.err.println("❌ Error sintáctico detectado: " + e.getMessage());
                    System.err.println("=== ANÁLISIS SINTÁCTICO COMPLETADO CON ERRORES ===");
                    e.printStackTrace(System.err);
                }
                
                // Obtención y presentación de resultados
                String parserOutput = parserStdOut.toString();
                String parserErrors = parserStdErr.toString();
                
                // Muestra la salida del análisis sintáctico y semántico
                if (!parserErrors.isEmpty()) {
                    parserOutputArea.setText(parserErrors + "\n\n" + parserOutput);
                } else {
                    parserOutputArea.setText(parserOutput);
                }
                
            } catch (Exception e) {
                parserOutputArea.setText("Error crítico en configuración del parser: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // Restauración de streams originales
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
