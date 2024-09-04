package src.Interpreter.Tokenization;

import src.Interpreter.TokenType;

public class TokenizerStateHandler {
    private TokenizerState newState;
    private TokenType createdTokenType;
    private boolean tokenEnd;

    public TokenizerStateHandler(TokenizerState newState, TokenType createdTokenType, boolean tokenEnd)
    {
        this.newState = newState;
        this.createdTokenType = createdTokenType;
        this.tokenEnd = tokenEnd;
    }

    public TokenizerState getNewState()
    {
        return newState;
    }

    public TokenType getCreatedTokenType()
    {
        return createdTokenType;
    }

    public boolean tokenEnd()
    {
        return tokenEnd;
    }
}
