package org.abcd.examples.ArrayInit;

import org.antlr.v4.runtime.*;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" 1. DEMONSTRAÇÃO DA ANÁLISE LÉXICA (Tokens)");
        System.out.println("==================================================\n");

        // expressão valida
        testarLexicoIsolado("(+ 1 2.5 \"abc\" T NIL #t foo-bar)");

        // caractere invalido (@)
        testarLexicoIsolado("(+ 1 @2)");


        System.out.println("\n==================================================");
        System.out.println(" 2. TESTES DE ANÁLISE SINTÁTICA");
        System.out.println("==================================================\n");

        // casos validos
        testarProcessamento("(+ 1 2.5 \"abc\" T NIL #t foo-bar)");
        testarProcessamento("(+ 1 (* 2 3) \"texto\" T)");
        testarProcessamento("(NIL)");
        testarProcessamento("42");
        testarProcessamento("\"apenas uma string\"");

        // casos invalidos
        testarProcessamento("(+ 1 2");          // parentese não fechado
        testarProcessamento("(1 2))");           // parentese a fechar em excesso
        testarProcessamento("(+ 1 @2)");         // caractere invalido
    }


    private static void testarLexicoIsolado(String entrada) {
        System.out.println("Entrada: " + entrada);
        CharStream input = CharStreams.fromString(entrada);
        LispLexer lexer = new LispLexer(input);

        lexer.removeErrorListeners();
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg, RecognitionException e) {
                System.out.println("  ERRO LÉXICO (linha " + line + ", coluna " + charPositionInLine + "): " + msg);
            }
        });

        for (Token token : lexer.getAllTokens()) {
            String nomeToken = LispLexer.VOCABULARY.getSymbolicName(token.getType());
            System.out.printf("  [Token] Texto: %-12s | Tipo: %s%n", token.getText(), nomeToken);
        }
        System.out.println("--------------------------------------------------");
    }

    private static void testarProcessamento(String entrada) {
        System.out.println("Entrada: " + entrada);

        try {
            CharStream input = CharStreams.fromString(entrada);
            LispLexer lexer = new LispLexer(input);

            lexer.removeErrorListeners();
            lexer.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg, RecognitionException e) {
                    System.out.println("  ERRO LÉXICO (linha " + line + ", coluna " + charPositionInLine + "): " + msg);
                }
            });

            // recolhe os tokens
            List<? extends Token> tokens = lexer.getAllTokens();

            // executa o parser
            ManualLispParser parser = new ManualLispParser(tokens);
            parser.parse();

        } catch (Exception e) {
            System.out.println("  -> " + e.getMessage());
        }

        System.out.println("--------------------------------------------------");
    }
}