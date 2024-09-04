package src.Interpreter.Tokenization;

/**
 * This represents the states of tokenizer.
 */
public enum TokenizerState {
        START, // This is our default state. We return to this state after we complete a token.
               // This is not marked by a character, but rather an implicit step if we discover
               // a character not explicitly handled by the current state.

        ASSIGN, // We enter this state after seeing a '='. From here we can either return to
                // start handle a relOp of "==".

        REL_OP, // We enter this state after seeing a '<' or '>'. From here we can either return
                // to start or handle a relOp of "<=" or ">=".

        DECIMAL, // We enter this state after seeing a '.'. From here we must see a digit, or we
                 // will fault.

        INTERGER, // We enter this state after seeing a digit. From here we can either build an
                  // integer, build a float, or return to start.

        FLOAT, // We enter this state after seeing a '.' from the integer state or a digit from
               // the decimal state. From here we can either continue to build a float, or
               // return to start.

        ID, // We enter this state after seeing our first letter. From here we can either
            // build an id/keyword, or return to start.

        COLON, // We enter this state after seeing a ':'. From here we can either make a
               // function call or return to start.

        BANG, // We enter this state after seeing a '!'. From here we must see a '=' or there
              // will be a syntax error.

        STRING, // We enter this state after seeing a '"'. All subsequent letters, digits, and
                // spaces are a part of that string until the close '"'. Any other characters,
                // including a newline, result in a syntax error.

        COMMENT, // We enter this state after seeing a '#'. We will remain in this state until
                 // the next newline. When we leave this state we will not create a token of its
                 // content.

        DONE, // We enter this state after finalizeTokenization is called and the final token
              // is added to the list. No new tokens can be added from this state.

        FAULTED; // Enter this state if we encounter a syntax error. No new tokens can be added
                 // from this state.
}
