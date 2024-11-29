package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportRuntime;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import Interpreter.ProgramTree.Nodes.SymbolInfo;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.ProgramSymbolTable;
import provided.Token;
import provided.TokenType;

public class RelOpNode extends ExpressionNodeBase {

    private final Token op;
    private final OperandNodeBase leftChild;
    private final OperandNodeBase rightChild;

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

    @Override
    public boolean evaluateBoolean() {

        TypeNode type = getType();

        if (type == null) {
            //System.out.println("[EX]  RelOpNode -- Type is null");
            return false;
        }

        Token typeToken = type.getType();
        if (typeToken == null) {
            //System.out.println("[EX]  RelOpNode -- Type token is null");
            return false;
        }
        
        //System.out.println("[EX]  Evaluating boolean expression: '" + convertToJott()+"' with type: "+type.getType().getToken());

        //Integer Comparison
        if (type.getType().getToken().equals("Integer")) {

            int leftValue, rightValue;

            if (leftChild instanceof VarRefNode) {
                
                String symbolString = leftChild.convertToJott();
                SymbolInfo potentialSymbol = ProgramSymbolTable.fetchSymbolInfoFromName(symbolString);
                ExpressionNodeBase symbolValueNode;

                if (potentialSymbol != null) {

                    symbolValueNode = potentialSymbol.getValue();
                    leftValue = (Integer) symbolValueNode.evaluate();

                }
                else {
                    //System.err.println("[EX] (L) wtf");
                    return false;
                }

            } else {
                leftValue = (Integer) leftChild.evaluate();
            }


            if (rightChild instanceof VarRefNode) {
                
                String symbolString = rightChild.convertToJott();
                SymbolInfo potentialSymbol = ProgramSymbolTable.fetchSymbolInfoFromName(symbolString);
                ExpressionNodeBase symbolValueNode;

                if (potentialSymbol != null) {

                    symbolValueNode = potentialSymbol.getValue();
                    rightValue = (Integer) symbolValueNode.evaluate();

                }
                else {
                    //System.err.println("[EX] (R) wtf");
                    return false;
                }

            } else {
                rightValue = (Integer) rightChild.evaluate();
            }


            return switch (op.getToken()) {

                case "<" -> leftValue < rightValue;
                case "<=" -> leftValue <= rightValue;
                case ">" -> leftValue > rightValue;
                case ">=" -> leftValue >= rightValue;
                case "==" -> leftValue == rightValue;
                case "!=" -> leftValue != rightValue;

                default -> {
                    ErrorReport.makeError(ErrorReportSyntax.class, "RelOpNode -- Invalid operator (Integer)", op);
                    yield false;
                }
                    
            };


        }

        //Double Comparison
        else if (type.getType().getToken().equals("Double")) {

            double leftValue, rightValue;

            if (leftChild instanceof VarRefNode) {
                
                String symbolString = leftChild.convertToJott();
                SymbolInfo potentialSymbol = ProgramSymbolTable.fetchSymbolInfoFromName(symbolString);
                ExpressionNodeBase symbolValueNode;

                if (potentialSymbol != null) {

                    symbolValueNode = potentialSymbol.getValue();
                    leftValue = (Double) symbolValueNode.evaluate();
                    //System.out.println("[EX]  (L) Symbol Value Node: " + symbolValueNode.convertToJott());
                    
                }
                else {
                    //System.err.println("[EX] (L) wtf");
                    return false;
                }

            } else {
                //System.out.println("[EX]  (L) Not a VarRefNode: " + leftChild.convertToJott());
                leftValue = (Double) leftChild.evaluate();   
            }


            if (rightChild instanceof VarRefNode) {
                
                String symbolString = rightChild.convertToJott();
                SymbolInfo potentialSymbol = ProgramSymbolTable.fetchSymbolInfoFromName(symbolString);
                ExpressionNodeBase symbolValueNode;

                if (potentialSymbol != null) {

                    symbolValueNode = potentialSymbol.getValue();
                    //System.out.println("[EX]  (R) Symbol Value Node: " + symbolValueNode.convertToJott());
                    rightValue = (Double) symbolValueNode.evaluate();

                }
                else {
                    //System.err.println("[EX] (R) wtf");
                    return false;
                }

            } else {
                //System.out.println("[EX]  (R) Not a VarRefNode: " + rightChild.convertToJott());
                rightValue = (Double) rightChild.evaluate();
            }


            return switch (op.getToken()) {

                case "<" -> leftValue < rightValue;
                case "<=" -> leftValue <= rightValue;
                case ">" -> leftValue > rightValue;
                case ">=" -> leftValue >= rightValue;
                case "==" -> leftValue == rightValue;
                case "!=" -> leftValue != rightValue;

                default -> {
                    ErrorReport.makeError(ErrorReportRuntime.class, "RelOpNode -- Invalid operator (Double)", op);
                    yield false;
                }
                    
            };

        }

        //Boolean Comparison
        else if (type.getType().getToken().equals("Boolean")) {

            boolean leftValue = leftChild.evaluateBoolean();
            boolean rightValue = rightChild.evaluateBoolean();

            return switch (op.getToken()) {

                case "==" -> leftValue == rightValue;
                case "!=" -> leftValue != rightValue;

                default -> {
                    ErrorReport.makeError(ErrorReportRuntime.class, "RelOpNode -- Invalid operator (Boolean)", op);
                    yield false;
                }
                    
            };

        }

        //String Comparison
        else if (type.getType().getToken().equals("String")) {

            String leftValue = (String) leftChild.evaluate();
            String rightValue = (String) rightChild.evaluate();

            return switch (op.getToken()) {

                case "==" -> leftValue.equals(rightValue);
                case "!=" -> !leftValue.equals(rightValue);

                default -> {
                    ErrorReport.makeError(ErrorReportRuntime.class, "RelOpNode -- Invalid operator (String)", op);
                    yield false;
                }
                    
            };

        }

        //Invalid Type
        ErrorReport.makeError(ErrorReportRuntime.class, "RelOpNode -- Invalid type", typeToken);
        return false;
        
    }

}
