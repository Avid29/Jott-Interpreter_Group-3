package Interpreter.ProgramTree;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportRuntime;
import Interpreter.ErrorReporting.ErrorReportSemantic;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.BoolNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.NumberNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.StringNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;
import Interpreter.ProgramTree.Nodes.SymbolInfo;
import Interpreter.ProgramTree.Nodes.TypeNode;
import java.util.HashMap;
import provided.JottParser;
import provided.Token;
import provided.TokenType;


public class ProgramSymbolTable {

    private static HashMap<String, SymbolInfo> table = new HashMap<>();

    public static void clearTable() { 

        table = new HashMap<>();

    }
    

    public static String getSymbolType(Token symbolToken, boolean allowUnsafeAccess) {

        String symbolName = symbolToken.getToken();
        String lastFunctionParsedName = (FunctionSymbolTable.isExecuting ? FunctionSymbolTable.getLastExecuted() : FunctionSymbolTable.getLastParsed());
        String symbolNameAltered = (lastFunctionParsedName + "__" + symbolName);

        //System.out.println("[EX]  Attempting to get symbol type (altered): '"+symbolName+"' (altered: '"+symbolNameAltered+"')");

        if (!table.containsKey(symbolNameAltered)) {

            if (!allowUnsafeAccess)
                ErrorReport.makeError(ErrorReportSemantic.class, "'ProgramSymbolTable' (getSymbolType) -- Symbol not found in table: "+symbolNameAltered,  symbolToken);
                
            return null;

        }

        return table.get(symbolNameAltered).getIdToken().getToken();

    }
    public static String getSymbolType(Token symbolToken) {

        return getSymbolType(symbolToken, false);

    }


    public static Object getSymbolValue(Token symbolToken) {

        String symbolName = symbolToken.getToken();
        String lastFunctionParsedName = (FunctionSymbolTable.isExecuting ? FunctionSymbolTable.getLastExecuted() : FunctionSymbolTable.getLastParsed());
        String symbolNameAltered = (lastFunctionParsedName + "__" + symbolName);

        if (!table.containsKey(symbolNameAltered)) {
            ErrorReport.makeError(ErrorReportSemantic.class, "'ProgramSymbolTable' (getSymbolValue) -- Symbol not found in table: "+symbolNameAltered, symbolToken);
            return null;
        }

        return table.get(symbolNameAltered).getValueRaw();

    }

    public static boolean tokenHasMatchingType(Token sourceSymbolToken, String targetType) {
        
        String sourceType = getSymbolType(sourceSymbolToken);

        if (sourceType == null)
            return false;

        return sourceType.equals(targetType);
        
    }

    public static boolean validateSymbolAssignmentOperation(SymbolInfo sourceSymbolInfo, Token targetToken) {

        /*

            Checks if an assignment operation is allowed
            between a symbol (which should already be
            in the table!) and a provided token.
        */


        //Symbol is not in the table, report an error
        if (!table.containsKey(sourceSymbolInfo.getVarRefToken().getToken())) {

            ErrorReport.makeError(ErrorReportSemantic.class, "'ProgramSymbolTable' (validateSymbolAssignmentOperation) -- Symbol not found in table: "+sourceSymbolInfo.getVarRefToken().getToken(), sourceSymbolInfo.getVarRefToken());
            return false;

        }

        //Symbol is in the table, check if the assignment is valid
        String sourceType = sourceSymbolInfo.getIdToken().getToken();
        String targetType = getSymbolType(targetToken);

        //Types are not the same, report an error
        if (!sourceType.equals(targetType)) {

            ErrorReport.makeError(ErrorReportSemantic.class, "ProgramSymbolTable -- Type mismatch in assignment operation: "+sourceType+" and "+targetType, targetToken);
            return false;

        }


        return true;

    }

    public static boolean symbolIsValid(SymbolInfo sourceSymbolInfo) {

        /*

            Checks if the symbol is allowed
            to be added to the table.

        */

        String symbolName = sourceSymbolInfo.getVarRefToken().getToken();
        String lastExecutedFunctionName = FunctionSymbolTable.getLastParsed();
        String symbolNameAltered = (lastExecutedFunctionName + "__" + symbolName);

        //Symbol already exists in table, report an error
        if (table.containsKey(symbolNameAltered)) {

            ErrorReport.makeError(ErrorReportSemantic.class, "ProgramSymbolTable -- Duplicate symbol definition: "+symbolNameAltered, sourceSymbolInfo.getVarRefToken());
            return false;
            
        }

        //Symbol uses a reserved keyword, report an error
        if (JottParser.isKeyword(symbolName)) {

            ErrorReport.makeError(ErrorReportSemantic.class, "ProgramSymbolTable -- Symbol uses a reserved keyword: "+symbolName, sourceSymbolInfo.getVarRefToken());
            return false;

        }

        //Symbol is valid
        return true;
        
    }


