package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSemantic;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.FunctionSymbolTable;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import Interpreter.ProgramTree.Nodes.SymbolInfo;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.ProgramSymbolTable;
import provided.Token;
import provided.TokenType;


public class VarRefNode extends OperandNodeBase {

    private final Token id;
    private String type;

    public VarRefNode(Token id) {
        this.id = id;
        this.type = null;
    }

    public Token getIdToken() {
        return id;
    }

    public static VarRefNode parseNode(TokenStack tokens){
        tokens.pushStack();

        var next = tokens.popToken();
        if (next.getTokenType() != TokenType.ID) {

            ErrorReport.makeError(ErrorReportSyntax.class, "VarRefNode -- Expected ID token, got "+next.getTokenType(), TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new VarRefNode(next);
    }

    @Override
    public String convertToJott() {
        return id.getToken();
    }

    @Override
    public TypeNode getType() {

        String symbolVariableType = ProgramSymbolTable.getSymbolType(id, true);

        //If the symbol is not a variable in the symbol table, check if it is a parameter in the function symbol table
        if (symbolVariableType == null) {

            //System.out.println("[EX]  Symbol not found in program symbol table: "+id.getToken());

            Token lastPoppedFunctionToken = TokenStack.get_last_function_token_popped();
            if (lastPoppedFunctionToken != null) {

                //System.out.println("[EX]  Checking function symbol table for symbol: "+id.getToken() +" in function: "+lastPoppedFunctionToken.getToken());
                symbolVariableType = FunctionSymbolTable.getSymbolType(lastPoppedFunctionToken, id);

            }

            if (symbolVariableType == null) {
                
                //System.out.println("[EX]  Symbol not found in function symbol table: "+id.getToken());
                ErrorReport.makeError(ErrorReportSemantic.class, "Symbol not found in program or function symbol table: "+id.getToken(), id);
                return null;

            }

        }

        return new TypeNode(
            new Token(symbolVariableType, id.getFilename(), id.getLineNum(), TokenType.KEYWORD)
        );

    }

    public SymbolInfo getSymbolInfoFromId() {
        String currentFunctionName = (FunctionSymbolTable.isExecuting ? FunctionSymbolTable.getLastExecuted() : FunctionSymbolTable.getLastParsed());
        return ProgramSymbolTable.getSymbol(id, currentFunctionName);
    }


    @Override
    public boolean validateTree() {

        SymbolInfo info = ProgramSymbolTable.getSymbol(id);
        
        if (info == null) {
            ErrorReport.makeError(ErrorReportSyntax.class, "Use of undefined variable '" + id.getToken() + "'", id);
            return false;
        }
        type = info.getIdToken().getToken();

        return true;

    }

    @Override
    public Object evaluate(String scopeFunctionName) {

        //Default to the caller's function name if scopeFunctionName is null
        if (scopeFunctionName == null)
            scopeFunctionName = FunctionSymbolTable.getCurrentFunctionScope();
        
        String symbolName = id.getToken();
        String symbolNameAltered = scopeFunctionName + "__" + symbolName;
        //System.out.println("[EX]  Evaluating 'VarRefNode' with ID symbol: '" + symbolName + "' (altered: '" + symbolNameAltered + "')");

        SymbolInfo symbolInfo = ProgramSymbolTable.getSymbol(id, scopeFunctionName);

        if (symbolInfo == null) {
            ErrorReport.makeError(ErrorReportSemantic.class, "'VarRefNode' (evaluate) -- Symbol not found in table: '" + symbolName + "' (altered: '" + symbolNameAltered + "')", id);
            return null;
        }

        if (symbolInfo.getValue() == null) {
            ErrorReport.makeError(ErrorReportSemantic.class, "'VarRefNode' (evaluate) -- Symbol value is null for symbol: '" + symbolName + "'", id);
            return null;
        }

        return symbolInfo.getValueRaw();
        
    }


    @Override
    public Object evaluate() {
        String currentFunctionName = FunctionSymbolTable.isExecuting ? FunctionSymbolTable.getLastExecuted() : FunctionSymbolTable.getLastParsed();
        return evaluate(currentFunctionName);
    }

    public Object fetchRawValue() {

        SymbolInfo symbolInfo = ProgramSymbolTable.getSymbol(id);

        if (symbolInfo == null) {
            ErrorReport.makeError(ErrorReportSemantic.class, "'VarRefNode' (fetchRawValue) -- Symbol not found in table: "+id.getToken(), id);
            return null;
        }

        return symbolInfo.getValueRaw();

    }

    public Number fetchRawNumericValue() {

        SymbolInfo symbolInfo = ProgramSymbolTable.getSymbol(id);

        if (symbolInfo == null) {
            ErrorReport.makeError(ErrorReportSemantic.class, "'VarRefNode' (fetchRawNumericValue) -- Symbol not found in table: "+id.getToken(), id);
            return null;
        }

        Object valueOut = symbolInfo.getValueRaw();

        if (valueOut instanceof Number number)
            return number;

        return null;

    }

}
