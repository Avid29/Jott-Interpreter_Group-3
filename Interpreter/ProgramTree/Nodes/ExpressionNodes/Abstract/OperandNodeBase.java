package Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.NumberNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.UnaryMathOpNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionCallNode;
import provided.Token;
import provided.TokenType;

public class OperandNodeBase extends ExpressionNodeBase {
    public static OperandNodeBase parseNode(TokenStack tokens) {
        tokens.pushStack();

        Token next = tokens.popToken();

        OperandNodeBase result = switch (next.getTokenType()) {
            case TokenType.ID -> VarRefNode.parseNode(tokens);
            case TokenType.NUMBER -> NumberNode.parseNode(tokens);
            case TokenType.FC_HEADER -> FunctionCallNode.parseNode(tokens);
            case TokenType.MATH_OP -> UnaryMathOpNode.parseNode(tokens);
            default -> null;
        };

        if (result == null) {
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return result;
    } 
}
