package Interpreter.ProgramTree;

import java.util.HashMap;

import Interpreter.Tuple2;
import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSemantic;
import provided.JottParser;
import provided.Token;
import Interpreter.ProgramTree.Nodes.SymbolInfo;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;



public class ProgramSymbolTable {

    private static HashMap<String, SymbolInfo> table = new HashMap<>();

    public static void clearTable() { 

        table = new HashMap<>();

    }
    

    public static String getSymbolType(Token symbolToken, boolean allowUnsafeAccess) {

        String symbolName = symbolToken.getToken();

        if (!table.containsKey(symbolName)) {

            if (!allowUnsafeAccess)
                ErrorReport.makeError(ErrorReportSemantic.class, "Symbol not found in table: "+symbolName,  symbolToken);
                
            return null;

        }

        return table.get(symbolName).getIdToken().getToken();

    }
    public static String getSymbolType(Token symbolToken) {

        return getSymbolType(symbolToken, false);

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

            ErrorReport.makeError(ErrorReportSemantic.class, "Symbol not found in table: "+sourceSymbolInfo.getVarRefToken().getToken(), sourceSymbolInfo.getVarRefToken());
            return false;

        }

        //Symbol is in the table, check if the assignment is valid
        String sourceType = sourceSymbolInfo.getIdToken().getToken();
        String targetType = getSymbolType(targetToken);

        //Types are not the same, report an error
        if (!sourceType.equals(targetType)) {

            ErrorReport.makeError(ErrorReportSemantic.class, "Type mismatch in assignment operation: "+sourceType+" and "+targetType, targetToken);
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

        //Symbol already exists in table, report an error
        if (table.containsKey(sourceSymbolInfo)) {

            ErrorReport.makeError(ErrorReportSemantic.class, "Duplicate symbol definition: "+symbolName, sourceSymbolInfo.getVarRefToken());
            return false;
            
        }
        //  System.out.println("\t\t[EX] Symbol doesn't already exist in table: "+symbolName);

        //Symbol uses a reserved keyword, report an error
        if (JottParser.isKeyword(symbolName)) {

            ErrorReport.makeError(ErrorReportSemantic.class, "Symbol uses a reserved keyword: "+symbolName, sourceSymbolInfo.getVarRefToken());
            return false;

        }

        //Symbol is valid
        return true;
        
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

    public static boolean defineSymbol(TypeNode type, VarRefNode name) {

        //System.out.println("[EX VAR REF NODE TYPE: "+type.getType().getToken());
        //System.out.println("[EX VAR REF NODE NAME: "+name.getIdToken().getToken());

        SymbolInfo newSymbol = new SymbolInfo(type.getType(), name.getIdToken());
        return defineSymbol(newSymbol);

    }

    public static SymbolInfo getSymbol(Token token) {
        if (!table.containsKey(token.getToken())) {
            ErrorReport.makeError(ErrorReportSemantic.class, "Symbol not found in table: "+token.getToken(), token);
            return null;

        }

        return table.get(token.getToken());

    }
}