    public static SymbolInfo fetchSymbolInfoFromName(String symbolName, String prefixOverride) {

        String symbolNameAltered = (prefixOverride + "__" + symbolName);

        //System.out.println("[EX]  Attempting to fetch symbol info from name (altered): '"+symbolNameAltered+"'");

        if (!table.containsKey(symbolNameAltered))
            return null;

        SymbolInfo valueOut = table.get(symbolNameAltered);
        //System.out.println("[EX]  Symbol info fetched: "+valueOut.getValueRaw());

        return valueOut;

    }
    public static SymbolInfo fetchSymbolInfoFromName(String symbolName) {

        String lastFunctionParsedName = (FunctionSymbolTable.isExecuting ? FunctionSymbolTable.getLastExecuted() : FunctionSymbolTable.getLastParsed());
        String symbolNameAltered = (lastFunctionParsedName + "__" + symbolName);

        //System.out.println("[EX]  Attempting to fetch symbol info from name (altered): '"+symbolNameAltered+"'");

        if (!table.containsKey(symbolNameAltered))
            return null;

        SymbolInfo valueOut = table.get(symbolNameAltered);
        //System.out.println("[EX]  Symbol info fetched: "+valueOut.getValueRaw());

        return valueOut;

    }

    
    public static void setSymbolValue(Token symbolToken, ExpressionNodeBase valueExpression, String prefixOverride) {

        String symbolName = symbolToken.getToken();
        String symbolNameAltered = (prefixOverride + "__" + symbolName);

        if (valueExpression == null) {
            ErrorReport.makeError(ErrorReportRuntime.class, "'ProgramSymbolTable' (setSymbolValue) -- valueExpression is null for symbol: " + symbolNameAltered, symbolToken);
            return;
        }

        //System.out.println("[EX]  Attempting to set symbol value: '" + symbolNameAltered + "' to '" + valueExpression.convertToJott() + "'");

        if (!table.containsKey(symbolNameAltered)) {
            ErrorReport.makeError(ErrorReportRuntime.class, "'ProgramSymbolTable' (setSymbolValue) -- Symbol not found in table: " + symbolNameAltered, symbolToken);
            return;
        }

        Object valueExpressionEvaluated = valueExpression.evaluate();
        //System.out.println("[EX]  Value expression evaluated: " + valueExpressionEvaluated);

        if (valueExpressionEvaluated == null) {
            //System.out.println("[EX]  Value expression evaluated to null, skipping assignment...");
            return;
        }

        SymbolInfo symbolInfo = table.get(symbolNameAltered);

        ExpressionNodeBase newValue = null;
        String symbolType = symbolInfo.getIdToken().getToken();
        switch (symbolType) {

            //Numbers
            case "Integer":
            case "Double":
                Token numberToken = new Token(
                    valueExpressionEvaluated.toString(),
                    "",
                    -1,
                    TokenType.NUMBER
                );
                newValue = new NumberNode(numberToken);
                break;

            //Strings
            case "String":
                Token stringToken = new Token(
                    valueExpressionEvaluated.toString(),
                    "",
                    -1,
                    TokenType.STRING
                );
                newValue = new StringNode(stringToken);
                break;

            //Bools
            case "Boolean":
                Token booleanToken = new Token(
                    valueExpressionEvaluated.toString(),
                    "",
                    -1,
                    TokenType.KEYWORD
                );
                newValue = new BoolNode(booleanToken);
                break;

            //???
            default:
                ErrorReport.makeError(ErrorReportSemantic.class, "Unknown symbol type: " + symbolType, symbolToken);
                return;
        }

        symbolInfo.setValue(newValue);
        symbolInfo.setValueRaw(valueExpressionEvaluated);

        //System.out.println("[EX]  Symbol value updated to: " + valueExpressionEvaluated.toString());

    }
    public static void setSymbolValue(Token symbolToken, ExpressionNodeBase valueExpression) {

        String symbolName = symbolToken.getToken();
        String lastFunctionParsedName = (FunctionSymbolTable.isExecuting ? FunctionSymbolTable.getLastExecuted() : FunctionSymbolTable.getLastParsed());
        String symbolNameAltered = (lastFunctionParsedName + "__" + symbolName);

        //Check if valueExpression is null before using it
        if (valueExpression == null) {
            ErrorReport.makeError(ErrorReportSemantic.class, "'ProgramSymbolTable' (setSymbolValue) -- valueExpression is null for symbol: " + symbolNameAltered, symbolToken);
            return;
        }

        //System.out.println("[EX]  Attempting to set symbol value: '" + symbolNameAltered + "' to '" + valueExpression.convertToJott() + "'");

        if (!table.containsKey(symbolNameAltered)) {
            ErrorReport.makeError(ErrorReportSemantic.class, "'ProgramSymbolTable' (setSymbolValue) -- Symbol not found in table: " + symbolNameAltered, symbolToken);
            return;
        }

        Object valueExpressionEvaluated = valueExpression.evaluate();
        //System.out.println("[EX]  Value expression evaluated: " + valueExpressionEvaluated);

        if (valueExpressionEvaluated == null) {
            //System.out.println("[EX]  Value expression evaluated to null, skipping assignment...");
            return;
        }

        SymbolInfo symbolInfo = table.get(symbolNameAltered);

        ExpressionNodeBase newValue = null;
        String symbolType = symbolInfo.getIdToken().getToken();
        switch (symbolType) {

            //Numbers
            case "Integer":
            case "Double":
                Token numberToken = new Token(
                    valueExpressionEvaluated.toString(),
                    "",
                    -1,
                    TokenType.NUMBER
                );
                newValue = new NumberNode(numberToken);
                break;

            //Strings
            case "String":
                Token stringToken = new Token(
                    valueExpressionEvaluated.toString(),
                    "",
                    -1,
                    TokenType.STRING
                );
                newValue = new StringNode(stringToken);
                break;

            //Bools
            case "Boolean":
                Token booleanToken = new Token(
                    valueExpressionEvaluated.toString(),
                    "",
                    -1,
                    TokenType.KEYWORD
                );
                newValue = new BoolNode(booleanToken);
                break;

            //???
            default:
                ErrorReport.makeError(ErrorReportSemantic.class, "Unknown symbol type: " + symbolType, symbolToken);
                return;
        }

        symbolInfo.setValue(newValue);
        symbolInfo.setValueRaw(valueExpressionEvaluated);

        //System.out.println("[EX]  Symbol value updated to: " + valueExpressionEvaluated.toString());

    }
    public static void setSymbolValue(Token symbolToken, Object value) {

        String symbolName = symbolToken.getToken();
        String lastFunctionParsedName = FunctionSymbolTable.isExecuting ? FunctionSymbolTable.getLastExecuted() : FunctionSymbolTable.getLastParsed();
        String symbolNameAltered = lastFunctionParsedName + "__" + symbolName;

        if (value == null) {
            ErrorReport.makeError(ErrorReportSemantic.class, "'ProgramSymbolTable' (setSymbolValue) -- value is null for symbol: " + symbolNameAltered, symbolToken);
            return;
        }

        //System.out.println("[EX]  Attempting to set symbol value: '" + symbolNameAltered + "' to '" + value.toString() + "'");

        if (!table.containsKey(symbolNameAltered)) {
            ErrorReport.makeError(ErrorReportSemantic.class, "'ProgramSymbolTable' (setSymbolValue) -- Symbol not found in table: " + symbolNameAltered, symbolToken);
            return;
        }

        SymbolInfo symbolInfo = table.get(symbolNameAltered);

        ExpressionNodeBase newValue = null;
        String symbolType = symbolInfo.getIdToken().getToken();
        switch (symbolType) {

            //Numbers
            case "Integer":
            case "Double":
                Token numberToken = new Token(
                    value.toString(),
                    "",
                    -1,
                    TokenType.NUMBER
                );
                newValue = new NumberNode(numberToken);
                break;

            //Strings
            case "String":
                Token stringToken = new Token(
                    value.toString(),
                    "",
                    -1,
                    TokenType.STRING
                );
                newValue = new StringNode(stringToken);
                break;

            //Bools
            case "Boolean":
                Token booleanToken = new Token(
                    value.toString(),
                    "",
                    -1,
                    TokenType.KEYWORD
                );
                newValue = new BoolNode(booleanToken);
                break;

            //???
            default:
                ErrorReport.makeError(ErrorReportSemantic.class, "Unknown symbol type: " + symbolType, symbolToken);
                return;
        }

        symbolInfo.setValue(newValue);
        symbolInfo.setValueRaw(value);

        //System.out.println("[EX]  Symbol value updated to: " + value.toString());

    }


