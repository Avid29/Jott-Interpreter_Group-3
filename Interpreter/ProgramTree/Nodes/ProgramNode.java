package Interpreter.ProgramTree.Nodes;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.FunctionNodes.FunctionDefNode;

public class ProgramNode extends NodeBase<ProgramNode> {
    private ArrayList<FunctionDefNode> funcNodes;

    public ProgramNode(){
        this(new ArrayList<>());
    }

    public ProgramNode(ArrayList<FunctionDefNode> funcNodes){
        this.funcNodes = funcNodes;
    }

    public static ProgramNode parseProgram(TokenStack tokens) {
        tokens.pushStack();
        ArrayList<FunctionDefNode> funcNodes = new ArrayList<>();

        FunctionDefNode node = null;
        while (true) {
            node = FunctionDefNode.parseNode(tokens);
            if (node == null) {
                break;
            }

            funcNodes.add(node);
        }

        tokens.popStack(false);
        return new ProgramNode(funcNodes);
    }

    @Override
    public String convertToJott() {
        String output = "";
        for (FunctionDefNode node : funcNodes) {
            output += node.convertToJott() + "\n"; 
        }
        
        return output;
    }
}
