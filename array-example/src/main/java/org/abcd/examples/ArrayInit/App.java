package org.abcd.examples.ArrayInit;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.gui.TreeViewer;

import javax.swing.*;
import java.util.Arrays;

public class App {

    public static void main(String[] args) {
        TreeUI tree = new TreeUI();

        System.out.println("Testes Léxicos");
        testarLexico("(\"hello\")");

        System.out.println("\n Testes Sintáticos (casos válidos)");
        testarSintaxeValida("(+ 1 (* 2 3) \"texto\" T)");
        testarSintaxeValida("(NIL)");
        testarSintaxeValida("42");
        testarSintaxeValida("\"apenas uma string\"");

        System.out.println("\nTestes Sintáticos (casos inválidos)");
        testarSintaxeInvalida("(+ 1 2");           // parêntese não fechado
        testarSintaxeInvalida("(1 2))");            // parêntese sobrando
        testarSintaxeInvalida("(+ 1 @2)");          // caractere/token inválido

        System.out.println("\n Visualização da Árvore");
        tree.mostrarArvore("\"Hello World!\"");
    }

    private static void testarLexico(String entrada) {
        System.out.println("Entrada: " + entrada);
        CharStream input = CharStreams.fromString(entrada);
        LispLexer lexer = new LispLexer(input);

        for (Token token : lexer.getAllTokens()) {
            String nomeToken = LispLexer.VOCABULARY.getSymbolicName(token.getType());
            System.out.printf("  Texto: %-12s Tipo: %s%n", token.getText(), nomeToken);
        }
    }

    private static void testarSintaxeValida(String entrada) {
        ResultadoParse resultado = parsear(entrada, false);
        if (resultado != null) {
            System.out.println("OK  -> " + entrada);
            System.out.println("     Árvore: " + resultado.tree.toStringTree(resultado.parser));
        }
    }

    private static void testarSintaxeInvalida(String entrada) {
        System.out.println("Entrada (deve falhar): " + entrada);
        parsear(entrada, true);
    }

    private static class ResultadoParse {
        final ParseTree tree;
        final LispParser parser;

        ResultadoParse(ParseTree tree, LispParser parser) {
            this.tree = tree;
            this.parser = parser;
        }
    }

    private static ResultadoParse parsear(String entrada, boolean esperaErro) {
        final int[] erros = {0};

        CharStream input = CharStreams.fromString(entrada);
        LispLexer lexer = new LispLexer(input);

        // erros léxicos
        lexer.removeErrorListeners();
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg, RecognitionException e) {
                System.out.println("  ERRO LÉXICO (linha " + line + ", coluna " + charPositionInLine + "): " + msg);
                erros[0]++;
            }
        });

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LispParser parser = new LispParser(tokens);

        // erros sintáticos
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg, RecognitionException e) {
                System.out.println("  ERRO SINTÁTICO (linha " + line + ", coluna " + charPositionInLine + "): " + msg);
                erros[0]++;
            }
        });

        ParseTree tree = parser.start();

        if (erros[0] > 0) {
            if (!esperaErro) {
                System.out.println("FALHOU (erro inesperado) -> " + entrada);
            }
            return null;
        } else {
            if (esperaErro) {
                System.out.println("  ATENÇÃO: entrada foi aceita mas deveria ter falhado!");
            }
            return new ResultadoParse(tree, parser);
        }
    }
}