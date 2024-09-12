package Interpreter.ProgramTree.FunctionNodes;

import java.util.ArrayList;

import provided.JottTree;

public class ParametersDefNode implements JottTree {
    private ArrayList<ParameterDefNode> paramNodes;

    public ParametersDefNode(ArrayList<ParameterDefNode> paramNodes) {
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
