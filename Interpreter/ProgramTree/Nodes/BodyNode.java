package Interpreter.ProgramTree.Nodes;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.ReturnStatementNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import provided.TokenType;

public class BodyNode extends NodeBase<BodyNode> {
    private ArrayList<BodyStatementNodeBase> statements;

    public BodyNode(ArrayList<BodyStatementNodeBase> statements) {
        this.statements = statements;
    }

    public static BodyNode parseNode(TokenStack tokens, boolean function) {
        tokens.pushStack();

    	//No opening Left Brace ( { ) -> null
    	if (tokens.popToken().getTokenType() != TokenType.L_BRACE) {
        	tokens.popStack(true);
        	return null;
    	}

        ArrayList<BodyStatementNodeBase> statements = new ArrayList<>();

        if (function) {
            while (true) {
                VariableDeclarationNode decl = VariableDeclarationNode.parseNode(tokens);
                if (decl == null) {
                    break;
                }

                statements.add(decl);
            }   
        } 

        while (tokens.peekToken().getTokenType() != TokenType.R_BRACE) {
            BodyStatementNodeBase statement = BodyStatementNodeBase.parseNode(tokens);
            if (statement == null) {
                tokens.popStack(true);
                return null;
            }

            statements.add(statement);

            if (statement instanceof ReturnStatementNode){
                break;
            }
        }

        // Pop the closing brace
        if(tokens.popToken().getTokenType() != TokenType.R_BRACE) {
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new BodyNode(statements);
    }

    @Override
    public String convertToJott() {
        String output = "{";
        for (BodyStatementNodeBase bodyStatementNodeBase : statements) {
            output += bodyStatementNodeBase.convertToJott() + " ";
        }
        return output + "}";
    }
}
