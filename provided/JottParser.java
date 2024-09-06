package provided;

/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author Adam Dernis
 */

import java.util.ArrayList;
import java.util.Deque;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.ProgramNode;

public class JottParser {

    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * 
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens) {
        return ProgramNode.parse(new TokenStack(tokens));
    }

    public static int tokenSequenceMatch(Deque<Token> tokens, TokenType[] typeCheck, Deque<Token> popped) {
        if (popped.isEmpty()) {
            // This is an error code specifying the popped Deque was not empty
            // In a good language popped would just be an out parameter, but alas, this is Java
            return -2;
        }

        for (int i = 0; i < typeCheck.length; i++) {

            // Pop the next token, and track that it was popped
            Token curr = tokens.pop();
            popped.addLast(curr);

            if (curr == null || curr.getTokenType() != typeCheck[i]){
                // Token missing or tokenType mismatch, 
                // Return the position of the error
                return i;
            }
        }

        // This is a sucess code
        return -1;
    }
}
