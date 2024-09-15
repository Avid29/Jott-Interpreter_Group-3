package Interpreter.ProgramTree.Nodes.StatementNodes.Abstract;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Enums.NodeTypeGroup;
import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.Abstract.IBranchNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import provided.TokenType;

public abstract class BlockDeclareNodeBase extends BodyStatementNodeBase implements IBranchNode {
    public BlockDeclareNodeBase(NodeType type) {
        super(type);
    }

    protected ExpressionNodeBase condition;
    protected BodyNode body;

    @Override
    public void AddChild(NodeBase child) {
        switch (child) {
            case ExpressionNodeBase node:
                if (condition != null) {
                    // This isn't supposed to happen
                }

                condition = node;
                break;
            case BodyNode node:
                if (body != null) {
                    // This isn't supposed to happen
                }

                body = node;
                break;
        
            default:
                // This isn't supposed to happen either
                break;
        }

        child.setParent(this);
    }

    @Override
    public NodeTypeGroup getExpectedGroup() {
        return NodeTypeGroup.CONDITIONAL_BLOCK;
    }
    
    @Override
    public TokenType getClosureType() {
        return null;
    }

    @Override
    public boolean isComplete() {
        return condition != null && body != null;
    }
}
