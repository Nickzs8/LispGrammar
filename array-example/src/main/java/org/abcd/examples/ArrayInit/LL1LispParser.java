package org.abcd.examples.ArrayInit;

import org.antlr.v4.runtime.Token;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Analisador sintático preditivo LL(1) dirigido por tabela para a gramática de Lisp.
 *
 * <p>Gramática fatorada à esquerda (equivalente à de {@link ManualLispParser}):
 * <pre>
 *   start        -> sExpr
 *   sExpr        -> atom | list
 *   list         -> PAR_ABRE listTail
 *   listTail     -> elements PAR_FECHA | PAR_FECHA
 *   elements     -> sExpr elementsTail
 *   elementsTail -> sExpr elementsTail | ε
 *   atom         -> NUMBER | SYMBOL | STRING | BOOLEAN
 * </pre>
 */
public class LL1LispParser {

    /**
     * Marca comum para símbolos gramaticais (terminais e não-terminais) na pilha e nas produções.
     */
    public interface Simbolo {
        String getRotulo();
    }

    /**
     * Símbolos não-terminais da gramática fatorada.
     */
    public enum NaoTerminal implements Simbolo {
        START("start"),
        S_EXPR("sExpr"),
        LIST("list"),
        LIST_TAIL("listTail"),
        ELEMENTS("elements"),
        ELEMENTS_TAIL("elementsTail"),
        ATOM("atom");

        private final String rotulo;

        NaoTerminal(String rotulo) {
            this.rotulo = rotulo;
        }

        @Override
        public String getRotulo() {
            return rotulo;
        }
    }

    /**
     * Símbolos terminais mapeados aos tipos de token gerados pelo {@link LispLexer}.
     */
    public enum Terminal implements Simbolo {
        PAR_ABRE(LispLexer.PAR_ABRE, "("),
        PAR_FECHA(LispLexer.PAR_FECHA, ")"),
        NUMBER(LispLexer.NUMBER, "NUMBER"),
        SYMBOL(LispLexer.SYMBOL, "SYMBOL"),
        STRING(LispLexer.STRING, "STRING"),
        BOOLEAN(LispLexer.BOOLEAN, "BOOLEAN"),
        EOF(Token.EOF, "$");

        private final int tokenType;
        private final String rotulo;

        Terminal(int tokenType, String rotulo) {
            this.tokenType = tokenType;
            this.rotulo = rotulo;
        }

        @Override
        public String getRotulo() {
            return rotulo;
        }

        public static Terminal fromTokenType(int tokenType) {
            for (Terminal terminal : values()) {
                if (terminal.tokenType == tokenType) {
                    return terminal;
                }
            }
            throw new RuntimeException("Erro sintático: tipo de token desconhecido (" + tokenType + ").");
        }
    }

    /**
     * Representa uma regra {@code lhs -> rhs}. Uma lista {@code rhs} vazia representa ε.
     */
    public record Producao(NaoTerminal lhs, List<Simbolo> rhs) {
        @Override
        public String toString() {
            if (rhs.isEmpty()) {
                return lhs.getRotulo() + " -> ε";
            }
            String corpo = rhs.stream()
                    .map(Simbolo::getRotulo)
                    .collect(Collectors.joining(" "));
            return lhs.getRotulo() + " -> " + corpo;
        }
    }

    private static final List<Terminal> ATOMOS = List.of(
            Terminal.NUMBER, Terminal.SYMBOL, Terminal.STRING, Terminal.BOOLEAN
    );
    private static final List<Terminal> INICIO_SEXPR = List.of(
            Terminal.PAR_ABRE, Terminal.NUMBER, Terminal.SYMBOL, Terminal.STRING, Terminal.BOOLEAN
    );

    private static final Map<NaoTerminal, Map<Terminal, Producao>> TABELA_LL1 = construirTabelaLL1();

    private final List<ManualLispParser.MyToken> tokens;

    public LL1LispParser(List<? extends Token> antlrTokens) {
        this.tokens = antlrTokens.stream()
                .filter(t -> t.getType() != Token.EOF)
                .map(ManualLispParser.MyToken::new)
                .collect(Collectors.toList());
    }

