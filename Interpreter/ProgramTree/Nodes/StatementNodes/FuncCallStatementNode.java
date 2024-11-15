package Interpreter.ProgramTree.Nodes.StatementNodes;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionCallNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import provided.TokenType;

public class FuncCallStatementNode extends BodyStatementNodeBase {
    private FunctionCallNode wrapped;

    public FuncCallStatementNode(FunctionCallNode wrapped) {
        this.wrapped = wrapped;
    }

    public static FuncCallStatementNode parseNode(TokenStack tokens) {
        
        tokens.pushStack();
        FunctionCallNode funcCall = FunctionCallNode.parseNode(tokens);
        
        if (funcCall == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "FuncCallStatementNode -- Function call is null", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;

        }

        // Ensure semi-colon
        var statementEnd = tokens.popToken();

        if (statementEnd.getTokenType() != TokenType.SEMICOLON) {

            ErrorReport.makeError(ErrorReportSyntax.class, "FuncCallStatementNode -- Expected semicolon ';', got "+statementEnd.getTokenType(), TokenStack.get_last_token_popped());
            
            tokens.popStack(true);
            return null;

        }

        tokens.popStack(false);
        return new FuncCallStatementNode(funcCall);
    }

    @Override
    public String convertToJott() {
        return wrapped.convertToJott() + ";";
    }
}
