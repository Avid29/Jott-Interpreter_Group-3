package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.Token;
import provided.TokenType;

public class BinaryMathOpNode extends ExpressionNodeBase {
    private Token op;
    private OperandNodeBase leftChild;
    private OperandNodeBase rightChild;

    public BinaryMathOpNode(Token op, OperandNodeBase leftChild, OperandNodeBase rightChild) {
        this.op = op; 
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public static BinaryMathOpNode parseNode(TokenStack stack){

        var result = ExpressionNodeBase.parseOperatorNode(stack, TokenType.MATH_OP);

        if (result == null)
            return null;

        return new BinaryMathOpNode(result.Item1, result.Item2, result.Item3);
    }

    @Override
    public String convertToJott() {
        return leftChild.convertToJott() + op.getToken() + rightChild.convertToJott();
    }
}
