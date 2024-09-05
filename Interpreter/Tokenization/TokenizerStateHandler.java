package Interpreter.Tokenization;

import provided.TokenType;

public class TokenizerStateHandler {
    private TokenizerState newState;
    private TokenType createdTokenType;

    public TokenizerStateHandler(TokenizerState newState, TokenType createdTokenType) {
        this.newState = newState;
        this.createdTokenType = createdTokenType;
    }

    public TokenizerState getNewState() {
        return newState;
    }

    public TokenType getCreatedTokenType() {
        return createdTokenType;
    }

    public boolean tokenEnd() {
        // If the resulting state is start, we know that this handler clears the token
        return newState == TokenizerState.START;
    }
}
