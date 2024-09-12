package Interpreter.ProgramTree.FunctionNodes;

import provided.JottTree;
import provided.Token;

public class ParameterDefNode implements JottTree {
    private Token name;
    private Token type;

    public ParameterDefNode(Token name, Token type){
        this.name = name;
        this.type = type;
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
