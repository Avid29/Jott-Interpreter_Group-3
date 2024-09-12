package Interpreter.ProgramTree.StatementNodes;

import Interpreter.Parsing.TokenStack;
import provided.JottTree;

public class WhileBlockNode implements JottTree {
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
    
    public static WhileBlockNode parse(TokenStack tokens) {
        tokens.pushStack();

        tokens.popStack(false);
        return null;
    }
}
