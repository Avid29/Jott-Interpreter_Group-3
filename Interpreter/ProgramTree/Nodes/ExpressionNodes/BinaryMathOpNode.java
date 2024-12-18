package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportRuntime;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import Interpreter.ProgramTree.Nodes.TypeNode;
import provided.Token;
import provided.TokenType;

public class BinaryMathOpNode extends ExpressionNodeBase {

    private final Token op;
    private final OperandNodeBase leftChild;
    private final OperandNodeBase rightChild;

    public BinaryMathOpNode(Token op, OperandNodeBase leftChild, OperandNodeBase rightChild) {

        this.op = op; 
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        
    }

    public static BinaryMathOpNode parseNode(TokenStack stack){

        var result = ExpressionNodeBase.parseOperatorNode(stack, TokenType.MATH_OP);

        if (result == null) {
            //ErrorReport.makeError(ErrorReportSyntax.class, "BinaryMathOpNode -- Failed to parse expression", TokenStack.get_last_token_popped());
            return null;
        }

        return new BinaryMathOpNode(result.Item1, result.Item2, result.Item3);
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
            ErrorReport.makeError(ErrorReportSyntax.class, "BinaryMathOpNode -- Type mismatch", op);
            return false;
        }

        if (op.getToken().equals("/") && rightChild.convertToJott().equals("0")) {
            ErrorReport.makeError(ErrorReportSyntax.class, "BinaryMathOpNode -- Division by 0", op);
            return false;
        }

        return true;
        
    }

    @Override
    public Number evaluate() {

        Object leftObj = leftChild.evaluate();
        Object rightObj = rightChild.evaluate();

        //System.out.println("[EX]  'BinaryMathOpNode (evaluate)' -- Left: " + leftObj.toString());
        //System.out.println("[EX]  'BinaryMathOpNode (evaluate)' -- Right: " + rightObj.toString());

        Number left = (Number) leftObj;
        Number right = (Number) rightObj;

        //Numbers are Integers
        if (left instanceof Integer && right instanceof Integer) {

            return switch (op.getToken()) {

                case "+" -> left.intValue() + right.intValue();
                case "-" -> left.intValue() - right.intValue();
                case "*" -> left.intValue() * right.intValue();
                case "/" -> left.intValue() / right.intValue();

                default -> null;

            };

        }

        //Numbers are Doubles
        if (left instanceof Double && right instanceof Double) {

            return switch (op.getToken()) {

                case "+" -> left.doubleValue() + right.doubleValue();
                case "-" -> left.doubleValue() - right.doubleValue();
                case "*" -> left.doubleValue() * right.doubleValue();
                case "/" -> left.doubleValue() / right.doubleValue();

                default -> null;

            };

        }

        ErrorReport.makeError(
            ErrorReportRuntime.class,
            "'BinaryMathOpNode (evaluate)' -- Type mismatch: " + left.getClass().getName() + " and " + right.getClass().getName(),
            TokenStack.get_last_token_popped()
        );
        return null;

    }

}
