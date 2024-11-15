package Interpreter.ProgramTree.Nodes.FunctionNodes;

import java.util.ArrayList;

import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionRefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.ReturnStatementNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.ElseIfBlockNode;
import provided.Token;
import provided.TokenType;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.TypeNode;

public class FunctionDefNode extends NodeBase<FunctionDefNode> {
    public FunctionRefNode funcName;//changed all this to public
    public ParametersDefNode params;
    public TypeNode returnType;
    public BodyNode body;

    public FunctionDefNode(FunctionRefNode name, ParametersDefNode params, TypeNode returnType, BodyNode body) {
        this.funcName = name;
        this.params = params;
        this.returnType = returnType;
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
        if (paramsNode == null)
        {
            tokens.popStack(true);
            return null;
        }

        // TODO: Replace with error objects
        pops = new ArrayList<>();
        errorCode = tokens
                .tokenSequenceMatch(new TokenType[] { TokenType.COLON, TokenType.KEYWORD}, pops);
        error = switch (errorCode) {
            case -1 -> null;
            case 0 -> "Expected ':' keyword";
            case 1 -> "Expected function return type";
            default -> "Unknown error";
        };

        if (error != null) {
            tokens.popStack(true);
            return null;
        }

        TypeNode returnTypeNode = new TypeNode(pops.get(1));

        BodyNode fBody = BodyNode.parseNode(tokens, true);
        if (fBody == null){
            tokens.popStack(true);
            return null;
        }

        return new FunctionDefNode(identifier, paramsNode, returnTypeNode, fBody);
    }

    public Token getName(){
        return funcName.getId();
    }

    public TypeNode getReturnType() {
        return returnType;
    }
    
    @Override
	public boolean validateTree() {
        // Validate body
        if (!body.validateTree())
            return false;

        // Ensure contains return when non-void.
        if (!returnType.getType().getToken().equals("Void") && !body.containsReturn())
            return false;

        return true;
	}

    @Override
    public String convertToJott() {
        // TODO: Return type and body.
        return "Def " + funcName.convertToJott() + "[" + params.convertToJott() + "]:" +
            returnType.convertToJott() + body.convertToJott();
    }
}
