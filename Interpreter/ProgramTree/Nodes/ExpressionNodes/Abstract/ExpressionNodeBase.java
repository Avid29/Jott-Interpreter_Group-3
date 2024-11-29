package Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.BinaryMathOpNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.BoolNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.RelOpNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.StringNode;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.Tuple3;
import java.lang.reflect.Method;
import provided.Token;
import provided.TokenType;

public abstract class ExpressionNodeBase extends NodeBase<ExpressionNodeBase> {


    public static boolean gotEmptyParams = false;


    public abstract TypeNode getType();
    
    public static ExpressionNodeBase parseNode(TokenStack tokens, boolean allowEmptyForFunctionParams) {
        
        Token next = tokens.peekToken();
        //System.out.println("[EX]  Next Token, TokenType: "+next.getToken() + ", "+ next.getTokenType());

        ExpressionNodeBase result = switch (next.getTokenType()) {
            case TokenType.STRING -> StringNode.parseNode(tokens);
            case /*TokenType.ID*/ TokenType.KEYWORD -> BoolNode.parseNode(tokens);
            default -> null;
        };


        final Class<?>[] expressionOperations = { BinaryMathOpNode.class, RelOpNode.class, OperandNodeBase.class };
        for (Class<?> operationClass : expressionOperations) {

            //Found result, break
            if (result != null)
                break;

            try {
                
                Method parseMethod = operationClass.getMethod("parseNode", TokenStack.class);
                result = (ExpressionNodeBase) parseMethod.invoke(null, tokens);

            }

            //Reflection exceptions
            catch (Exception e) {
                /* e.printStackTrace(); */
            }

        }


        //Check that expression type is not null
        if (result != null) {

            TypeNode resultType = result.getType();
            if (resultType == null) {

                ErrorReport.makeError(
                    ErrorReportSyntax.class,
                    "ExpressionNodeBase -- Fetched expression result with a 'null' type: '"+result.convertToJott()+"'",
                    TokenStack.get_last_token_popped()
                );

                return null;
            }

        }


        //Check for empty function parameters (e.g. function[ ])
        Token postExpression = tokens.peekToken();
        //System.out.println("[EX]  Post Expression Token: " + postExpression.getToken());

        if (result == null) {

            if (allowEmptyForFunctionParams && postExpression.getTokenType() == TokenType.R_BRACKET) {
                gotEmptyParams = true;
                return null;
            }

            ErrorReport.makeError(ErrorReportSyntax.class, "ExpressionNodeBase -- Invalid or missing Expression", TokenStack.get_last_token_popped());

        }

        return result;
        
    } 

    public static ExpressionNodeBase parseNode(TokenStack tokens) {
        return parseNode(tokens, false);
    }

    protected static Tuple3<Token, OperandNodeBase, OperandNodeBase> parseOperatorNode(TokenStack stack, TokenType type){
        
        stack.pushStack();

        OperandNodeBase left = OperandNodeBase.parseNode(stack);
        if (left == null){
            stack.popStack(true);
            return null;
        }

        var op = stack.popToken();
        if (op.getTokenType() != type)
        {
            stack.popStack(true);
            return null;
        }

        OperandNodeBase right = OperandNodeBase.parseNode(stack);
        if (right == null){
            stack.popStack(true);
            return null;
        }

        String leftTypeName = left.getType().getType().getToken();
        String rightTypeName = right.getType().getType().getToken();

        if (!leftTypeName.equals(rightTypeName)) {
            ErrorReport.makeError(ErrorReportSyntax.class, "ExpressionNodeBase -- Type mismatch: " + leftTypeName + " and " + rightTypeName, TokenStack.get_last_token_popped());
            stack.popStack(true);
            return null;
        }
        
        stack.popStack(false);
        return new Tuple3<>(op, left, right);
    }


    @Override
    public void execute() {
        /* ... */
    }

    public boolean evaluateBoolean() {
        return false;
    }

    public Object evaluate(String scopeFunctionName) {
        return null;
    }
    public Object evaluate() {
        return evaluate(null);
    }
    
}
