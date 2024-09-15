package provided;

/**
 * This represents the types of tokens that can be in Jott.
 */
public enum TokenType {
    COMMA,
    R_BRACKET,
    L_BRACKET,
    R_BRACE,
    L_BRACE,
    ASSIGN,
    REL_OP,
    MATH_OP,
    SEMICOLON,
    NUMBER,
    ID_KEYWORD,
    COLON,
    STRING,
    FC_HEADER,

    // The parser uses 2 passes, where in the first IDs and Keywords are
    // differentiated. 
    ID,
    KEYWORD,
}
