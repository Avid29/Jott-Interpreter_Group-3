package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import Interpreter.ProgramTree.Nodes.TypeNode;
import provided.Token;
import provided.TokenType;

public class NumberNode extends OperandNodeBase {

    private final Token number;

    public NumberNode(Token number) {
        this.number = number;
    }

    public static NumberNode parseNode(TokenStack tokens){
        tokens.pushStack();

        var next = tokens.popToken();
        if (next.getTokenType() != TokenType.NUMBER) {

            ErrorReport.makeError(ErrorReportSyntax.class, "Expected NUMBER", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new NumberNode(next);
    }

    @Override
    public String convertToJott() {
        return number.getToken();
    }

    @Override
    public TypeNode getType() {
        
        String numberString = number.getToken();
        String typeNameOut = (numberString.contains(".")) ? "Double" : "Integer";

        return new TypeNode(
            new Token(typeNameOut, number.getFilename(), number.getLineNum(), TokenType.KEYWORD)
        );

    }

    @Override
    public boolean validateTree() {
        return true;
    }

    @Override
    public Number evaluate() {

        String numberString = number.getToken();

        //Number is an Integer
        if (!numberString.contains("."))
            return Integer.valueOf(numberString);

        //Number is a Double
        return Double.valueOf(numberString);

        
    }
}