    // ===== Construção modular da Tabela LL(1) =====

    private static Map<NaoTerminal, Map<Terminal, Producao>> construirTabelaLL1() {
        Map<NaoTerminal, Map<Terminal, Producao>> tabela = criarTabelaVazia();

        registrarRegrasStart(tabela);
        registrarRegrasSExpr(tabela);
        registrarRegrasList(tabela);
        registrarRegrasElements(tabela);
        registrarRegrasAtom(tabela);

        return tabela;
    }

    private static Map<NaoTerminal, Map<Terminal, Producao>> criarTabelaVazia() {
        Map<NaoTerminal, Map<Terminal, Producao>> tabela = new EnumMap<>(NaoTerminal.class);
        for (NaoTerminal nt : NaoTerminal.values()) {
            tabela.put(nt, new EnumMap<>(Terminal.class));
        }
        return tabela;
    }

    private static void registrarRegrasStart(Map<NaoTerminal, Map<Terminal, Producao>> tabela) {
        // start -> sExpr
        registrar(tabela, NaoTerminal.START, INICIO_SEXPR, NaoTerminal.S_EXPR);
    }

    private static void registrarRegrasSExpr(Map<NaoTerminal, Map<Terminal, Producao>> tabela) {
        // sExpr -> list | atom
        registrar(tabela, NaoTerminal.S_EXPR, List.of(Terminal.PAR_ABRE), NaoTerminal.LIST);
        registrar(tabela, NaoTerminal.S_EXPR, ATOMOS, NaoTerminal.ATOM);
    }

    private static void registrarRegrasList(Map<NaoTerminal, Map<Terminal, Producao>> tabela) {
        // list -> '(' listTail
        registrar(tabela, NaoTerminal.LIST, List.of(Terminal.PAR_ABRE), Terminal.PAR_ABRE, NaoTerminal.LIST_TAIL);
        // listTail -> elements ')' | ')'
        registrar(tabela, NaoTerminal.LIST_TAIL, INICIO_SEXPR, NaoTerminal.ELEMENTS, Terminal.PAR_FECHA);
        registrar(tabela, NaoTerminal.LIST_TAIL, List.of(Terminal.PAR_FECHA), Terminal.PAR_FECHA);
    }

    private static void registrarRegrasElements(Map<NaoTerminal, Map<Terminal, Producao>> tabela) {
        // elements -> sExpr elementsTail
        registrar(tabela, NaoTerminal.ELEMENTS, INICIO_SEXPR, NaoTerminal.S_EXPR, NaoTerminal.ELEMENTS_TAIL);
        // elementsTail -> sExpr elementsTail | ε  (FOLLOW(elementsTail) = { ')' })
        registrar(tabela, NaoTerminal.ELEMENTS_TAIL, INICIO_SEXPR, NaoTerminal.S_EXPR, NaoTerminal.ELEMENTS_TAIL);
        registrar(tabela, NaoTerminal.ELEMENTS_TAIL, List.of(Terminal.PAR_FECHA));
    }

    private static void registrarRegrasAtom(Map<NaoTerminal, Map<Terminal, Producao>> tabela) {
        // atom -> NUMBER | SYMBOL | STRING | BOOLEAN
        for (Terminal atomo : ATOMOS) {
            registrar(tabela, NaoTerminal.ATOM, List.of(atomo), atomo);
        }
    }

    private static void registrar(
            Map<NaoTerminal, Map<Terminal, Producao>> tabela,
            NaoTerminal lhs,
            List<Terminal> terminais,
            Simbolo... rhs) {
        Producao producao = new Producao(lhs, List.of(rhs));
        Map<Terminal, Producao> linha = tabela.get(lhs);

        for (Terminal terminal : terminais) {
            Producao existente = linha.put(terminal, producao);
            if (existente != null) {
                throw new IllegalStateException(
                        "Conflito LL(1) em [" + lhs.getRotulo() + ", " + terminal.getRotulo() + "]"
                );
            }
        }
    }

