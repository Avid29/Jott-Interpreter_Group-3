package Interpreter.ProgramTree.Nodes.Abstract;

import Interpreter.ProgramTree.NodeMeta;
import Interpreter.ProgramTree.Enums.NodeType;
import provided.JottTree;

public abstract class NodeBase implements JottTree {
    private IBranchNode parent;
    private NodeType type;

    public NodeBase(NodeType type) {
        this.type = type;
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
    
    public NodeMeta getMeta() {
        return new NodeMeta(type);
    }

    public IBranchNode getParent() {
        return parent;
    }

    public void setParent(IBranchNode parent) {
        this.parent = parent;
    }
}
