package src.Interpreter.Tokenization;

/**
 * This represents the states of tokenizer.
 */
public enum TokenizerState {
    START, // This is our default state. We return to this after we complete a token.
           //This is not marked by a character, but rather an implicit step if we discover a character not explicitly handled by the current state.

    ASSIGN, // We enter this state after seeing a '='. From here we can either return to start handle a relop of "==".
    REL_OP, // We enter this state after seeing a '<' or '>'. From here we can either return to start or handle a relop of "<=" or ">=". 
    DECIMAL, // We enter this state after seeing a '.'. From here we must see a digit, or we will fault.
    INTERGER, // We enter this state after a digit. From here we can either build an integer, build a float, or return to start. 
    FLOAT, // We enter this state after seeing a '.' from the integer state or a digit from the decimal state. From here we can either build an float, or return to start.
    ID, // We enter this state after seeing our first letter. From here we can either build an id/keyword, or return to start.
    COLON, // We enter this state after seeing a 
	BANG, 
    STRING,
    COMMENT,
    DONE,
    FAULTED;
}
