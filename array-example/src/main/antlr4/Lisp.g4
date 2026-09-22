grammar Lisp;

// ===== Regras Sintáticas (Parser) =====
sExpr    : atom | list ;

list     : PAR_ABRE elements PAR_FECHA
         | PAR_ABRE PAR_FECHA
         ;

elements : sExpr elements
         | sExpr
         ;

atom     : NUMBER
         | SYMBOL
         | STRING
         | BOOLEAN
         ;

// ===== Regras Léxicas (Lexer) — ordem importa! =====
PAR_ABRE  : '(' ;
PAR_FECHA : ')' ;

BOOLEAN   : 'T' | 'NIL' | '#t' | '#f' ;   // deve vir ANTES de SYMBOL

STRING    : '"' ~["]* '"' ;

NUMBER    : ('+' | '-')? DIGITS ('.' DIGITS)? ;

SYMBOL    : INITIAL_CHAR SYMBOL_CHAR* ;   // vem DEPOIS de BOOLEAN

fragment DIGITS        : DIGIT+ ;
fragment DIGIT          : [0-9] ;
fragment INITIAL_CHAR   : [a-zA-Z] | '+' | '-' | '*' | '/' | '=' | '<' | '>' | '!' | '?' ;
fragment SYMBOL_CHAR    : INITIAL_CHAR | DIGIT ;

WS        : [ \t\r\n]+ -> skip ;