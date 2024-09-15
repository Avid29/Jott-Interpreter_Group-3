package Interpreter.Parsing;

import provided.Token;

public abstract class ParserStateHandlerBase {
    public abstract void apply(JottTreeBuilder builder, Token token, TokenStack tokens);
}
