package Interpreter.ProgramTree.Nodes.FunctionNodes;

import java.util.ArrayList;

import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionRefNode;
import provided.Token;
import provided.TokenType;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.BodyNode;

public class FunctionDefNode extends NodeBase<FunctionDefNode> {
    private FunctionRefNode funcName;
    private ParametersDefNode params;
    private BodyNode body;

    public FunctionDefNode(FunctionRefNode name, ParametersDefNode params, BodyNode body) {
        this.funcName = name;
        this.params = params;
        this.body = body;
    }

    public static FunctionDefNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        // Perform mential tokenType checks on function id/definition.
        ArrayList<Token> pops = new ArrayList<>();

        // TODO: Replace with error objects
        int errorCode = tokens.tokenSequenceMatch(
                new TokenType[] { TokenType.KEYWORD, TokenType.ID, TokenType.L_BRACKET }, pops);
        String error = switch (errorCode) {
            case -1 -> null;
            case 0 -> "Expected \"Def\" keyword";
            case 1 -> "Expected function identifier";
            case 2 -> "Expected function parameters";
            default -> "Unknown error";
        };

        if (error != null) {
            tokens.popStack(true);
            return null;
        }

        // TODO: Process identifier from popped tokens
        if (!pops.get(0).getToken().equals("Def")) {
            tokens.popStack(true);
            return null;
        }

        FunctionRefNode identifier = new FunctionRefNode(pops.get(1));

        // Parse parameters
        ParametersDefNode paramsNode = ParametersDefNode.parseNode(tokens);

        // TODO: Replace with error objects
        pops = new ArrayList<>();
        errorCode = tokens
                .tokenSequenceMatch(new TokenType[] { TokenType.COLON, TokenType.KEYWORD, TokenType.L_BRACE }, pops);
        error = switch (errorCode) {
            case -1 -> null;
            case 0 -> "Expected ':' keyword";
            case 1 -> "Expected function return type";
            case 2 -> "'{'";
            default -> "Unknown error";
        };

        if (error != null) {
            tokens.popStack(true);
            return null;
        }

        BodyNode fBody = BodyNode.parseNode(tokens, true);

        tokens.popStack(false);
        return new FunctionDefNode(identifier, paramsNode, fBody);
    }

    @Override
    public String convertToJott() {
        // TODO: Return type and body.
        return "Def " + funcName.convertToJott() + params.convertToJott();
    }

}
