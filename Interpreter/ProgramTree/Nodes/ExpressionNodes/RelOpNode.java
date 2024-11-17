package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.Token;
import provided.TokenType;
import Interpreter.ProgramTree.Nodes.TypeNode;

public class RelOpNode extends ExpressionNodeBase {

    private Token op;
    private OperandNodeBase leftChild;
    private OperandNodeBase rightChild;

    public RelOpNode(Token op, OperandNodeBase leftChild, OperandNodeBase rightChild) {
        this.op = op;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public static RelOpNode parseNode(TokenStack stack){

        var result = ExpressionNodeBase.parseOperatorNode(stack, TokenType.REL_OP);
        if (result == null) {
            
            //ErrorReport.makeError(ErrorReportSyntax.class, "RelOpNode -- Failed to parse expression", TokenStack.get_last_token_popped());
            return null;

        }

        return new RelOpNode(result.Item1, result.Item2, result.Item3);
    }

    @Override
    public String convertToJott() {
        return leftChild.convertToJott() + op.getToken() + rightChild.convertToJott();
    }

    @Override
    public TypeNode getType() {

        //Assuming both children have the same type
        return leftChild.getType();
    }
    

    @Override
    public boolean validateTree() {
        if (!leftChild.validateTree() || !rightChild.validateTree()) {
            return false;
        }

        if (!leftChild.getType().equals(rightChild.getType())) {
            ErrorReport.makeError(ErrorReportSyntax.class, "RelOpNode -- Type mismatch between left and right operands", op);
            return false;
        }

        return true;
    }

}
