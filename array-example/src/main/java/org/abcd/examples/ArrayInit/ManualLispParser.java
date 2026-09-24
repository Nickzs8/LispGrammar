package org.abcd.examples.ArrayInit;

import org.antlr.v4.runtime.Token;
import java.util.List;
import java.util.stream.Collectors;

public class ManualLispParser {

    private final List<MyToken> tokens;
    private int currentIndex = 0;

    public ManualLispParser(List<? extends Token> antlrTokens) {
        this.tokens = antlrTokens.stream()
                .map(MyToken::new)
                .collect(Collectors.toList());
        this.currentIndex = 0;
    }

    public void parse() {
        sExpr();

        // se ainda houver tokens na lista após processar a expressão principal, ha lixo no final
        if (currentIndex < tokens.size()) {
            MyToken current = tokens.get(currentIndex);
            throw new RuntimeException("Erro sintático: tokens extras encontrados no final da entrada '" + current.getText() + "'");
        }
        System.out.println("Sintaxe válida com sucesso!");
    }

    private void sExpr() {
        MyToken current = peek();
        if (current == null) {
            throw new RuntimeException("Erro sintático: fim inesperado da entrada.");
        }

        if (isAtom(current)) {
            atom();
        } else if (current.getType() == LispLexer.PAR_ABRE) {
            list();
        } else {
            throw new RuntimeException("Erro sintático na linha " + current.getLine() +
                    ": expressão inválida encontrada ('" + current.getText() + "')");
        }
    }

    private void list() {
        consume(LispLexer.PAR_ABRE);

        MyToken next = peek();
        if (next != null && next.getType() == LispLexer.PAR_FECHA) {
            consume(LispLexer.PAR_FECHA);
        } else {
            elements();
            consume(LispLexer.PAR_FECHA);
        }
    }

    private void elements() {
        sExpr();
        while (canStartSExpr(peek())) {
            sExpr();
        }
    }

    private void atom() {
        MyToken current = peek();
        if (isAtom(current)) {
            currentIndex++;
        } else {
            throw new RuntimeException("Erro sintático: esperado um átomo, mas encontrou '" + (current != null ? current.getText() : "EOF") + "'");
        }
    }

    // return null se estourar limite (EOF)
    private MyToken peek() {
        if (currentIndex < tokens.size()) {
            return tokens.get(currentIndex);
        }
        return null;
    }

    private void consume(int expectedType) {
        MyToken current = peek();

        // traduz id para nome
        String nomeEsperado = LispLexer.VOCABULARY.getSymbolicName(expectedType);
        if (nomeEsperado == null) {
            nomeEsperado = LispLexer.VOCABULARY.getLiteralName(expectedType);
        }

        if (current != null && current.getType() == expectedType) {
            currentIndex++;
        }
        // erro
        else {
            String found = (current != null) ? current.getText() : "EOF (fim da entrada)";
            int line = (current != null) ? current.getLine() : 0;
            int col = (current != null) ? current.getCharPositionInLine() : 0;

            throw new RuntimeException("Erro sintático na linha " + line +
                    ", coluna " + col +
                    ": esperado o token [" + nomeEsperado + "], mas encontrou '" + found + "'");
        }
    }

    private boolean isAtom(MyToken t) {
        if (t == null) return false;
        int type = t.getType();
        return type == LispLexer.NUMBER ||
                type == LispLexer.SYMBOL ||
                type == LispLexer.STRING ||
                type == LispLexer.BOOLEAN;
    }

    private boolean canStartSExpr(MyToken t) {
        if (t == null) return false;
        int type = t.getType();
        return isAtom(t) || type == LispLexer.PAR_ABRE;
    }

    public static class MyToken {
        private final int type;
        private final String text;
        private final int line;
        private final int charPositionInLine;

        public MyToken(Token antlrToken) {
            this.type = antlrToken.getType();
            this.text = antlrToken.getText();
            this.line = antlrToken.getLine();
            this.charPositionInLine = antlrToken.getCharPositionInLine();
        }

        public int getType() { return this.type; }
        public String getText() { return this.text; }
        public int getLine() { return this.line; }
        public int getCharPositionInLine() { return this.charPositionInLine; }
    }
}