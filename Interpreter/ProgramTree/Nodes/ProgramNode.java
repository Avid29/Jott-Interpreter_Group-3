package Interpreter.ProgramTree.Nodes;

import java.util.ArrayList;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.FunctionNodes.FunctionNode;

public class ProgramNode extends NodeBase {
    private ArrayList<FunctionNode> funcNodes;

    public ProgramNode(){
        this(new ArrayList<>());
    }

    public ProgramNode(ArrayList<FunctionNode> funcNodes){
        super(NodeType.PROGRAM);

        this.funcNodes = funcNodes;
    }
}
