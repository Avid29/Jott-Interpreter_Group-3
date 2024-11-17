package Interpreter.ProgramTree.Nodes.StatementNodes;

import java.util.ArrayList;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.ProgramSymbolTable;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import provided.Token;
import provided.TokenType;

public class AssignmentNode extends BodyStatementNodeBase {
    private Token target;
    private ExpressionNodeBase expression;

    public AssignmentNode(Token target, ExpressionNodeBase expression){
        this.target = target;
        this.expression = expression;
    }

    public static AssignmentNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        ArrayList<Token> popped = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(new TokenType[] { TokenType.ID, TokenType.ASSIGN }, popped);
        if (errorCode != -1) {

            //ErrorReport.makeError(ErrorReportSyntax.class, "AssignmentNode -- Expected ID or ASSIGN", TokenStack.get_last_token_popped());

            // Let the parent function handle reporting this error.
            tokens.popStack(true);
            return null;

        }

        var id = popped.get(0);

        var expression = ExpressionNodeBase.parseNode(tokens);
        if (expression == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "AssignmentNode -- Invalid Assignment Statement", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        // Ensure semi-colon
        var statementEnd = tokens.popToken();
        if (statementEnd.getTokenType() != TokenType.SEMICOLON) {

            ErrorReport.makeError(ErrorReportSyntax.class, "AssignmentNode -- Expected Semicolon ';', got "+statementEnd.getTokenType(), statementEnd);

            tokens.popStack(true);
            return null;
        }

        //Check if the type of the resolved expression matches the symbol it is being assigned to
        String expressionTypeName = expression.getType().getType().getToken();
        if (!ProgramSymbolTable.tokenHasMatchingType(id, expressionTypeName)) {

            String idType = ProgramSymbolTable.getSymbolType(id);

            ErrorReport.makeError(ErrorReportSyntax.class, "AssignmentNode -- Invalid assignment, expecting "+idType+" and got "+expressionTypeName, id);
            tokens.popStack(true);
            return null;

        }

        tokens.popStack(false);
        return new AssignmentNode(id, expression);
    }

    @Override
    public String convertToJott() {
        return target.getToken() + " = " + expression.convertToJott() + ";";
    }
}
