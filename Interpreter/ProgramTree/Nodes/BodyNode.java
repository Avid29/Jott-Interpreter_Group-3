package Interpreter.ProgramTree.Nodes;

import java.util.ArrayList;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.ReturnStatementNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import provided.Token;
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

            ErrorReport.makeError(ErrorReportSyntax.class, "Missing Opening Brace '{'", TokenStack.get_last_token_popped());

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


        boolean encounteredReturn = false;
        while (true) {

            //Peek next token to check for closing brace
            Token peekedToken = tokens.peekToken();

            //No more tokens, missing closing brace
            if (peekedToken == null) {

                ErrorReport.makeError(ErrorReportSyntax.class, "Missing Closing Brace '}'", TokenStack.get_last_token_popped());
                
                tokens.popStack(true);
                return null;

            }
            
            //Found closing brace, end loop
            if (peekedToken.getTokenType() == TokenType.R_BRACE)
                break;

            //If a return statement has been encountered, no more statements are allowed
            else if (encounteredReturn) {

                ErrorReport.makeError(ErrorReportSyntax.class, "Code after Return", TokenStack.get_last_token_popped());

                tokens.popStack(true);
                return null;
            }

            BodyStatementNodeBase statement = BodyStatementNodeBase.parseNode(tokens);
            if (statement == null) {
                tokens.popStack(true);
                return null;
            }

            statements.add(statement);

            //Flag that a return statement has been encountered
            if (statement instanceof ReturnStatementNode)
                encounteredReturn = true;
        }

        // Pop the closing brace
        if(tokens.popToken().getTokenType() != TokenType.R_BRACE) {

            ErrorReport.makeError(ErrorReportSyntax.class, "Missing Closing Brace '}'", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new BodyNode(statements);
    }

    public boolean containsReturn() {
        return statements.getLast() instanceof ReturnStatementNode;
    }

    @Override
    public String convertToJott() {
        String output = "{";
        for (BodyStatementNodeBase bodyStatementNodeBase : statements) {
            output += bodyStatementNodeBase.convertToJott();
        }
        return output + "}";
    }
}
