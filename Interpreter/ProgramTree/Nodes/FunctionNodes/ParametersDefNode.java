package Interpreter.ProgramTree.Nodes.FunctionNodes;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;
import provided.Token;
import provided.TokenType;

public class ParametersDefNode extends NodeBase<ParametersDefNode> {
    private ArrayList<VariableDeclarationNode> paramNodes;

    public ParametersDefNode(ArrayList<VariableDeclarationNode> paramNodes) {
        this.paramNodes = paramNodes;
    }

    public static ParametersDefNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        ArrayList<VariableDeclarationNode> paramNodes = new ArrayList<>();

        Token curr = tokens.popToken();
        while (curr != null && curr.getTokenType() == TokenType.COMMA) {
            ArrayList<Token> pops = new ArrayList<>();
            int errorCode = tokens.tokenSequenceMatch(
                    new TokenType[] { TokenType.ID, TokenType.SEMICOLON, TokenType.KEYWORD }, pops);

            String error = switch (errorCode) {
                case -1 -> null;
                case 0 -> "Expected parameter identifier";
                case 1 -> "Expected ':";
                case 2 -> "Expected parameter type";
                default -> "Unknown error";
            };

            if (error != null) {
                tokens.popStack(true);
                return null;
            }

            TypeNode type = new TypeNode(pops.getLast());
            VarRefNode name = new VarRefNode(pops.getFirst());

            paramNodes.add(new VariableDeclarationNode(type, name, true));
            curr = tokens.popToken();
        }

        if (curr.getTokenType() != TokenType.R_BRACKET) {
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new ParametersDefNode(paramNodes);
    }
}
