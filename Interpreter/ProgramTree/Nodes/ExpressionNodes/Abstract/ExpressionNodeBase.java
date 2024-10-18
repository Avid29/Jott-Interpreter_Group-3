package Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract;

import Interpreter.Tuple;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.BinaryMathOpNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.BoolNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.RelOpNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.StringNode;
import provided.Token;
import provided.TokenType;

public abstract class ExpressionNodeBase extends NodeBase<ExpressionNodeBase> {
    public static ExpressionNodeBase parseNode(TokenStack tokens) {
        Token next = tokens.peekToken();
        ExpressionNodeBase result = switch (next.getTokenType()) {
            case TokenType.STRING -> StringNode.parseNode(tokens);
            case TokenType.KEYWORD -> BoolNode.parseNode(tokens);
            default -> null;
        };

        if (result == null) {
            result = BinaryMathOpNode.parseNode(tokens);
        }
        if (result == null) {
            result = RelOpNode.parseNode(tokens);
        }
        if (result == null) {
            result = OperandNodeBase.parseNode(tokens);
        }

        return result;
    } 

    protected static Tuple<Token, OperandNodeBase, OperandNodeBase> parseOperatorNode(TokenStack stack, TokenType type){
        stack.pushStack();

        OperandNodeBase left = OperandNodeBase.parseNode(stack);
        if (left == null){
            stack.popStack(true);
            return null;
        }

        var op = stack.popToken();
        if (op.getTokenType() != type)
        {
            stack.popStack(true);
            return null;
        }

        OperandNodeBase right = OperandNodeBase.parseNode(stack);
        if (right == null){
            stack.popStack(true);
            return null;
        }

        stack.popStack(false);
        return new Tuple<>(op, left, right);
    }
}
