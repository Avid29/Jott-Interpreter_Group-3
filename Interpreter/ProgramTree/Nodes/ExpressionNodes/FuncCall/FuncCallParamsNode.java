package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportRuntime;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.FunctionSymbolTable;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.BoolNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.NumberNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.StringNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.FunctionNodes.ParametersDefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;
import Interpreter.ProgramTree.ProgramSymbolTable;
import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

public class FuncCallParamsNode extends NodeBase<FuncCallParamsNode> {
    
    private final ArrayList<ExpressionNodeBase> paramNodes;
    private String funcNameStr = null;

    public FuncCallParamsNode(ArrayList<ExpressionNodeBase> paramNodes, ArrayList<TokenType> tokenTypes) {
        this.paramNodes = paramNodes;
    } 

    public int getNumParams() {
        return paramNodes.size();
    }
    public boolean hasParams() {
        return !paramNodes.isEmpty();
    }
    public ExpressionNodeBase getParam(int index) {
        return paramNodes.get(index);
    }

    public static FuncCallParamsNode parseNode(TokenStack tokens) {
        
        tokens.pushStack();

        ArrayList<ExpressionNodeBase> params = new ArrayList<>(); 
        if (tokens.popToken().getTokenType() != TokenType.L_BRACKET){

            ErrorReport.makeError(ErrorReportSyntax.class, "FuncCallParamsNode -- Expected Opening Bracket '['", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        ArrayList<TokenType> tokenTypesOut = new ArrayList<>();

        Token popped = null;
        do {

            ExpressionNodeBase expression = ExpressionNodeBase.parseNode(tokens, true); //<-- Flag to allow expression to be empty
            if (expression == null && !ExpressionNodeBase.gotEmptyParams){

                ErrorReport.makeError(ErrorReportSyntax.class, "FuncCallParamsNode -- Failed to parse expression", TokenStack.get_last_token_popped());

                tokens.popStack(true);
                return null;

            }

            if (ExpressionNodeBase.gotEmptyParams)
                ExpressionNodeBase.gotEmptyParams = false;
            else
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
        return new FuncCallParamsNode(params, tokenTypesOut);
    }

    @Override
    public String convertToJott() {

        String output = "";
        for (int i = 0; i < paramNodes.size(); i++) {

            if (i != 0)
                output += ",";

            output += paramNodes.get(i).convertToJott(); 

        }
        
        return output;

    }

    public void setFuncName(String funcName) {
        this.funcNameStr = funcName;
    }

    @Override
    public void execute(String callerFunctionName) {

        String currentFunctionName = FunctionSymbolTable.getLastExecuted();
        //System.out.println("[EX]ecuting 'FuncCallParamsNode'...");

        boolean isBuiltInFunction = FunctionSymbolTable.isBuiltInFunction(funcNameStr);
        ParametersDefNode paramDefs = FunctionSymbolTable.getFunctionParameters(funcNameStr);

        int ind = 0;
        for (ExpressionNodeBase param : paramNodes) {

            //System.out.println("[EX] Executing Param ('" + param.getClass().getSimpleName() + "')");

            Object paramValue;
            if (param instanceof VarRefNode varRefParam) {

                //System.out.println("[EX] Param is a variable reference, evaluating...");
                
                //Always use caller's function name for built-in functions
                paramValue = varRefParam.evaluate(callerFunctionName);

            } else {
                param.execute();
                paramValue = param.evaluate();
            }

            if (paramValue == null) {

                ErrorReport.makeError(
                    ErrorReportRuntime.class,
                    "'FuncCallParamsNode' -- Parameter value is null for parameter '" + param.convertToJott() + "' in called function '" + callerFunctionName + "'",
                    TokenStack.get_last_token_popped()
                );
                return;

            }

            if (!isBuiltInFunction && paramDefs != null) {

                VariableDeclarationNode paramRef = paramDefs.getParam(ind++);
                ExpressionNodeBase literalNode = createLiteralNode(paramValue, paramRef.getType().getType().getToken());
                ProgramSymbolTable.setSymbolValue(paramRef.getName().getIdToken(), literalNode, currentFunctionName);
                //System.out.println("[EX] 'FuncCallParamsNode' -- Updated Value for '" + paramRef.getName().getIdToken().getToken() + "' -> " + paramValue);

            }

        }


    }



    private ExpressionNodeBase createLiteralNode(Object value, String type) {

        if (value == null)
            throw new RuntimeException("'FuncCallParamsNode' (createLiteralNode) -- Cannot create a literal node with null value for type: " + type);

        Token token;
        switch (type) {

            case "Integer":
            case "Double":
                token = new Token(value.toString(), "", -1, TokenType.NUMBER);
                return new NumberNode(token);

            case "String":
                token = new Token(value.toString(), "", -1, TokenType.STRING);
                return new StringNode(token);

            case "Boolean":
                token = new Token(value.toString(), "", -1, TokenType.KEYWORD);
                return new BoolNode(token);
                
            case "ANY":
                return null;

            default:
                throw new RuntimeException("'FuncCallParamsNode' (createLiteralNode) -- Unknown type: " + type);
                
        }

    }

}