    /**
     * Executa a análise sintática sem imprimir o rastreamento passo a passo.
     */
    public void parse() {
        parse(false);
    }

    /**
     * Executa o algoritmo descendente preditivo dirigido por tabela usando pilha explícita.
     *
     * @param exibirRastreamento se {@code true}, imprime cada transição de pilha, entrada e ação.
     */
    public void parse(boolean exibirRastreamento) {
        Deque<Simbolo> pilha = new ArrayDeque<>();
        pilha.push(Terminal.EOF);
        pilha.push(NaoTerminal.START);

        int currentIndex = 0;

        if (exibirRastreamento) {
            System.out.printf("  %-38s | %-28s | %s%n", "PILHA (base -> topo)", "ENTRADA RESTANTE", "AÇÃO");
            System.out.println("  " + "-".repeat(100));
        }

        while (!pilha.isEmpty()) {
            Simbolo topo = pilha.peek();
            ManualLispParser.MyToken tokenAtual = (currentIndex < tokens.size()) ? tokens.get(currentIndex) : null;
            Terminal terminalAtual = (tokenAtual != null)
                    ? Terminal.fromTokenType(tokenAtual.getType())
                    : Terminal.EOF;

            if (topo instanceof Terminal terminalEsperado) {
                if (terminalEsperado != terminalAtual) {
                    lancarErro(tokenAtual, "esperado [" + terminalEsperado.getRotulo() + "]");
                }
                if (exibirRastreamento) {
                    imprimirPasso(pilha, currentIndex, "Casa " + terminalEsperado.getRotulo());
                }
                pilha.pop();
                currentIndex++;
            } else if (topo instanceof NaoTerminal naoTerminal) {
                Producao producao = TABELA_LL1.get(naoTerminal).get(terminalAtual);
                if (producao == null) {
                    String esperados = TABELA_LL1.get(naoTerminal).keySet().stream()
                            .map(Terminal::getRotulo)
                            .collect(Collectors.joining(", "));
                    lancarErro(tokenAtual, "inesperado ao expandir <" + naoTerminal.getRotulo() +
                            "> (esperado um de: " + esperados + ")");
                }
                if (exibirRastreamento) {
                    imprimirPasso(pilha, currentIndex, producao.toString());
                }
                pilha.pop();
                List<Simbolo> rhs = producao.rhs();
                for (int i = rhs.size() - 1; i >= 0; i--) {
                    pilha.push(rhs.get(i));
                }
            }
        }

        System.out.println("Sintaxe válida com sucesso! (LL(1))");
    }

    private void imprimirPasso(Deque<Simbolo> pilha, int currentIndex, String acao) {
        List<Simbolo> baseParaTopo = new ArrayList<>(pilha);
        Collections.reverse(baseParaTopo);
        String pilhaStr = baseParaTopo.stream()
                .map(Simbolo::getRotulo)
                .collect(Collectors.joining(" "));

        StringBuilder entradaStr = new StringBuilder();
        for (int i = currentIndex; i < tokens.size(); i++) {
            entradaStr.append(tokens.get(i).getText()).append(" ");
        }
        entradaStr.append("$");

        System.out.printf("  %-38s | %-28s | %s%n",
                truncar(pilhaStr, 38),
                truncar(entradaStr.toString(), 28),
                acao);
    }

    private String truncar(String texto, int limite) {
        if (texto.length() <= limite) {
            return texto;
        }
        return texto.substring(0, limite - 3) + "...";
    }

    private void lancarErro(ManualLispParser.MyToken encontrado, String detalhe) {
        String textoEncontrado = (encontrado != null) ? encontrado.getText() : "EOF (fim da entrada)";
        int linha = (encontrado != null) ? encontrado.getLine() : 0;
        int coluna = (encontrado != null) ? encontrado.getCharPositionInLine() : 0;

        throw new RuntimeException("Erro sintático LL(1) na linha " + linha +
                ", coluna " + coluna + ": " + detalhe + ", mas encontrou '" + textoEncontrado + "'");
    }
}
