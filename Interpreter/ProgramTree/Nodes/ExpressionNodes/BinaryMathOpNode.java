package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;

public class BinaryMathOpNode extends ExpressionNodeBase {
    private OperandNodeBase leftChild;
    private OperandNodeBase rightChild;

    public BinaryMathOpNode(OperandNodeBase leftChild, OperandNodeBase rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
}
