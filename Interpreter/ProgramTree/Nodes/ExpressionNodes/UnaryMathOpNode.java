package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.TokenType;
import Interpreter.ProgramTree.Nodes.TypeNode;

public class UnaryMathOpNode extends OperandNodeBase {

    private NumberNode child;

    public UnaryMathOpNode(NumberNode child) {
        this.child = child;
    }

    public static UnaryMathOpNode parseNode(TokenStack tokens) {
        tokens.pushStack();
        var next = tokens.popToken();

        if (next.getTokenType() != TokenType.MATH_OP && !next.getToken().equals("-")) {

            ErrorReport.makeError(ErrorReportSyntax.class, "Expected MATH_OP", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        NumberNode number = NumberNode.parseNode(tokens);
        if (number == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "Expected number", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new UnaryMathOpNode(number);
    }

    @Override
    public String convertToJott() {
        return "-" + child.convertToJott();
    }

    @Override
    public TypeNode getType() {
        return child.getType();
    }

    @Override
    public boolean validateTree() {
        return child.validateTree();
    }

}
