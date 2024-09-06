package Interpreter.ProgramTree;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class ParametersNode implements JottTree {
    private ArrayList<ParameterNode> paramNodes;

    public ParametersNode(ArrayList<ParameterNode> paramNodes) {
        this.paramNodes = paramNodes;
    }

    @Override
    public String convertToJott() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToJott'");
    }

    @Override
    public boolean validateTree() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateTree'");
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    public static ParametersNode parse(TokenStack tokens) {
        tokens.pushStack();

        ArrayList<ParameterNode> paramNodes = new ArrayList<>();

        Token curr = tokens.popToken();
        while (curr != null && curr.getTokenType() == TokenType.COMMA) {
            ArrayList<Token> pops = new ArrayList<>();
            int errorCode = tokens.tokenSequenceMatch(
                    new TokenType[] { TokenType.ID_KEYWORD, TokenType.SEMICOLON, TokenType.ID_KEYWORD }, pops);

            // TODO: Replace with error objects
            String error = switch (errorCode) {
                case -1 -> null;
                case 0 -> "Expected parameter identifier";
                case 1 -> "Expected ':";
                case 2 -> "Expected parameter type";
                default -> "Unknown error";
            };

            if (error != null) {
                tokens.popStack(true);
                return null;
            }

            paramNodes.add(new ParameterNode(pops.getFirst(), pops.getLast()));
            curr = tokens.popToken();
        }

        if (curr.getTokenType() != TokenType.R_BRACKET) {
            // Error: Expected parameters closing brosing.
            // TODO: Log error
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new ParametersNode(paramNodes);
    }
}
