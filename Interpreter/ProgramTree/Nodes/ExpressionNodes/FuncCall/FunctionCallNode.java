package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import java.util.ArrayList;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import ErrorReporting.ErrorReportSemantic;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.FunctionSymbolTable;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.Token;
import provided.TokenType;
import Interpreter.ProgramTree.Nodes.TypeNode;

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
            new TokenType[] { TokenType.FC_HEADER, TokenType.ID},
            pops
        );

        //Malformed function call
        if (errorCode != -1) {

            ErrorReport.makeError(ErrorReportSyntax.class, "FunctionCallNode -- Got malformed function call", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        FunctionRefNode fRefNode = new FunctionRefNode(pops.get(1));

        
        //Called function is not defined yet!
        String funcName = fRefNode.getId().getToken();
        if (!FunctionSymbolTable.programContainsFunction(funcName)) {

            ErrorReport.makeError(ErrorReportSemantic.class, "FunctionCallNode -- Tried to call an undeclared function: "+funcName, fRefNode.getId());

            tokens.popStack(true);
            return null;

        }

        FuncCallParamsNode fParamsNode = FuncCallParamsNode.parseNode(tokens);
        if (fParamsNode == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "FunctionCallNode -- Failed to parse function call parameters", TokenStack.get_last_token_popped());
            return null;

        }


        //Ensure param types match function definition
        if (!FunctionSymbolTable.functionParamsMatch(funcName, fParamsNode)) {

            ErrorReport.makeError(ErrorReportSyntax.class, "FunctionCallNode -- Function call parameters do not match function definition", fRefNode.getId());

            tokens.popStack(true);
            return null;

        }

        tokens.popStack(false);
        return new FunctionCallNode(fRefNode, fParamsNode);
    }

    @Override
    public String convertToJott() {
        return "::" + funcName.convertToJott() + "[" + callParams.convertToJott() + "]";
    }

    @Override
    public TypeNode getType() {
        return FunctionSymbolTable.getFunctionType(funcName.getId().getToken());
    }
}
