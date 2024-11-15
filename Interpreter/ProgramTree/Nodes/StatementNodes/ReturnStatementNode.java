package Interpreter.ProgramTree.Nodes.StatementNodes;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import provided.Token;
import provided.TokenType;

public class ReturnStatementNode extends BodyStatementNodeBase {
    private ExpressionNodeBase expression;

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

        tokens.popStack(false);
        return new ReturnStatementNode(expression);
    }

    @Override
    public String convertToJott() {
        return "Return " + expression.convertToJott() + ";";
    }
}
