package org.abcd.examples.ArrayInit;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.gui.TreeViewer;

import javax.swing.*;
import java.util.Arrays;

/**
 * Analisador Léxico e Sintático para subconjunto de Lisp.
 */
public class App {

    public static void main(String[] args) {
        System.out.println("=== Testes Léxicos ===");
        testarLexico("(+ 1 2.5 \"abc\" T NIL #t foo-bar)");

        System.out.println("\n=== Testes Sintáticos (casos válidos) ===");
        testarSintaxeValida("(+ 1 (* 2 3) \"texto\" T)");
        testarSintaxeValida("(NIL)");
        testarSintaxeValida("42");
        testarSintaxeValida("\"apenas uma string\"");

        System.out.println("\n=== Testes Sintáticos (casos inválidos) ===");
        testarSintaxeInvalida("(+ 1 2");           // parêntese não fechado
        testarSintaxeInvalida("(1 2))");            // parêntese sobrando
        testarSintaxeInvalida("(+ 1 @2)");           // símbolo/token inválido

        System.out.println("\n=== Visualização da Árvore ===");
        mostrarArvore("(+ 1 (* 2 3) \"texto\" T)");
    }

    /**
     * Roda apenas o lexer sobre a entrada e imprime cada token reconhecido
     * com seu nome simbólico (ex: PAR_ABRE, NUMBER, SYMBOL...).
     */
    private static void testarLexico(String entrada) {
        System.out.println("Entrada: " + entrada);
        CharStream input = CharStreams.fromString(entrada);
        LispLexer lexer = new LispLexer(input);

        for (Token token : lexer.getAllTokens()) {
            String nomeToken = LispLexer.VOCABULARY.getSymbolicName(token.getType());
            System.out.printf("  Texto: %-12s Tipo: %s%n", token.getText(), nomeToken);
        }
    }

    /**
     * Roda o parser completo (léxico + sintático) e espera que a entrada
     * seja aceita sem erros.
     */
    private static void testarSintaxeValida(String entrada) {
        ParseTree tree = parsear(entrada, false);
        if (tree != null) {
            System.out.println("OK  -> " + entrada);
            System.out.println("     Árvore: " + tree.toStringTree());
        }
    }

    /**
     * Roda o parser esperando que a entrada seja rejeitada;
     * imprime os erros de sintaxe capturados.
     */
    private static void testarSintaxeInvalida(String entrada) {
        System.out.println("Entrada (deve falhar): " + entrada);
        parsear(entrada, true);
    }

    /**
     * Constrói lexer + parser para a entrada dada.
     * Se esperaErro=true, apenas reporta os erros sem lançar exceção.
     */
    private static ParseTree parsear(String entrada, boolean esperaErro) {
        CharStream input = CharStreams.fromString(entrada);
        LispLexer lexer = new LispLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LispParser parser = new LispParser(tokens);

        // Listener customizado para capturar erros em vez de só imprimir no console padrão
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg, RecognitionException e) {
                System.out.println("  ERRO (linha " + line + ", coluna " + charPositionInLine + "): " + msg);
            }
        });

        ParseTree tree = parser.sExpr();

        if (parser.getNumberOfSyntaxErrors() > 0) {
            if (!esperaErro) {
                System.out.println("FALHOU (erro inesperado) -> " + entrada);
            }
            return null;
        } else {
            if (esperaErro) {
                System.out.println("  ATENÇÃO: entrada foi aceita mas deveria ter falhado!");
            }
            return tree;
        }
    }

    /**
     * Abre uma janela gráfica mostrando a árvore sintática da entrada.
     */
    private static void mostrarArvore(String entrada) {
        CharStream input = CharStreams.fromString(entrada);
        LispLexer lexer = new LispLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LispParser parser = new LispParser(tokens);

        ParseTree tree = parser.sExpr();

        JFrame frame = new JFrame("Árvore Sintática - " + entrada);
        JPanel panel = new JPanel();
        TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);
        viewer.setScale(1.5);
        panel.add(viewer);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}