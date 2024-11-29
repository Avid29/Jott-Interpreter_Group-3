package Interpreter.ProgramTree;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSemantic;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FuncCallParamsNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionRefNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.FunctionNodes.FunctionDefNode;
import Interpreter.ProgramTree.Nodes.FunctionNodes.ParametersDefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.Tuple4;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import provided.Token;
import provided.TokenType;

public class FunctionSymbolTable {

    private static final String[] builtInFunctionNames = new String[] { "print", "concat", "length" };    
    private static HashMap<String, Tuple4<FunctionRefNode, FunctionDefNode, ParametersDefNode, TypeNode>> table = new HashMap<>();
    static {
        addBuiltInFunctions();
    }

    //Stack execution tracking
    public static boolean isExecuting = false;
    private static String lastFunctionParsedName = "*PLACEHOLDER FUNCTION NAME*";
    private static final Stack<String> functionExecutedStack = new Stack<>();
    public static boolean isEvaluatingParams = false;
    public static String paramEvaluationScope = null;
    public static final Stack<Object> functionReturnStack = new Stack<>();


    private static void addBuiltInFunctions() {

        /*

            All built-in functions:

            - print[ANY x] -> Void
            - concat[String x, String y] -> String
            - length[String x] -> Integer

        */

        /*
            Add print function:
        */
        FunctionRefNode printRef = new FunctionRefNode(
            new Token("print", "print", 0, TokenType.ID)
        );

        ArrayList<VariableDeclarationNode> printParams = new ArrayList<>();
        printParams.add(
            new VariableDeclarationNode(
                new TypeNode(new Token("ANY", "print", 0, TokenType.KEYWORD)),
                new VarRefNode(new Token("printContent", "print", 0, TokenType.ID))
            )
        );

        ParametersDefNode printParamsDef = new ParametersDefNode(printParams);
        BodyNode printBody = new BodyNode(new ArrayList<>());

        TypeNode printReturn = new TypeNode(new Token("Void", "print", 0, TokenType.KEYWORD));

        FunctionDefNode printDef = new FunctionDefNode(
            printRef,
            printParamsDef,
            printReturn,
            printBody
        );

        Tuple4<FunctionRefNode, FunctionDefNode, ParametersDefNode, TypeNode> printData = new Tuple4<>(printRef, printDef, printParamsDef, printReturn);
        table.put("print", printData);


        /*
            Add concat function:
        */
        FunctionRefNode concatRef = new FunctionRefNode(
            new Token("concat", "concat", 0, TokenType.ID)
        );

        ArrayList<VariableDeclarationNode> concatParams = new ArrayList<>();
        concatParams.add(
            new VariableDeclarationNode(
                new TypeNode(new Token("String", "concat", 0, TokenType.KEYWORD)),
                new VarRefNode(new Token("str1", "concat", 0, TokenType.ID))
            )
        );
        concatParams.add(
            new VariableDeclarationNode(
                new TypeNode(new Token("String", "concat", 0, TokenType.KEYWORD)),
                new VarRefNode(new Token("str2", "concat", 0, TokenType.ID))
            )
        );

        ParametersDefNode concatParamsDef = new ParametersDefNode(concatParams);
        BodyNode concatBody = new BodyNode(new ArrayList<>());

        TypeNode concatReturn = new TypeNode(new Token("String", "concat", 0, TokenType.KEYWORD));

        FunctionDefNode concatDef = new FunctionDefNode(
            concatRef,
            concatParamsDef,
            concatReturn,
            concatBody
        );

        Tuple4<FunctionRefNode, FunctionDefNode, ParametersDefNode, TypeNode> concatData = new Tuple4<>(concatRef, concatDef, concatParamsDef, concatReturn);
        table.put("concat", concatData);


        /*
            Add length function:
        */
        FunctionRefNode lengthRef = new FunctionRefNode(
            new Token("length", "length", 0, TokenType.ID)
        );

        ArrayList<VariableDeclarationNode> lengthParams = new ArrayList<>();
        lengthParams.add(
            new VariableDeclarationNode(
                new TypeNode(new Token("String", "length", 0, TokenType.KEYWORD)),
                new VarRefNode(new Token("str", "length", 0, TokenType.ID))
            )
        );

        ParametersDefNode lengthParamsDef = new ParametersDefNode(lengthParams);
        BodyNode lengthBody = new BodyNode(new ArrayList<>());

        TypeNode lengthReturn = new TypeNode(new Token("Integer", "length", 0, TokenType.KEYWORD));

        FunctionDefNode lengthDef = new FunctionDefNode(
            lengthRef,
            lengthParamsDef,
            lengthReturn,
            lengthBody
        );

        Tuple4<FunctionRefNode, FunctionDefNode, ParametersDefNode, TypeNode> lengthData = new Tuple4<>(lengthRef, lengthDef, lengthParamsDef, lengthReturn);
        table.put("length", lengthData);

    }

