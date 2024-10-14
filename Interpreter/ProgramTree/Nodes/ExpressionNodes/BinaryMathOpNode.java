package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.TokenType;

public class BinaryMathOpNode extends ExpressionNodeBase {
    private OperandNodeBase leftChild;
    private OperandNodeBase rightChild;

    public BinaryMathOpNode(OperandNodeBase leftChild, OperandNodeBase rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public static BinaryMathOpNode parseNode(TokenStack stack){
        var result = ExpressionNodeBase.parseOperatorNode(stack, TokenType.MATH_OP);
        if (result == null)
            return null;

        return new BinaryMathOpNode(result.Item1, result.Item2);
    }
}
