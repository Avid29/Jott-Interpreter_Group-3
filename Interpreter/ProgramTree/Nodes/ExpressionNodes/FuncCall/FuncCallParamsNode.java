package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import provided.Token;
import provided.TokenType;

public class FuncCallParamsNode extends NodeBase<FuncCallParamsNode> {
    private ArrayList<ExpressionNodeBase> paramNodes;

    public FuncCallParamsNode(ArrayList<ExpressionNodeBase> paramNodes) {
        this.paramNodes = paramNodes;
    } 

    public static FuncCallParamsNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        ArrayList<ExpressionNodeBase> params = new ArrayList<>(); 
        if (tokens.popToken().getTokenType() != TokenType.L_BRACKET){
            tokens.popStack(true);
            return null;
        }

        Token popped = null;
        do {
            ExpressionNodeBase expression = ExpressionNodeBase.parseNode(tokens);
            if (expression == null)
            {
                tokens.popStack(true);
                return null;
            }

            params.add(expression);
            popped = tokens.popToken();
        }
        while(popped.getTokenType() == TokenType.COMMA);

        if (popped.getTokenType() != TokenType.R_BRACKET) {
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new FuncCallParamsNode(params);
    }

    @Override
    public String convertToJott() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToJott'");
    }
}
