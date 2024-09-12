package Interpreter.ProgramTree;

import provided.JottParser;
import provided.JottTree;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.FunctionNodes.FunctionNode;

public class ProgramNode implements JottTree {
    private ArrayList<FunctionNode> funcNodes;

    public ProgramNode(ArrayList<FunctionNode> funcNodes){
        this.funcNodes = funcNodes;
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