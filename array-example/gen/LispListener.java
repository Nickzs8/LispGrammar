// Generated from /media/nickzs/D/Repositorios/LIspGrammar/array-example/src/main/antlr4/org/abcd/examples/ArrayInit/Lisp.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LispParser}.
 */
public interface LispListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LispParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(LispParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(LispParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#sExpr}.
	 * @param ctx the parse tree
	 */
	void enterSExpr(LispParser.SExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#sExpr}.
	 * @param ctx the parse tree
	 */
	void exitSExpr(LispParser.SExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(LispParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(LispParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#elements}.
	 * @param ctx the parse tree
	 */
	void enterElements(LispParser.ElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#elements}.
	 * @param ctx the parse tree
	 */
	void exitElements(LispParser.ElementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(LispParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(LispParser.AtomContext ctx);
}