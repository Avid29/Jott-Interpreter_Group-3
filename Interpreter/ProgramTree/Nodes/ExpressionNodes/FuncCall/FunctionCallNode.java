package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.Token;
import provided.TokenType;

public class FunctionCallNode extends OperandNodeBase {
    private FunctionRefNode funcName;
    private FuncCallParamsNode callParams;

    public FunctionCallNode(FunctionRefNode name, FuncCallParamsNode params) {
        this.funcName = name;
        this.callParams = params;
    }

    public static FunctionCallNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        ArrayList<Token> pops = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(
                new TokenType[] { TokenType.FC_HEADER, TokenType.ID}, pops);

        if (errorCode != -1) {
            // Malformed function call
            tokens.popStack(true);
            return null;
        }

        FunctionRefNode fRefNode = new FunctionRefNode(pops.get(1));

        FuncCallParamsNode fParamsNode = FuncCallParamsNode.parseNode(tokens);
        if (fParamsNode == null) {
            return null;
        }

        tokens.popStack(false);
        return new FunctionCallNode(fRefNode, fParamsNode);
    }
}
