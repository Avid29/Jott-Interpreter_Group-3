package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.ProgramSymbolTable;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import SymbolInfo.SymbolInfo;
import provided.Token;
import provided.TokenType;

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
    public String getType() {
        return type;
    }
}
