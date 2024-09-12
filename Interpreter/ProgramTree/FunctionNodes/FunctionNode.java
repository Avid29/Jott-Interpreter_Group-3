package Interpreter.ProgramTree.FunctionNodes;

import provided.JottTree;
import provided.Token;

public class FunctionNode implements JottTree {
    private Token id;
    private ParametersDefNode params;
    private FunctionBodyNode body;

    public FunctionNode(Token id, ParametersDefNode params, FunctionBodyNode body){
        this.id = id;
        this.params = params;
        this.body = body;
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
