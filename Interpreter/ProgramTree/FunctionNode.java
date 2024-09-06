package Interpreter.ProgramTree;

import provided.JottTree;
import provided.Token;
import provided.TokenType;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;

public class FunctionNode implements JottTree {

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

    public static FunctionNode parse(TokenStack tokens) {
        tokens.pushStack();

        // Perform mential tokenType checks on function id/definition.
        ArrayList<Token> pops = new ArrayList<>();

        // TODO: Replace with error objects
        int errorCode = tokens.tokenSequenceMatch(new TokenType[] { TokenType.ID_KEYWORD, TokenType.ID_KEYWORD, TokenType.L_BRACKET }, pops);
        String error = switch (errorCode) {
            case -1 -> null;
            case 0 -> "Expected \"Def\" keyword";
            case 1 -> "Expected function identifier";
            case 2 -> "Expected function parameters";
            default -> "Unknown error";
        };

        if (error != null) {
            tokens.popStack(true);
            return null;
        }

        // TODO: Process identifier from popped tokens

        // Parse parameters
        ParametersNode paramsNode = ParametersNode.parse(tokens);

        // TODO: Replace with error objects
        errorCode = tokens.tokenSequenceMatch(new TokenType[] { TokenType.COLON, TokenType.ID_KEYWORD, TokenType.L_BRACE }, pops);
        error = switch (errorCode) {
            case -1 -> null;
            case 0 -> "Expected ':' keyword";
            case 1 -> "Expected function return type";
            case 2 -> "'{'";
            default -> "Unknown error";
        };

        if (error != null) {
            tokens.popStack(true);
            return null;
        }

        // TODO: Parse body

        tokens.popStack(false);
        return null;
    }

}
