// Generated from /media/nickzs/D/Repositorios/LIspGrammar/array-example/src/main/antlr4/org/abcd/examples/ArrayInit/Lisp.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class LispLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PAR_ABRE=1, PAR_FECHA=2, BOOLEAN=3, STRING=4, NUMBER=5, SYMBOL=6, WS=7;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"PAR_ABRE", "PAR_FECHA", "BOOLEAN", "STRING", "NUMBER", "SYMBOL", "DIGITS", 
			"DIGIT", "INITIAL_CHAR", "SYMBOL_CHAR", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PAR_ABRE", "PAR_FECHA", "BOOLEAN", "STRING", "NUMBER", "SYMBOL", 
			"WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public LispLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Lisp.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0007R\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002"+
		"$\b\u0002\u0001\u0003\u0001\u0003\u0005\u0003(\b\u0003\n\u0003\f\u0003"+
		"+\t\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0003\u00040\b\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0003\u00045\b\u0004\u0001\u0005\u0001"+
		"\u0005\u0005\u00059\b\u0005\n\u0005\f\u0005<\t\u0005\u0001\u0006\u0004"+
		"\u0006?\b\u0006\u000b\u0006\f\u0006@\u0001\u0007\u0001\u0007\u0001\b\u0003"+
		"\bF\b\b\u0001\t\u0001\t\u0003\tJ\b\t\u0001\n\u0004\nM\b\n\u000b\n\f\n"+
		"N\u0001\n\u0001\n\u0000\u0000\u000b\u0001\u0001\u0003\u0002\u0005\u0003"+
		"\u0007\u0004\t\u0005\u000b\u0006\r\u0000\u000f\u0000\u0011\u0000\u0013"+
		"\u0000\u0015\u0007\u0001\u0000\u0005\u0001\u0000\"\"\u0002\u0000++--\u0001"+
		"\u000009\u0007\u0000!!*+--//<?AZaz\u0003\u0000\t\n\r\r  W\u0000\u0001"+
		"\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005"+
		"\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001"+
		"\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\u0015\u0001"+
		"\u0000\u0000\u0000\u0001\u0017\u0001\u0000\u0000\u0000\u0003\u0019\u0001"+
		"\u0000\u0000\u0000\u0005#\u0001\u0000\u0000\u0000\u0007%\u0001\u0000\u0000"+
		"\u0000\t/\u0001\u0000\u0000\u0000\u000b6\u0001\u0000\u0000\u0000\r>\u0001"+
		"\u0000\u0000\u0000\u000fB\u0001\u0000\u0000\u0000\u0011E\u0001\u0000\u0000"+
		"\u0000\u0013I\u0001\u0000\u0000\u0000\u0015L\u0001\u0000\u0000\u0000\u0017"+
		"\u0018\u0005(\u0000\u0000\u0018\u0002\u0001\u0000\u0000\u0000\u0019\u001a"+
		"\u0005)\u0000\u0000\u001a\u0004\u0001\u0000\u0000\u0000\u001b$\u0005T"+
		"\u0000\u0000\u001c\u001d\u0005N\u0000\u0000\u001d\u001e\u0005I\u0000\u0000"+
		"\u001e$\u0005L\u0000\u0000\u001f \u0005#\u0000\u0000 $\u0005t\u0000\u0000"+
		"!\"\u0005#\u0000\u0000\"$\u0005f\u0000\u0000#\u001b\u0001\u0000\u0000"+
		"\u0000#\u001c\u0001\u0000\u0000\u0000#\u001f\u0001\u0000\u0000\u0000#"+
		"!\u0001\u0000\u0000\u0000$\u0006\u0001\u0000\u0000\u0000%)\u0005\"\u0000"+
		"\u0000&(\b\u0000\u0000\u0000\'&\u0001\u0000\u0000\u0000(+\u0001\u0000"+
		"\u0000\u0000)\'\u0001\u0000\u0000\u0000)*\u0001\u0000\u0000\u0000*,\u0001"+
		"\u0000\u0000\u0000+)\u0001\u0000\u0000\u0000,-\u0005\"\u0000\u0000-\b"+
		"\u0001\u0000\u0000\u0000.0\u0007\u0001\u0000\u0000/.\u0001\u0000\u0000"+
		"\u0000/0\u0001\u0000\u0000\u000001\u0001\u0000\u0000\u000014\u0003\r\u0006"+
		"\u000023\u0005.\u0000\u000035\u0003\r\u0006\u000042\u0001\u0000\u0000"+
		"\u000045\u0001\u0000\u0000\u00005\n\u0001\u0000\u0000\u00006:\u0003\u0011"+
		"\b\u000079\u0003\u0013\t\u000087\u0001\u0000\u0000\u00009<\u0001\u0000"+
		"\u0000\u0000:8\u0001\u0000\u0000\u0000:;\u0001\u0000\u0000\u0000;\f\u0001"+
		"\u0000\u0000\u0000<:\u0001\u0000\u0000\u0000=?\u0003\u000f\u0007\u0000"+
		">=\u0001\u0000\u0000\u0000?@\u0001\u0000\u0000\u0000@>\u0001\u0000\u0000"+
		"\u0000@A\u0001\u0000\u0000\u0000A\u000e\u0001\u0000\u0000\u0000BC\u0007"+
		"\u0002\u0000\u0000C\u0010\u0001\u0000\u0000\u0000DF\u0007\u0003\u0000"+
		"\u0000ED\u0001\u0000\u0000\u0000F\u0012\u0001\u0000\u0000\u0000GJ\u0003"+
		"\u0011\b\u0000HJ\u0003\u000f\u0007\u0000IG\u0001\u0000\u0000\u0000IH\u0001"+
		"\u0000\u0000\u0000J\u0014\u0001\u0000\u0000\u0000KM\u0007\u0004\u0000"+
		"\u0000LK\u0001\u0000\u0000\u0000MN\u0001\u0000\u0000\u0000NL\u0001\u0000"+
		"\u0000\u0000NO\u0001\u0000\u0000\u0000OP\u0001\u0000\u0000\u0000PQ\u0006"+
		"\n\u0000\u0000Q\u0016\u0001\u0000\u0000\u0000\n\u0000#)/4:@EIN\u0001\u0006"+
		"\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}