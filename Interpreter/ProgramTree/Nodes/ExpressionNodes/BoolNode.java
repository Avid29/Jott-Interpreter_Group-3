package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.TypeNode;
import provided.Token;
import provided.TokenType;

public class BoolNode extends ExpressionNodeBase {

    private final Token value;

    public BoolNode(Token value) {
        this.value = value;
    }

    public static BoolNode parseNode(TokenStack tokens){

        tokens.pushStack();

        var next = tokens.popToken();
        if (next.getTokenType() != TokenType.KEYWORD) {

            ErrorReport.makeError(ErrorReportSyntax.class, "Expected KEYWORD", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        if (!next.getToken().equals("True") && !next.getToken().equals("False")) {

            ErrorReport.makeError(ErrorReportSyntax.class, "Expected 'True' or 'False'", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new BoolNode(next);
        
    }

    @Override
    public String convertToJott() {
        return value.getToken();
    }

    @Override
    public TypeNode getType() {
        return new TypeNode(
            new Token("Boolean", value.getFilename(), value.getLineNum(), TokenType.KEYWORD)
        );
    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public Object evaluate() {
        return evaluateBoolean();
    }

    @Override
    public boolean evaluateBoolean() {
        return value.getToken().equals("True");
    }

}
