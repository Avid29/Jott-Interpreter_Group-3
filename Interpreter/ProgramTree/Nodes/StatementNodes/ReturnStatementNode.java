package Interpreter.ProgramTree.Nodes.StatementNodes;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.FunctionSymbolTable;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.ReturnException;
import provided.Token;
import provided.TokenType;

public class ReturnStatementNode extends BodyStatementNodeBase {

    private final ExpressionNodeBase expression;

    public ReturnStatementNode(ExpressionNodeBase expression){
        this.expression = expression;
    }

    public static ReturnStatementNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        Token next = tokens.popToken();

        // Ensure return
        if (!next.getToken().equals("Return")){

            ErrorReport.makeError(ErrorReportSyntax.class, "ReturnStatementNode -- Expected 'Return', got "+next.getTokenType(), next);

            tokens.popStack(true);
            return null;
        }

        // Get expression
        var expression = ExpressionNodeBase.parseNode(tokens);
        if (expression == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "ReturnStatementNode -- Missing Expression", next);

            tokens.popStack(true);
            return null;
        }

        // Ensure semi-colon
        var statementEnd = tokens.popToken();
        if (statementEnd.getTokenType() != TokenType.SEMICOLON) {

            ErrorReport.makeError(ErrorReportSyntax.class, "ReturnStatementNode -- Expected Semicolon ';', got "+statementEnd.getTokenType(), next);

            tokens.popStack(true);
            return null;
        }

        //Check if the returned value matches the return type of the function
        String lastFunctionPoppedName = TokenStack.get_last_function_token_popped().getToken();
        TypeNode returnType = FunctionSymbolTable.getFunctionType(lastFunctionPoppedName);
        String returnTypeName = returnType.getType().getToken();
        String expressionTypeName = expression.getType().getType().getToken();
        if (!returnTypeName.equals(expressionTypeName)) {

            ErrorReport.makeError(ErrorReportSyntax.class, "ReturnStatementNode -- Return type mismatch: expected "+returnTypeName+", got "+expressionTypeName, next);

            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new ReturnStatementNode(expression);
    }

    @Override
    public String convertToJott() {
        return "Return " + expression.convertToJott() + ";";
    }


    @Override
    public void execute() {

        Object returnValue = expression.evaluate();

        throw new ReturnException(returnValue);
    }

}
