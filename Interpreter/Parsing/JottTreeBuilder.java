package Interpreter.Parsing;

import Interpreter.ProgramTree.Nodes.Abstract.IBranchNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ProgramNode;

public class JottTreeBuilder {
    private ProgramNode root;
    private IBranchNode activeNode;

    public JottTreeBuilder() {
        root = new ProgramNode();
        activeNode = root;
    }

    public boolean InsertNode(NodeBase node) {
        var groups = node.getMeta().getGroups();
        if (!groups.contains(activeNode.getExpectedGroup())) {
            // This node is not a valid type to insert here
            return false;
        }

        activeNode.AddChild(node);
        return true;
    }

    public void SelectNode(IBranchNode newActive) {
        this.activeNode = newActive;
    }

    public ProgramNode getTree() {
        return root;
    }
}
