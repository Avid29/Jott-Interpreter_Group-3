package Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionCallNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.NumberNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.UnaryMathOpNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import provided.Token;
import provided.TokenType;

public abstract class OperandNodeBase extends ExpressionNodeBase {

    public static OperandNodeBase parseNode(TokenStack tokens) {

        Token next = tokens.peekToken();
        TokenType nextTokenType = next.getTokenType();

        //System.out.println("[EX]  OperandNodeBase.parseNode() - next token: " + next.getToken() + " - " + nextTokenType);

        return switch (nextTokenType) {

            case TokenType.ID -> VarRefNode.parseNode(tokens);
            case TokenType.NUMBER -> NumberNode.parseNode(tokens);
            case TokenType.FC_HEADER -> FunctionCallNode.parseNode(tokens);
            case TokenType.MATH_OP -> UnaryMathOpNode.parseNode(tokens);

            default -> null;

        };

    }
    
}
