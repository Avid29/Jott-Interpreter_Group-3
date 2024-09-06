package Interpreter.ProgramTree;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class VariableDefinitionNode implements JottTree {

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

    public static VariableDefinitionNode parse(TokenStack tokens) {
        tokens.pushStack();

        ArrayList<Token> popped = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(
                new TokenType[] { TokenType.ID_KEYWORD, TokenType.ID_KEYWORD, TokenType.SEMICOLON }, popped);

        if (errorCode != -1) {
            // No need to write an error message because the line still may be valid. We
            // just need to backtrack and pass it along to the function body.
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return null;
    }
}
