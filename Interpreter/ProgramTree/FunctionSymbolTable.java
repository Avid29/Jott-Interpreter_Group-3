package Interpreter.ProgramTree;

import java.util.HashMap;

import Interpreter.Tuple3;
import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSemantic;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.SymbolInfo;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;
import provided.JottTokenizer;
import provided.Token;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FuncCallParamsNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionRefNode;
import Interpreter.ProgramTree.Nodes.FunctionNodes.ParametersDefNode;

public class FunctionSymbolTable {

    private static String[] builtInFunctionNames = new String[] { "print", "concat", "length" };    
    private static HashMap<String, Tuple3<FunctionRefNode, ParametersDefNode, TypeNode>> table = new HashMap<>();
    static {
        addBuiltInFunctions();
    }


    private static void addBuiltInFunctions() {
            
        //Add built-in functions to the table
        for (String funcName : builtInFunctionNames) {

            table.put(funcName, null);

        }

    }

    private static boolean isBuiltInFunction(String funcName) {

        for (String builtInFuncName : builtInFunctionNames) {

            if (funcName.equals(builtInFuncName))
                return true;

        }

        return false;

    }

    private static boolean parameterNameExistsForFunctionName(String funcName, String paramName) {

        Tuple3<FunctionRefNode, ParametersDefNode, TypeNode> funcData = table.get(funcName);

        if (funcData == null)
            return false;

        ParametersDefNode params = funcData.Item2;

        for (int i = 0; i < params.getNumParams(); i++) {

            //System.out.println("[EX] Checking parameter: "+params.getParam(i).getName().getIdToken());

            if (params.getParam(i).getName().getIdToken().equals(paramName))
                return true;

        }

        return false;

    }

    public static String getSymbolType(Token funcTarget, Token symbolToken) {

        String funcName = funcTarget.getToken();

        Tuple3<FunctionRefNode, ParametersDefNode, TypeNode> funcData = table.get(funcName);

        if (funcData == null) {

            ErrorReport.makeError(ErrorReportSemantic.class, "Function not found in table: "+funcName, funcTarget);
            return null;

        }

        ParametersDefNode params = funcData.Item2;

        for (int i = 0; i < params.getNumParams(); i++) {

            String paramName = params.getParam(i).getName().getIdToken().getToken();
            String paramTypeName = params.getParam(i).getType().getType().getToken();
            //System.out.println("[EX] Param String: "+paramName +", type: "+paramTypeName);

            if (paramName.equals(symbolToken.getToken()))
                return paramTypeName;

        }

        return null;

    }

    public static boolean programContainsMain() {

        return table.containsKey("main");

    }


    public static TypeNode getFunctionType(String funcName) {

        Tuple3<FunctionRefNode, ParametersDefNode, TypeNode> funcData = table.get(funcName);

        if (funcData == null)
            return null;

        return funcData.Item3;

    }

    public static boolean programContainsFunction(String funcName) {

        return table.containsKey(funcName);

    }


    public static void clearTable() { 

        //  System.out.println("[EX] Cleared Function Symbol Table");

        table = new HashMap<>();

        //(Re-)add built-in functions
        addBuiltInFunctions();

    }


    public static boolean functionParamsMatch(String funcName, FuncCallParamsNode params) {

        //Get the function data
        Tuple3<FunctionRefNode, ParametersDefNode, TypeNode> funcData = table.get(funcName);

        Token lastToken = TokenStack.get_last_token_popped();

        //Function not found in table and not a built-in function -> Invalidate
        if (funcData == null) {

            if (!isBuiltInFunction(funcName)) {

                ErrorReport.makeError(ErrorReportSemantic.class, "Function not found in table: "+funcName, lastToken);
                return false;

            }
        }

        //Get the function's parameters
        else {

            ParametersDefNode funcParams = funcData.Item2;

            //Check if the number of parameters match
            if (params.getNumParams() != funcParams.getNumParams()) {

                ErrorReport.makeError(ErrorReportSemantic.class, "Function parameter count mismatch: "+funcName, lastToken);
                return false;

            }

            //Check if the parameter types match
            for (int i = 0; i < params.getNumParams(); i++) {

                TypeNode paramType = params.getParam(i).getType();
                TypeNode funcParamType = funcParams.getParam(i).getType();

                String paramTypeName = paramType.getType().getToken();
                String funcParamTypeName = funcParamType.getType().getToken();

                if (!paramTypeName.equals(funcParamTypeName)) {

                    ErrorReport.makeError(ErrorReportSemantic.class, "Function parameter type mismatch in "+funcName+": expected "+funcParamTypeName+", got "+paramTypeName, lastToken);
                    return false;

                }

            }
            
        }

        //Parameters match
        return true;

    }


    public static boolean validateNewFunctionSymbol(FunctionRefNode funcRef, ParametersDefNode params, TypeNode returnType) {

        String returnTypeName = returnType.getType().getToken();
        String funcName = funcRef.getId().getToken();

        //Duplicate function name -> Invalidate
        if (table.containsKey(funcName)) {

            //  System.out.println("[EX] Function already exists in table: "+funcName);

            ErrorReport.makeError(ErrorReportSemantic.class, "Duplicate function definition: "+funcName, funcRef.getId());
            return false;

        }

        //Handle main function special cases
        if (funcName.equals("main")) {
            
            //Main with parameters -> Invalidate
            if (params.hasParams()) {

                ErrorReport.makeError(ErrorReportSemantic.class, "Main function cannot have parameters", funcRef.getId());
                return false;

            }

            //Main with non-Void return type -> Invalidate 
            if (!returnTypeName.equals("Void")) {

                ErrorReport.makeError(ErrorReportSemantic.class, "Main function cannot have a return type", funcRef.getId());
                return false;

            }

        }

        //Validated Successfully
        return true;

    }
    

    public static boolean defineSymbol(FunctionRefNode funcRef, ParametersDefNode params, TypeNode returnType) {

        //  System.out.println("[EX] Attempting to add function to table: "+funcRef.getId().getToken());

        //Check if the new symbol is valid
        if (!validateNewFunctionSymbol(funcRef, params, returnType))
            return false;

        //Create Symbol
        String funcName = funcRef.getId().getToken();
        Tuple3<FunctionRefNode, ParametersDefNode, TypeNode> funcData = new Tuple3<>(funcRef, params, returnType);
        table.put(funcName, funcData);

        //Successfully added symbol to table
        return true;

    }


}