    public static boolean isBuiltInFunction(String funcName) {

        for (String builtInFuncName : builtInFunctionNames) {

            if (funcName.equals(builtInFuncName))
                return true;

        }

        return false;

    }

    public static ParametersDefNode getFunctionParameters(String funcName) {

        Tuple4<FunctionRefNode, FunctionDefNode, ParametersDefNode, TypeNode> funcData = table.get(funcName);

        if (funcData == null)
            return null;

        return funcData.Item3;

    }

    public static String getSymbolType(Token funcTarget, Token symbolToken) {

        String funcName = funcTarget.getToken();

        Tuple4<FunctionRefNode, FunctionDefNode, ParametersDefNode, TypeNode> funcData = table.get(funcName);

        if (funcData == null) {

            ErrorReport.makeError(ErrorReportSemantic.class, "Function not found in table: "+funcName, funcTarget);
            return null;

        }

        ParametersDefNode params = funcData.Item3;

        for (int i = 0; i < params.getNumParams(); i++) {

            String paramName = params.getParam(i).getName().getIdToken().getToken();
            String paramTypeName = params.getParam(i).getType().getType().getToken();

            //System.out.println("[EX]  Param String: "+paramName +", type: "+paramTypeName);

            if (paramName.equals(symbolToken.getToken()))
                return paramTypeName;

        }

        return null;

    }

    public static boolean programContainsMain() {

        return table.containsKey("main");

    }

    public static FunctionRefNode getMainFunctionRef() {

        return table.get("main").Item1;

    }


    public static TypeNode getFunctionType(String funcName) {

        Tuple4<FunctionRefNode, FunctionDefNode, ParametersDefNode, TypeNode> funcData = table.get(funcName);

        if (funcData == null)
            return null;

        return funcData.Item4;

    }

    public static boolean programContainsFunction(String funcName) {

        return table.containsKey(funcName);

    }

    public static Tuple4<FunctionRefNode, FunctionDefNode, ParametersDefNode, TypeNode> getFunctionData(String funcName) {

        return table.get(funcName);

    }


    public static void clearTable() { 

        table = new HashMap<>();

        //(Re-)add built-in functions
        addBuiltInFunctions();

    }


