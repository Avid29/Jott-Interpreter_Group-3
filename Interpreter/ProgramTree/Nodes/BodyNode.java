package Interpreter.ProgramTree.Nodes;

import java.util.ArrayList;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import ErrorReporting.ErrorReportSemantic;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.ReturnStatementNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import provided.Token;
import provided.TokenType;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.FunctionSymbolTable;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.IfBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.ElseIfBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.ElseBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.WhileBlockNode;

public class BodyNode extends NodeBase<BodyNode> {
    private ArrayList<BodyStatementNodeBase> statements;

    public BodyNode(ArrayList<BodyStatementNodeBase> statements) {
        this.statements = statements;
    }

    private static boolean allPathsReturn(ArrayList<BodyStatementNodeBase> statements) {

        boolean hasReturn = false;
        for (BodyStatementNodeBase statement : statements) {

            //Got a raw return statement
            if (statement instanceof ReturnStatementNode) {
                hasReturn = true;
                break;
            }
            
            //Recursively check for return statements in branches (if-else and while)
            if (statement instanceof IfBlockNode) {

                IfBlockNode ifBlock = (IfBlockNode) statement;
                if (allPathsReturn(ifBlock.getBodyNode().getStatements()) && allPathsReturn(ifBlock.getBodyNode().getStatements())) {
                    hasReturn = true;
                    break;
                }

            } else if (statement instanceof ElseIfBlockNode) {

                ElseIfBlockNode elseIfBlock = (ElseIfBlockNode) statement;
                if (allPathsReturn(elseIfBlock.getBodyNode().getStatements()) && allPathsReturn(elseIfBlock.getBodyNode().getStatements())) {
                    hasReturn = true;
                    break;
                }

            } else if (statement instanceof ElseBlockNode) {

                ElseBlockNode elseBlock = (ElseBlockNode) statement;
                if (allPathsReturn(elseBlock.getBodyNode().getStatements())) {
                    hasReturn = true;
                    break;
                }

            } else if (statement instanceof WhileBlockNode) {

                WhileBlockNode whileBlock = (WhileBlockNode) statement;
                if (allPathsReturn(whileBlock.getBodyNode().getStatements())) {
                    hasReturn = true;
                    break;
                }

            }
            

        }
        return hasReturn;
    }
    

    public static BodyNode parseNode(TokenStack tokens, boolean function) {
        tokens.pushStack();

    	//No opening Left Brace ( { ) -> null
    	if (tokens.popToken().getTokenType() != TokenType.L_BRACE) {

            ErrorReport.makeError(ErrorReportSyntax.class, "BodyNode -- Missing Opening Brace '{'", TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;
    	}

        ArrayList<BodyStatementNodeBase> statements = new ArrayList<>();

        if (function) {

            while (true) {

                VariableDeclarationNode decl = VariableDeclarationNode.parseNode(tokens);
                if (decl == null)
                    break;

                statements.add(decl);
            }   
        } 


        boolean encounteredReturn = false;
        while (true) {

            //Peek next token to check for closing brace
            Token peekedToken = tokens.peekToken();

            //No more tokens, missing closing brace
            if (peekedToken == null) {

                ErrorReport.makeError(ErrorReportSyntax.class, "BodyNode -- Missing Closing Brace '}'", TokenStack.get_last_token_popped());
                
                tokens.popStack(true);
                return null;

            }
            
            //Found closing brace, end loop
            if (peekedToken.getTokenType() == TokenType.R_BRACE)
                break;

            //If a return statement has been encountered, no more statements are allowed
            else if (encounteredReturn) {

                ErrorReport.makeError(ErrorReportSyntax.class, "BodyNode -- Code after Return", TokenStack.get_last_token_popped());

                tokens.popStack(true);
                return null;
            }

            BodyStatementNodeBase statement = BodyStatementNodeBase.parseNode(tokens);
            if (statement == null) {

                ErrorReport.makeError(ErrorReportSyntax.class, "BodyNode -- Failed to parse Body Statement", TokenStack.get_last_token_popped());

                tokens.popStack(true);
                return null;
            }

            statements.add(statement);

            //Flag that a return statement has been encountered
            if (statement instanceof ReturnStatementNode)
                encounteredReturn = true;
        }

        //Check if the function has a non-returning branch in a non-Void function
        if (function) {

            //Check if the function has a Void return type
            Token lastFunctionToken = TokenStack.get_last_function_token_popped();
            String lastFunctionName = lastFunctionToken.getToken();
            String lastFunctionReturnTypeName = FunctionSymbolTable.getFunctionType(lastFunctionName).getType().getToken();
            if (!lastFunctionReturnTypeName.equals("Void") && !allPathsReturn(statements)) {

                ErrorReport.makeError(ErrorReportSemantic.class, "BodyNode -- Got non-returning branch in non-Void function: "+lastFunctionName, TokenStack.get_last_token_popped());

                tokens.popStack(true);
                return null;

            }
        }

        // Pop the closing brace
        if(tokens.popToken().getTokenType() != TokenType.R_BRACE) {

            ErrorReport.makeError(ErrorReportSyntax.class, "BodyNode -- Missing Closing Brace '}'", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new BodyNode(statements);
    }

    public boolean containsReturn() {
        return statements.getLast() instanceof ReturnStatementNode;
    }

    public ArrayList<BodyStatementNodeBase> getStatements() {
        return statements;
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
