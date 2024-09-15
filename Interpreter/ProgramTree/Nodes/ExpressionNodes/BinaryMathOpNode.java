package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Enums.NodeTypeGroup;
import Interpreter.ProgramTree.Nodes.Abstract.IBranchNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.TokenType;

public class BinaryMathOpNode extends ExpressionNodeBase implements IBranchNode {
    private OperandNodeBase leftChild;
    private OperandNodeBase rightChild;

    public BinaryMathOpNode(OperandNodeBase leftChild, OperandNodeBase rightChild) {
        super(NodeType.OPERATOR);

        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public void AddChild(NodeBase child) {
        if (!OperandNodeBase.class.isInstance(child)) {
            // This isn't supposed to happen
        }

        OperandNodeBase node = (OperandNodeBase) child;
        if (leftChild == null) {
            leftChild = node;
        } else if (rightChild == null) {
            rightChild = node;
        } else {
            // This isn't supposed to happen either.
        }

        child.setParent(this);
    }

    @Override
    public NodeTypeGroup getExpectedGroup() {
        return NodeTypeGroup.OPERAND;
    }
    
    @Override
    public TokenType getClosureType() {
        return TokenType.SEMICOLON;
    }

    @Override
    public boolean isComplete() {
        return leftChild != null && rightChild != null;
    }
}