    public static boolean defineSymbol(SymbolInfo sourceSymbolInfo) {

        //Symbol is invalid, do not add it to the table
        if (!symbolIsValid(sourceSymbolInfo))
            return false;

        //Add the validated symbol to the table
        String symbolName = sourceSymbolInfo.getVarRefToken().getToken();
        table.put(symbolName, sourceSymbolInfo);

        return true;

    }

    public static boolean defineSymbol(VariableDeclarationNode varDecNode) {
        return defineSymbol(varDecNode.getType(), varDecNode.getName(), null);
    }
    public static boolean defineSymbol(TypeNode type, VarRefNode name) {
        return defineSymbol(type, name, null);
    }
    public static boolean defineSymbol(TypeNode type, VarRefNode name, ExpressionNodeBase value) {

        //System.out.println("[EX VAR REF NODE TYPE: "+type.getType().getToken());
        //System.out.println("[EX VAR REF NODE NAME: "+name.getIdToken().getToken());

        String symbolName = name.getIdToken().getToken();
        String lastFunctionParsedName = (FunctionSymbolTable.isExecuting ? FunctionSymbolTable.getLastExecuted() : FunctionSymbolTable.getLastParsed());
        //System.out.println("[EX]  Last parsed function name: "+lastFunctionParsedName);

        Token symbolNameTokenAltered = new Token(
            lastFunctionParsedName + "__" + symbolName,
            name.getIdToken().getFilename(),
            name.getIdToken().getLineNum(),
            name.getIdToken().getTokenType()
        );

        SymbolInfo newSymbol = new SymbolInfo(type.getType(), symbolNameTokenAltered, value);
        return defineSymbol(newSymbol);

    }


