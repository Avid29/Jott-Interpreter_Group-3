package Interpreter.ProgramTree.ExpressionNodes;

import provided.JottTree;

public class BinaryMathOpNode implements JottTree {
    private NumberNode leftChild;
    private NumberNode rightChild;

    public BinaryMathOpNode(NumberNode leftChild, NumberNode rightChild){
        this.leftChild = leftChild;
        this.rightChild = rightChild;
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
