package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.TokenType;

public class UnaryMathOpNode extends OperandNodeBase {
    private NumberNode child;

    public UnaryMathOpNode(NumberNode child) {
        this.child = child;
    }

    public static UnaryMathOpNode parseNode(TokenStack tokens) {
        tokens.pushStack();
        var next = tokens.popToken();

        if (next.getTokenType() != TokenType.MATH_OP && !next.getToken().equals("-")) {
            tokens.popStack(true);
            return null;
        }

        NumberNode number = NumberNode.parseNode(tokens);
        if (number == null) {
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new UnaryMathOpNode(number);
    }
}
