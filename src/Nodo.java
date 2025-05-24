/*
 * ===============================================================================
 * CLASE NODO - REPRESENTACIÓN SEMÁNTICA DE EXPRESIONES
 * ===============================================================================
 * 
 * FASE DEL COMPILADOR: ANÁLISIS SEMÁNTICO (Fase 3)
 * 
 * Esta clase representa un nodo en el árbol semántico que se construye
 * durante el análisis sintáctico-semántico. Cada nodo contiene información
 * sobre el tipo y valor de una expresión.
 * 
 * PROPÓSITO EN EL ANÁLISIS SEMÁNTICO:
 * 1. Almacenar información de tipos para verificación semántica
 * 2. Representar el resultado de evaluaciones de expresiones
 * 3. Facilitar la propagación de tipos a través del AST
 * 4. Detectar y reportar errores de tipos
 * 
 * ATRIBUTOS:
 * - tipo: Tipo semántico de la expresión (int, float, string, error)
 * - valor: Representación del valor o expresión
 * 
 * UTILIZACIÓN:
 * - Creado por las acciones semánticas en el parser
 * - Utilizado para verificar compatibilidad de tipos
 * - Fundamental para el análisis de operaciones aritméticas
 * ===============================================================================
 */

/**
 * NODO SEMÁNTICO PARA ANÁLISIS DE TIPOS
 * 
 * Representa un elemento con tipo en el árbol semántico del compilador.
 * Es fundamental para implementar el análisis semántico y la verificación de tipos.
 */
public class Nodo {
    // ATRIBUTOS DEL NODO SEMÁNTICO
    public String tipo;    // Tipo semántico: "int", "float", "string", "error", etc.
    public Object valor;   // Valor o representación de la expresión

    /**
     * CONSTRUCTOR BÁSICO
     * Crea un nodo con solo información de tipo
     */
    public Nodo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * CONSTRUCTOR COMPLETO
     * Crea un nodo con tipo y valor específicos
     * 
     * Este constructor es el más utilizado en las acciones semánticas
     * del parser para crear nodos que representen el resultado de
     * operaciones y expresiones.
     */
    public Nodo(String tipo, Object valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    /**
     * REPRESENTACIÓN EN CADENA
     * 
     * Proporciona una representación legible del nodo para depuración
     * y reporte de resultados del análisis semántico.
     */
    public String toString() {
        return tipo + (valor != null ? " : " + valor : "");
    }
}
