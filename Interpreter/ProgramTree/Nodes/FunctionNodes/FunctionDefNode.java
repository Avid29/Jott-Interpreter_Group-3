package Interpreter.ProgramTree.Nodes.FunctionNodes;

import java.util.ArrayList;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionRefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.ReturnStatementNode;
import provided.Token;
import provided.TokenType;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.TypeNode;

public class FunctionDefNode extends NodeBase<FunctionDefNode> {
    private FunctionRefNode funcName;
    private ParametersDefNode params;
    private TypeNode returnType;
    private BodyNode body;

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

        String errorMessage;

        // TODO: Replace with error objects
        int errorCode = tokens.tokenSequenceMatch(new TokenType[] { TokenType.KEYWORD, TokenType.ID, TokenType.L_BRACKET }, pops);

        errorMessage = switch (errorCode) {
            case -1 -> null;
            case 0 -> "Expected \"Def\" keyword";
            //case 1 -> "Expected function identifier";
            case 1 -> ("Expected " + TokenType.ID + " got " + pops.get(1).getTokenType());
            case 2 -> "Expected function parameters";
            default -> "Unknown error";
        };

        if (errorMessage != null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "FunctionDefNode -- "+errorMessage, tokens.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        // TODO: Process identifier from popped tokens
        String nextTokenString = pops.get(0).getToken();
        if (!nextTokenString.equals("Def")) {

            ErrorReport.makeError(ErrorReportSyntax.class, "FunctionDefNode -- Expected 'Def'", pops.get(0));

            tokens.popStack(true);
            return null;
        }

        FunctionRefNode identifier = new FunctionRefNode(pops.get(1));

        // Parse parameters
        ParametersDefNode paramsNode = ParametersDefNode.parseNode(tokens);
        if (paramsNode == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "FunctionDefNode -- Failed to parse parameters", tokens.get_last_token_popped());

            tokens.popStack(true);
            return null;

        }

        // TODO: Replace with error objects
        pops = new ArrayList<>();
        errorCode = tokens.tokenSequenceMatch(new TokenType[] { TokenType.COLON, TokenType.KEYWORD}, pops);
        errorMessage = switch (errorCode) {
            case -1 -> null;
            case 0 -> "Expected ':' keyword";
            case 1 -> "Expected function return type";
            default -> "Unknown error";
        };

        if (errorMessage != null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "FunctionDefNode -- "+errorMessage, tokens.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        TypeNode returnTypeNode = new TypeNode(pops.get(1));

        BodyNode fBody = BodyNode.parseNode(tokens, true);
        if (fBody == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "FunctionDefNode -- Function body is null", tokens.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        return new FunctionDefNode(identifier, paramsNode, returnTypeNode, fBody);
    }

    public Token getName(){
        return funcName.getId();
    }

    @Override
    public String convertToJott() {
        // TODO: Return type and body.
        return "Def " + funcName.convertToJott() + "[" + params.convertToJott() + "]:" +
            returnType.convertToJott() + body.convertToJott();
    }

}
