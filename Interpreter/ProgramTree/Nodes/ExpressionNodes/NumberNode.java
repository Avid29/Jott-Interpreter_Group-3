package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.Token;
import provided.TokenType;

public class NumberNode extends OperandNodeBase {
    private Token number;

    public NumberNode(Token number) {
        this.number = number;
    }

    public static NumberNode parseNode(TokenStack tokens){
        tokens.pushStack();

        var next = tokens.popToken();
        if (next.getTokenType() != TokenType.NUMBER) {
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new NumberNode(next);
    }
}
