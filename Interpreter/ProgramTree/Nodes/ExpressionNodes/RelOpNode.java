package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.TokenType;

public class RelOpNode extends ExpressionNodeBase {
    private OperandNodeBase leftChild;
    private OperandNodeBase rightChild;

    public RelOpNode(OperandNodeBase leftChild, OperandNodeBase rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public static RelOpNode parseNode(TokenStack stack){
        var result = ExpressionNodeBase.parseOperatorNode(stack, TokenType.REL_OP);
        if (result == null)
            return null;

        return new RelOpNode(result.Item1, result.Item2);
    }
}
