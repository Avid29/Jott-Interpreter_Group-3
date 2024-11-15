package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import java.util.ArrayList;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
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

            ErrorReport.makeError(ErrorReportSyntax.class, "FuncCallParamsNode -- Expected Opening Bracket '['", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        Token popped = null;
        do {
            ExpressionNodeBase expression = ExpressionNodeBase.parseNode(tokens);
            if (expression == null) {

                ErrorReport.makeError(ErrorReportSyntax.class, "FuncCallParamsNode -- Failed to parse expression", TokenStack.get_last_token_popped());

                tokens.popStack(true);
                return null;
            }

            params.add(expression);
            popped = tokens.popToken();
        }
        while(popped.getTokenType() == TokenType.COMMA);

        if (popped.getTokenType() != TokenType.R_BRACKET) {

            ErrorReport.makeError(ErrorReportSyntax.class, "FuncCallParamsNode -- Expected Closing Bracket ']'", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new FuncCallParamsNode(params);
    }

    @Override
    public String convertToJott() {
        String output = "";
        for (int i = 0; i < paramNodes.size(); i++) {
            if (i != 0){
                output += ",";
            }

            output += paramNodes.get(i).convertToJott(); 
        }
        return output;
    }
}
