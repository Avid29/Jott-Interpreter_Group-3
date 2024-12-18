package Interpreter.ProgramTree.Nodes.StatementNodes;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.ProgramSymbolTable;
import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

public class VariableDeclarationNode extends BodyStatementNodeBase {
    
    public TypeNode type;
    private final VarRefNode name;

    public VariableDeclarationNode(TypeNode type, VarRefNode name) {
        this.type = type;
        this.name = name;
    }

    public static VariableDeclarationNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        ArrayList<Token> popped = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(
            new TokenType[] { TokenType.KEYWORD, TokenType.ID, TokenType.SEMICOLON },
            popped
        );

        if (errorCode != -1) {

            // No need to write an error message because the line still may be valid. We
            // just need to backtrack and pass it along to the function body.
            tokens.popStack(true);
            return null;
        }

        //Get the type, ensure it is not Void
        TypeNode type = new TypeNode(popped.get(0));
        if (type.getType().getToken().equals("Void")) {

            ErrorReport.makeError(ErrorReportSyntax.class, "VariableDeclarationNode -- Cannot declare a variable of type 'Void'", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;

        }

        VarRefNode name = new VarRefNode(popped.get(1));


        //Attempt to record the symbol in the symbol table
        if (!ProgramSymbolTable.defineSymbol(type, name)) {

            tokens.popStack(true);
            return null;
            
        }


        tokens.popStack(false);
        return new VariableDeclarationNode(type, name);
    }

    @Override
    public String convertToJott() {
        return type.convertToJott() + " " + name.convertToJott() + ";";
    }

    public String convertToJott(boolean functionDef) {
        if (!functionDef)
            return this.convertToJott();

        return name.convertToJott() + ":" + type.convertToJott();
    }

    public TypeNode getType() {
        return type;
    }
    public VarRefNode getName() {
        return name;
    }


    @Override
    public void execute() {
        /*...*/
    }
}
