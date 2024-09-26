package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;

public class BinaryMathOpNode extends ExpressionNodeBase {
    private OperandNodeBase leftChild;
    private OperandNodeBase rightChild;

    public BinaryMathOpNode(OperandNodeBase leftChild, OperandNodeBase rightChild) {
        super(NodeType.OPERATOR);

        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
}