    public static SymbolInfo getSymbol(Token token, String scopeFunctionName) {

        //Default to the current function scope if scopeFunctionName is null
        if (scopeFunctionName == null)
            scopeFunctionName = FunctionSymbolTable.getCurrentFunctionScope();

        String symbolName = token.getToken();
        String symbolNameAltered = scopeFunctionName + "__" + symbolName;

        //System.out.println("[EX]  Attempting to get symbol from name (altered): '" + symbolNameAltered + "'");

        if (!table.containsKey(symbolNameAltered)) {
            ErrorReport.makeError(ErrorReportSemantic.class, "'ProgramSymbolTable' (getSymbol <Token, String>) -- Symbol not found in table: " + symbolNameAltered, token);
            return null;
        }

        return table.get(symbolNameAltered);

    }
    public static SymbolInfo getSymbol(String symbolName) {

        String lastFunctionParsedName = (FunctionSymbolTable.isExecuting ? FunctionSymbolTable.getLastExecuted() : FunctionSymbolTable.getLastParsed());
        String symbolNameAltered = (lastFunctionParsedName + "__" + symbolName);

        //System.out.println("[EX]  Attempting to get symbol from name (altered): '"+symbolNameAltered+"'");

        if (!table.containsKey(symbolNameAltered)) {
            ErrorReport.makeError(ErrorReportSemantic.class, "'ProgramSymbolTable' (getSymbol <String>) -- Symbol not found in table: "+symbolNameAltered, TokenStack.get_last_token_popped());
            return null;
        }

        return table.get(symbolNameAltered);

    }
    public static SymbolInfo getSymbol(Token token) {

        return getSymbol(token, null);

    }

    public static String getSymbolNameAltered(String symbolName) {

        String lastFunctionParsedName = (FunctionSymbolTable.isExecuting ? FunctionSymbolTable.getLastExecuted() : FunctionSymbolTable.getLastParsed());
        return (lastFunctionParsedName + "__" + symbolName);

    }


    public static void printWholeTable() {

        String output = "Symbol Table:\n";
        for (String key : table.keySet()) {

            SymbolInfo symbol = table.get(key);
            String symbolTypeStr = symbol.getIdToken().getToken();
            
            String symbolValueStr = (symbol.getValueRaw() == null)  ? "null" : symbol.getValueRaw().toString();

            output += "\t"+key+" -> "+symbolTypeStr+": "+symbolValueStr+"\n";

        }

        System.out.println(output);

    }
}
