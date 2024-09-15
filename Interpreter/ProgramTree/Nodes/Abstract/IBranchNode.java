package Interpreter.ProgramTree.Nodes.Abstract;

import Interpreter.ProgramTree.Enums.NodeTypeGroup;
import provided.TokenType;

public interface IBranchNode {
    public void AddChild(NodeBase child);

    // This defines what node group type are allowed when this is the active node
    // on the JottTreeBuilder.
    public NodeTypeGroup getExpectedGroup();

    public boolean isComplete(); 

    public TokenType getClosureType();
}
