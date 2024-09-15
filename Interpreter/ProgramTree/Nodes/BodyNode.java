package Interpreter.ProgramTree.Nodes;

import java.util.ArrayList;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Enums.NodeTypeGroup;
import Interpreter.ProgramTree.Nodes.Abstract.IBranchNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import provided.TokenType;

public class BodyNode extends NodeBase implements IBranchNode {
    private ArrayList<BodyStatementNodeBase> statements;

    public BodyNode() {
        super(NodeType.BODY);

        statements = new ArrayList<>();
    }

    @Override
    public void AddChild(NodeBase child) {
        if (!BodyStatementNodeBase.class.isInstance(child)) {
            // This isn't supposed to happen
        }

        BodyStatementNodeBase node = (BodyStatementNodeBase)child;
        statements.add(node);
    }

    @Override
    public NodeTypeGroup getExpectedGroup() {
        return NodeTypeGroup.BODY_STATEMENT;
    }
    
    @Override
    public TokenType getClosureType() {
        return TokenType.R_BRACE;
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}
