package Interpreter.ProgramTree.Nodes.StatementNodes;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import provided.Token;
import provided.TokenType;

public class VariableDeclarationNode extends BodyStatementNodeBase {
    private TypeNode type;
    private VarRefNode name;

    public VariableDeclarationNode(TypeNode type, VarRefNode name, boolean param) {
        this.type = type;
        this.name = name;
    }

    public static VariableDeclarationNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        ArrayList<Token> popped = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(
                new TokenType[] { TokenType.KEYWORD, TokenType.ID, TokenType.SEMICOLON }, popped);

        if (errorCode != -1) {
            // No need to write an error message because the line still may be valid. We
            // just need to backtrack and pass it along to the function body.
            tokens.popStack(true);
            return null;
        }

        //Get the type, ensure it is not Void
        TypeNode type = new TypeNode(popped.get(0));
        if (type.getType().getToken().equals("Void")) {
            System.err.println("ERROR -- Cannot declare a variable of type Void");
            tokens.popStack(true);
            return null;
        }

        VarRefNode name = new VarRefNode(popped.get(1));

        tokens.popStack(false);
        return new VariableDeclarationNode(type, name, false);
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
}
