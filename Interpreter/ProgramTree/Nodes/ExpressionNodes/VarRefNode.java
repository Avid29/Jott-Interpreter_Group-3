package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.Token;
import provided.TokenType;

public class VarRefNode extends OperandNodeBase {
    private Token id;

    public VarRefNode(Token id) {
        this.id = id;
    }

    public Token getIdToken() {
        return id;
    }

    public static VarRefNode parseNode(TokenStack tokens){
        tokens.pushStack();

        var next = tokens.popToken();
        if (next.getTokenType() != TokenType.ID) {

            ErrorReport.makeError(ErrorReportSyntax.class, "VarRefNode -- Expected ID token, got "+next.getTokenType(), TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new VarRefNode(next);
    }

    @Override
    public String convertToJott() {
        return id.getToken();
    }
}
