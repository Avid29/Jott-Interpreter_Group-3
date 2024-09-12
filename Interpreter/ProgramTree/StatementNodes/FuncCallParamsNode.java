package Interpreter.ProgramTree.StatementNodes;

import java.util.ArrayList;

import provided.JottTree;

public class FuncCallParamsNode implements JottTree {
    private ArrayList<FuncCallParamNode> paramNodes;

    public FuncCallParamsNode(ArrayList<FuncCallParamNode> paramNodes){
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
}
