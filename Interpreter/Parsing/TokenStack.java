package Interpreter.Parsing;

import java.util.ArrayList;
import java.util.Stack;

import provided.Token;
import provided.TokenType;

public class TokenStack {
    private ArrayList<Token> tokens;
    private Stack<Integer> tokenUseStack;
    private int offset;
    private int stackOffset;

    public TokenStack(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.tokenUseStack = new Stack<>();
        this.offset = 0;
        this.stackOffset = 0;
    }

    public Token peekToken() {
        if (tokens.size() <= offset) {
            return null;
        }

        return tokens.get(offset);
    }

    public Token popToken() {
        Token token = peekToken();
        if (token == null)
            return null;

        offset++;
        return token;
    }

    public void pushStack() {

        int frameOffset = offset - stackOffset;
        tokenUseStack.push(frameOffset);
        stackOffset = offset;
    }

    public void popStack(boolean backtracking) {
        if (backtracking) {
            offset = stackOffset;
        }

        stackOffset += tokenUseStack.pop();
    }

    public int tokenSequenceMatch(TokenType[] typeCheck, ArrayList<Token> popped) {
        if (popped.isEmpty()) {
            // This is an error code specifying the popped Deque was not empty
            // In a good language popped would just be an out parameter, but alas, this is
            // Java
            return -2;
        }

        for (int i = 0; i < typeCheck.length; i++) {

            // Pop the next token, and track that it was popped
            Token curr = popToken();
            popped.addLast(curr);

            if (curr == null || curr.getTokenType() != typeCheck[i]) {
                // Token missing or tokenType mismatch,
                // Return the position of the error
                return i;
            }
        }

        // This is a sucess code
        return -1;
    }
}
