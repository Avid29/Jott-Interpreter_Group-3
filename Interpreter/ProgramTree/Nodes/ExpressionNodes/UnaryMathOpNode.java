package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import java.util.EnumSet;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Enums.NodeTypeGroup;
import Interpreter.ProgramTree.Nodes.Abstract.IBranchNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.TokenType;

public class UnaryMathOpNode extends OperandNodeBase implements IBranchNode {
    private NumberNode child;

    public UnaryMathOpNode(NumberNode child) {
        super(NodeType.OPERATOR);

        this.child = child;
    }

    @Override
    public void AddChild(NodeBase child) {
        if (!NumberNode.class.isInstance(child)) {
            // This isn't supposed to happen
        }

        NumberNode node = (NumberNode) child;
        if (this.child != null) {
            // This isn't supposed to happen either.
        }

        this.child = node;
        child.setParent(this);
    }

    @Override
    public NodeTypeGroup getExpectedGroup() {
        // It is my strong conviction that this operator should be valid on all
        // operands, not just numbers. It is such a strong conviction, that I refuse to
        // add a NUMBER group, and I will instead hard-code in an exception later.
        return NodeTypeGroup.OPERAND;
    }
    
    @Override
    public TokenType getClosureType() {
        return TokenType.SEMICOLON;
    }

    @Override
    public boolean isComplete() {
        return child != null;
    }
}
