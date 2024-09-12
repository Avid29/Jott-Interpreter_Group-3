package Interpreter.ProgramTree.StatementNodes;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import provided.JottTree;
import provided.Token;
import provided.TokenType;

public class VariableDefinitionNode implements JottTree {
    private Token type;
    private Token name;

    public VariableDefinitionNode(Token type, Token name) {
        this.type = type;
        this.name = name;
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
