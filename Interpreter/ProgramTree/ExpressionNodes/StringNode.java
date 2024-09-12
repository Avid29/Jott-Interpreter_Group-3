package Interpreter.ProgramTree.ExpressionNodes;

import provided.JottTree;
import provided.Token;

public class StringNode implements JottTree {
    private Token string;

    public StringNode(Token string){
        this.string = string;
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
