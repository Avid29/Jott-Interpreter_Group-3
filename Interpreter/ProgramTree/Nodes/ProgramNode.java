package Interpreter.ProgramTree.Nodes;

import java.util.ArrayList;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Enums.NodeTypeGroup;
import Interpreter.ProgramTree.Nodes.Abstract.IBranchNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.FunctionNodes.FunctionNode;
import provided.TokenType;

public class ProgramNode extends NodeBase implements IBranchNode {
    private ArrayList<FunctionNode> funcNodes;

    public ProgramNode(){
        this(new ArrayList<>());
    }

    public ProgramNode(ArrayList<FunctionNode> funcNodes){
        super(NodeType.PROGRAM);

        this.funcNodes = funcNodes;
    }

    @Override
    public void AddChild(NodeBase child) {
        if (!FunctionNode.class.isInstance(child)) {
            // This isn't supposed to happen
        }

        funcNodes.add((FunctionNode)child);
        child.setParent(this);
    }

    @Override
    public NodeTypeGroup getExpectedGroup() {
        return NodeTypeGroup.FUNCTION;
    }
    
    @Override
    public TokenType getClosureType() {
        return null;
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}
