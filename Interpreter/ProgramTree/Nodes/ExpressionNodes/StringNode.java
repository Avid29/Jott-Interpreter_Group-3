package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import provided.Token;
import provided.TokenType;

public class StringNode extends ExpressionNodeBase {
    private Token string;

    public StringNode(Token string) {
        this.string = string;
    }

    public static StringNode parseNode(TokenStack tokens){
        tokens.pushStack();

        var next = tokens.popToken();
        if (next.getTokenType() != TokenType.STRING) {

            ErrorReport.makeError(ErrorReportSyntax.class, "Expected STRING", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new StringNode(next);
    }

    @Override
    public String convertToJott() {
        return string.getToken();
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public String getType() {
        return "String";
    }
}
