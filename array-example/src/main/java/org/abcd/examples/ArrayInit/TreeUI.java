package org.abcd.examples.ArrayInit;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import javax.swing.*;
import java.util.Arrays;

public class TreeUI {
    public void mostrarArvore(String entrada) {
        CharStream input = CharStreams.fromString(entrada);
        LispLexer lexer = new LispLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LispParser parser = new LispParser(tokens);

        ParseTree tree = parser.start();

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
