package org.abcd.examples.ArrayInit;

import org.antlr.v4.runtime.*;
import java.util.List;
import java.util.function.Consumer;

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


        System.out.println("\n==================================================");
        System.out.println(" 3. TESTES DE ANÁLISE SINTÁTICA LL(1) (TABELA)");
        System.out.println("==================================================\n");

        // rastreamento passo a passo da pilha e tabela (para demonstração)
        System.out.println(">> Demonstração passo a passo (pilha + tabela LL(1)):");
        testarLL1("(+ 1 (* 2 3))", true);

        // casos validos
        testarLL1("(+ 1 2.5 \"abc\" T NIL #t foo-bar)", false);
        testarLL1("()", false);
        testarLL1("(NIL)", false);
        testarLL1("42", false);
        testarLL1("\"apenas uma string\"", false);

        // casos invalidos
        testarLL1("(+ 1 2", false);              // parentese não fechado
        testarLL1("(1 2))", false);              // parentese a fechar em excesso
        testarLL1("(+ 1 @2)", false);            // caractere invalido
    }


    private static void testarLexicoIsolado(String entrada) {
        System.out.println("Entrada: " + entrada);
        CharStream input = CharStreams.fromString(entrada);
        LispLexer lexer = new LispLexer(input);

        // erro personalizado
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
        executarComLexer(entrada, tokens -> new ManualLispParser(tokens).parse());
    }

    private static void testarLL1(String entrada, boolean exibirRastreamento) {
        executarComLexer(entrada, tokens -> new LL1LispParser(tokens).parse(exibirRastreamento));
    }

    private static void executarComLexer(String entrada, Consumer<List<? extends Token>> acaoParser) {
        System.out.println("Entrada: " + entrada);
        final int[] errosLexicos = {0};

        try {
            CharStream input = CharStreams.fromString(entrada);
            LispLexer lexer = new LispLexer(input);

            // erro personalizado
            lexer.removeErrorListeners();
            lexer.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg, RecognitionException e) {
                    System.out.println("  ERRO LÉXICO (linha " + line + ", coluna " + charPositionInLine + "): " + msg);
                    errosLexicos[0]++;
                }
            });
            // recolhe os tokens
            List<? extends Token> tokens = lexer.getAllTokens();

            // só executa o parser se não houve erro léxico
            if (errosLexicos[0] > 0) {
                System.out.println("  -> Análise sintática abortada devido a erro léxico.");
            } else {
                acaoParser.accept(tokens);
            }
        } catch (Exception e) {
            System.out.println("  -> " + e.getMessage());
        }
        System.out.println("--------------------------------------------------");
    }
}