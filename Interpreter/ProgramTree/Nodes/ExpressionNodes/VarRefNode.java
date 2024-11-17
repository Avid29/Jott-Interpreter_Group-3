package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import java.lang.reflect.Parameter;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import ErrorReporting.ErrorReportSemantic;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.ProgramSymbolTable;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.Token;
import provided.TokenType;
import Interpreter.ProgramTree.Nodes.SymbolInfo;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.ProgramSymbolTable;
import Interpreter.ProgramTree.FunctionSymbolTable;


public class VarRefNode extends OperandNodeBase {

    private Token id;
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

            //System.out.println("[EX] Symbol not found in program symbol table: "+id.getToken());

            Token lastPoppedFunctionToken = TokenStack.get_last_function_token_popped();

            if (lastPoppedFunctionToken != null) {

                //System.out.println("[EX] Checking function symbol table for symbol: "+id.getToken() +" in function: "+lastPoppedFunctionToken.getToken());
                symbolVariableType = FunctionSymbolTable.getSymbolType(lastPoppedFunctionToken, id);

            }

            if (symbolVariableType == null) {
                
                ErrorReport.makeError(ErrorReportSemantic.class, "Symbol not found in program or function symbol table: "+id.getToken(), id);
                return null;

            }

        }

        return new TypeNode(
            new Token(symbolVariableType, id.getFilename(), id.getLineNum(), TokenType.KEYWORD)
        );
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

}
