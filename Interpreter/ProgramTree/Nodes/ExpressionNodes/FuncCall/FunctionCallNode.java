package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSemantic;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.FunctionSymbolTable;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.SymbolInfo;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.ProgramSymbolTable;
import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

public class FunctionCallNode extends OperandNodeBase {

    private final FunctionRefNode funcName;
    private final FuncCallParamsNode callParams;

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

        String funcNameString = funcName.getId().getToken();
        TypeNode fetchResult = FunctionSymbolTable.getFunctionType(funcNameString);

        if (fetchResult == null) {
            ErrorReport.makeError(
                ErrorReportSemantic.class,
                "FunctionCallNode -- Failed to fetch function type of '" + funcNameString + "'",
                TokenStack.get_last_token_popped()
            );
            return null;
        }

        return fetchResult;
    }

    @Override
    public Object evaluate() {

        //Capture the caller's function name
        String callerFunctionName = FunctionSymbolTable.getLastExecuted();
        String funcNameStr = funcName.getId().getToken();
        //System.out.println("[EX] Evaluating 'FunctionCallNode' with Func Name '" + funcNameStr + "'");

        boolean isBuiltInFunction = FunctionSymbolTable.isBuiltInFunction(funcNameStr);
        boolean pushedFunction = false;

        //Push the function onto the stack if not built-in
        if (!isBuiltInFunction) {
            FunctionSymbolTable.pushFunctionExecuted(funcNameStr);
            pushedFunction = true;
        }

        //Evaluate the function parameters with the caller function's name
        callParams.setFuncName(funcNameStr);
        callParams.execute(callerFunctionName);

        Object returnValue = null;

        //Handle built-in functions
        if (isBuiltInFunction)
            returnValue = handleBuiltInFunction(funcNameStr, callerFunctionName);
        
        //Handle non-built-in function
        else
            FunctionSymbolTable.executeFunction(funcNameStr);

        //Pop the function from the stack if it was pushed
        if (pushedFunction)
            FunctionSymbolTable.popFunctionExecuted();

        return returnValue;

    }

    @Override
    public void execute() {

        //Capture the caller's function name
        String callerFunctionName = FunctionSymbolTable.getLastExecuted();
        String funcNameStr = funcName.getId().getToken();
        //System.out.println("[EX]ecuting 'FunctionCallNode' with Func Name '" + funcNameStr + "'");

        boolean isBuiltInFunction = FunctionSymbolTable.isBuiltInFunction(funcNameStr);
        boolean pushedFunction = false;

        //Push the function onto the stack if not built-in
        if (!isBuiltInFunction) {
            FunctionSymbolTable.pushFunctionExecuted(funcNameStr);
            pushedFunction = true;
        }

        //Evaluate the function parameters with the caller function's name
        callParams.setFuncName(funcNameStr);
        callParams.execute(callerFunctionName);

        //Execute the built-in function
        if (isBuiltInFunction)
            handleBuiltInFunction(funcNameStr, callerFunctionName);

        //Execute non-built-in function
        else
            FunctionSymbolTable.executeFunction(funcNameStr);

        //Pop the function from the stack if it was pushed
        if (pushedFunction)
            FunctionSymbolTable.popFunctionExecuted();

    }


    private Object handleBuiltInFunction(String funcNameStr, String callerFunctionName) {

        /*
            Handle print function
        */
        if (funcNameStr.equals("print")) {

            String printText = callParams.convertToJott();
            //System.out.println("[EX] Printing... (Raw Jott: " + printText + ")");

            //Resolve variable references in the caller's scope
            if (printText.startsWith("\"") && printText.endsWith("\"")) {
                printText = printText.substring(1, printText.length() - 1);
            }
            
            //Check if the print text is a function
            else if (printText.startsWith("::")) {

                Object returnValuePopped = FunctionSymbolTable.popFunctionReturn();
                if (returnValuePopped != null)
                    printText = returnValuePopped.toString();

            }

            //Check if the print text is a variable reference
            else {

                SymbolInfo symbolInfo = ProgramSymbolTable.fetchSymbolInfoFromName(printText, callerFunctionName);
                if (symbolInfo != null) {

                    Object symbolValue = symbolInfo.getValueRaw();

                    //Variable is numeric (Integer or Double)
                    if (symbolValue instanceof Number numberSymbol) {
                        
                        //System.out.println("[EX] Print Number Symbol: " + numberSymbol + " (" + numberSymbol.getClass().getSimpleName() + ")");

                        if (numberSymbol instanceof Integer)
                            printText = String.valueOf(numberSymbol.intValue());
                        else if (numberSymbol instanceof Double)
                            printText = String.valueOf(numberSymbol.doubleValue());

                    }
                    else {
                        printText = symbolValue.toString();
                    }

                }
                
            }

            //Remove starting and ending quotes if they exist (again)
            if (printText.startsWith("\"") && printText.endsWith("\""))
                printText = printText.substring(1, printText.length() - 1);

            System.out.println(printText);
            return null; //<-- print returns Void

        }
        
        /*
            Handle length function
        */
        else if (funcNameStr.equals("length")) {

            VarRefNode inputStringParam = (VarRefNode) callParams.getParam(0);
            String inputStringID = inputStringParam.getIdToken().getToken();
            SymbolInfo inputStringSymbol = ProgramSymbolTable.fetchSymbolInfoFromName(inputStringID, callerFunctionName);
            String inputStringValue = inputStringSymbol.getValueRaw().toString();

            //Strip quotes if present
            if (inputStringValue.startsWith("\"") && inputStringValue.endsWith("\""))
                inputStringValue = inputStringValue.substring(1, inputStringValue.length() - 1);

            int length = inputStringValue.length();
            //System.out.println("[EX] Length of string '" + inputStringValue + "': " + length);

            return length;

        }
        
        /*
            Handle concat function
        */
        else if (funcNameStr.equals("concat")) {

            VarRefNode inputStringParam1 = (VarRefNode) callParams.getParam(0);
            VarRefNode inputStringParam2 = (VarRefNode) callParams.getParam(1);

            String inputStringID1 = inputStringParam1.getIdToken().getToken();
            String inputStringID2 = inputStringParam2.getIdToken().getToken();

            SymbolInfo inputStringSymbol1 = ProgramSymbolTable.fetchSymbolInfoFromName(inputStringID1, callerFunctionName);
            SymbolInfo inputStringSymbol2 = ProgramSymbolTable.fetchSymbolInfoFromName(inputStringID2, callerFunctionName);

            String inputStringValue1 = inputStringSymbol1.getValueRaw().toString();
            String inputStringValue2 = inputStringSymbol2.getValueRaw().toString();

            //Strip quotes if present
            if (inputStringValue1.startsWith("\"") && inputStringValue1.endsWith("\""))
                inputStringValue1 = inputStringValue1.substring(1, inputStringValue1.length() - 1);
            if (inputStringValue2.startsWith("\"") && inputStringValue2.endsWith("\""))
                inputStringValue2 = inputStringValue2.substring(1, inputStringValue2.length() - 1);

            String outputValue = inputStringValue1 + inputStringValue2;
            //System.out.println("[EX] Concatenated strings: '" + inputStringValue1 + "' + '" + inputStringValue2 + "' = '" + outputValue + "'");

            return outputValue;

        }

        return null;
        
    }


}
