package Interpreter.ProgramTree.Nodes;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.FunctionNodes.FunctionNode;

public class ProgramNode extends NodeBase<ProgramNode> {
    private ArrayList<FunctionNode> funcNodes;

    public ProgramNode(){
        this(new ArrayList<>());
    }

    public ProgramNode(ArrayList<FunctionNode> funcNodes){
        this.funcNodes = funcNodes;
    }

    public static ProgramNode parseProgram(TokenStack tokens) {
        tokens.pushStack();
        ArrayList<FunctionNode> funcNodes = new ArrayList<>();

        FunctionNode node = null;
        while (true) {
            node = FunctionNode.parseNode(tokens);
            if (node == null) {
                break;
            }

            funcNodes.add(node);
        }

        tokens.popStack(false);
        return new ProgramNode(funcNodes);
    }
}
