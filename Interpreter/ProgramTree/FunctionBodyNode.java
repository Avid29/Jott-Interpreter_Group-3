package Interpreter.ProgramTree;

import Interpreter.Parsing.TokenStack;
import provided.JottTree;

public class FunctionBodyNode implements JottTree {
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
    
    public static FunctionBodyNode parse(TokenStack tokens) {
        tokens.pushStack();

        // Parse the variable definitions at the top of the function body.
        VariableDefinitionNode varDefNode = null;
        do {
            varDefNode = VariableDefinitionNode.parse(tokens);
        } while(varDefNode != null);

        // Parse the remainder of the function body.

        tokens.popStack(false);
        return null;
    }
}
