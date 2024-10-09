package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import provided.Token;
import provided.TokenType;

public class BoolNode extends ExpressionNodeBase {
    private Token value;

    public BoolNode(Token value) {
        this.value = value;
    }

    public static BoolNode parseNode(TokenStack tokens){
        tokens.pushStack();

        var next = tokens.popToken();
        if (next.getTokenType() != TokenType.KEYWORD) {
            tokens.popStack(true);
            return null;
        }

        // TODO: Ensure keyword is a real boolean

        tokens.popStack(false);
        return new BoolNode(next);
    }
}
