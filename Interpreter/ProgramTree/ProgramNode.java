package Interpreter.ProgramTree;

import provided.JottTree;

import Interpreter.Parsing.TokenStack;

public class ProgramNode implements JottTree {

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

    public static ProgramNode parse(TokenStack tokens) {

        tokens.pushStack();
        FunctionNode node = null;
        do
        {
            node = FunctionNode.parse(tokens);
        } while (node != null);

        return null;
    }
    
}
