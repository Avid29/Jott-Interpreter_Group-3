package Interpreter.ProgramTree.StatementNodes;

import provided.JottTree;
import provided.Token;

public class FuncCallParamNode implements JottTree {
    private Token id;

    public FuncCallParamNode(Token id) {
        this.id = id;
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
    
}
