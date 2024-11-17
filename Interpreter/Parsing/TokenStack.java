package Interpreter.Parsing;

import java.util.ArrayList;
import java.util.Stack;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import provided.Token;
import provided.TokenType;

public class TokenStack {

    private static Token lastTokenPopped = null;
    private static Token lastFunctionTokenPopped = null;

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
        if (isEmpty()) {
            return null;
        }

        return tokens.get(offset);
    }

    public Token peekToken(int pOffset)
    {
        if (offset + pOffset >= tokens.size() || pOffset < -offset) {
            return null;
        }

        return tokens.get(offset + pOffset);
    }

    public Token popToken() {

        Token token = peekToken();
        if (token == null)
            return null;

        //Check if a function was popped...
        if (lastTokenPopped != null) {

            if ((lastTokenPopped.getTokenType() == TokenType.KEYWORD && lastTokenPopped.getToken().equals("Def")) && token.getTokenType() == TokenType.ID) {

                lastFunctionTokenPopped = token;
                //System.out.println("Function Name: " + token.getToken());
            }

        }

        //Record the last token popped (only if not null)
        lastTokenPopped = token;

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

        stackOffset -= tokenUseStack.pop();
    }

    public boolean isEmpty() {
        return offset >= tokens.size();
    }

    public int tokenSequenceMatch(TokenType[] typeCheck, ArrayList<Token> popped) {
        pushStack();
        if (popped != null && !popped.isEmpty()) {
            // This is an error code specifying the popped array was not empty
            // In a good language popped would just be an out parameter, but alas, this is
            // Java.
            return -2;
        }

        for (int i = 0; i < typeCheck.length; i++) {
            // Pop the next token, and track that it was popped
            Token curr = popToken();

            if (popped != null) {
                popped.addLast(curr);
            }

            if (curr == null || curr.getTokenType() != typeCheck[i]) {
                // Token missing or tokenType mismatch,
                // Return the position of the error.
                popStack(true);
                return i;
            }
        }

        // This is a sucess code
        popStack(false);
        return -1;
    }

    public static Token get_last_token_popped() {
        return lastTokenPopped;
    }
    public static Token get_last_function_token_popped() {
        return lastFunctionTokenPopped;
    }
    public static void clear_last_token_popped() {

        /*
            Need this 'cause having a token stored
            between tests caused erorrs to be
            reported for the wrong file...
        */

        lastTokenPopped = null;

        lastFunctionTokenPopped = null;

    }
}