    public static boolean functionParamsMatch(String funcName, FuncCallParamsNode params) {

        //Get the function data
        Tuple4<FunctionRefNode, FunctionDefNode, ParametersDefNode, TypeNode> funcData = table.get(funcName);

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

            ParametersDefNode funcParams = funcData.Item3;

            //Check if the number of parameters match
            int paramsStored = funcParams.getNumParams();
            int paramsGiven = params.getNumParams();
            if (paramsStored != paramsGiven) {

                ErrorReport.makeError(
                    ErrorReportSemantic.class,
                    "Function parameter count mismatch: "
                        + "\n\tfuncName"
                        + "\n\tExpected "+paramsStored+", got "+paramsGiven
                        + "\n\tSource: "+funcParams.convertToJott()
                        + "\n\tProvided: "+params.convertToJott()
                        ,
                    lastToken
                );
                return false;

            }

            //Check if the parameter types match
            for (int i = 0; i < params.getNumParams(); i++) {

                TypeNode paramType = params.getParam(i).getType();
                TypeNode funcParamType = funcParams.getParam(i).getType();

                String paramTypeName = paramType.getType().getToken();
                String funcParamTypeName = funcParamType.getType().getToken();

                //Skip ANY type parameters (i.e. for print function)
                if (funcParamTypeName.equals("ANY"))
                    continue;

                if (!paramTypeName.equals(funcParamTypeName)) {

                    ErrorReport.makeError(ErrorReportSemantic.class, "Parameter type mismatch in function '"+funcName+"': expected "+funcParamTypeName+", got "+paramTypeName, lastToken);
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

            //System.out.println("[EX]  Function already exists in table: "+funcName);

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
    

    public static void setFunctionDefinitionForName(String funcName, FunctionDefNode funcDef) {

        Tuple4<FunctionRefNode, FunctionDefNode, ParametersDefNode, TypeNode> funcData = table.get(funcName);

        if (funcData == null) {

            ErrorReport.makeError(ErrorReportSemantic.class, "Function not found in table: "+funcName, TokenStack.get_last_token_popped());
            return;

        }

        funcData.Item2 = funcDef;

    }

    public static boolean defineSymbol(FunctionRefNode funcRef, FunctionDefNode funcDef, ParametersDefNode params, TypeNode returnType) {

        //System.out.println("[EX]  Attempting to add function to table: "+funcRef.getId().getToken());

        //Check if the new symbol is valid
        if (!validateNewFunctionSymbol(funcRef, params, returnType))
            return false;

        //Create Symbol
        String funcName = funcRef.getId().getToken();
        Tuple4<FunctionRefNode, FunctionDefNode, ParametersDefNode, TypeNode> funcData = new Tuple4<>(funcRef, funcDef, params, returnType);
        table.put(funcName, funcData);

        //Successfully added symbol to table
        return true;

    }

    //Execute Function
    public static void executeFunction(String funcName) {

        //System.out.println("[EX]ecuting Function: "+funcName);

        //Get the function data
        Tuple4<FunctionRefNode, FunctionDefNode, ParametersDefNode, TypeNode> funcData = table.get(funcName);

        //Function not found in table -> Invalidate
        if (funcData == null) {

            ErrorReport.makeError(ErrorReportSemantic.class, "Function not found in table: "+funcName, TokenStack.get_last_token_popped());
            return;

        }

        //Execute the function's body
        funcData.Item2.execute();

    }


    /*
        Handle Recording Parsed Functions
    */
    public static void pushFunctionParsed(String funcName) {

        //System.out.println("[EX] Pushing Function Call: "+funcName);
        lastFunctionParsedName = funcName;

    }
    public static String getLastParsed() {

        return lastFunctionParsedName;

    }


    /*
        Handle Recording Executed Functions
    */
    public static void pushFunctionExecuted(String funcName) {

        //System.out.println("[EX] Pushing Function Executed: "+funcName);
        functionExecutedStack.push(funcName);

    }
    public static void popFunctionExecuted() {

        //System.out.println("[EX] Popping Function Executed: "+functionExecutedStack.peek());
        functionExecutedStack.pop();

    }
    public static String getLastExecuted() {

        if (functionExecutedStack.isEmpty()) {
            //System.out.println("[EX]  Function Executed Stack is empty!");
            return null;
        }

        return functionExecutedStack.peek();

    }


    /*
        Handle Function Scope
    */
    public static String getCurrentFunctionScope() {

        if (isEvaluatingParams && paramEvaluationScope != null)
            return paramEvaluationScope;

        else if (isExecuting)
            return getLastExecuted();

        else
            return getLastParsed();

    }


    /*
        Handle Function Return Values
    */
    public static void pushFunctionReturn(Object returnValue) {

        //System.out.println("[EX] Pushing Function Return: "+returnValue);
        functionReturnStack.push(returnValue);

    }
    public static Object popFunctionReturn() {

        //System.out.println("[EX] Popping Function Return: "+functionReturnStack.peek());
        return functionReturnStack.pop();

    }



}

