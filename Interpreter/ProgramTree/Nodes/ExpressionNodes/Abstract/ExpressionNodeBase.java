package Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.StringNode;
import provided.Token;
import provided.TokenType;

public abstract class ExpressionNodeBase extends NodeBase<ExpressionNodeBase> {
    public static ExpressionNodeBase parseNode(TokenStack tokens) {
        tokens.pushStack();

        Token next = tokens.popToken();

        // Handle string literal
        if (next.getTokenType() == TokenType.STRING) {
            tokens.popStack(false);
            return StringNode.parseNode(tokens);
        }

        tokens.popStack(false);
        return null;
    } 
}
