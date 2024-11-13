package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import java.util.ArrayList;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BlockDeclareNodeBase;
import provided.Token;
import provided.TokenType;

public class IfBlockNode extends BlockDeclareNodeBase {

	/*
 	* IF_STMT GRAMMAR:
 	*
 	* <if_stmt> -> If[<expr>]{<body>}<elseif_lst>*<else>
	*/

	private ExpressionNodeBase expression;
	private ArrayList<ElseIfBlockNode> elseIfBlocks;
	private ElseBlockNode elseBlock;

	public IfBlockNode(ExpressionNodeBase expression, BodyNode body, ArrayList<ElseIfBlockNode> elseIfBlocks, ElseBlockNode elseBlock) {

		this.expression = expression;
		this.body = body;
    	this.elseIfBlocks = elseIfBlocks;
    	this.elseBlock = elseBlock;

	}

	public static IfBlockNode parseNode(TokenStack tokens) {
    	tokens.pushStack();

    	ArrayList<Token> popped = new ArrayList<>();
    	int errorCode = tokens.tokenSequenceMatch(new TokenType[]{TokenType.KEYWORD, TokenType.L_BRACKET}, popped);

    	if (errorCode != -1) {
        	
            // Missing conditional expression
            ErrorReport.makeError(ErrorReportSyntax.class, "Missing conditional expression", TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;
    	}

    	if (!popped.get(0).getToken().equals("If")) {
        	// https://youtu.be/cYMfJUMsLj4
        	tokens.popStack(true);
        	return null;
    	}
   	 
    	//Parse the expression
        ExpressionNodeBase parsedExpression = ExpressionNodeBase.parseNode(tokens);

        //Parsed expression is null -> null
        if (parsedExpression == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "Parsed expression is null", TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;
        }
        
    	//No closing Right Bracket ( ] ) -> null
    	if (tokens.popToken().getTokenType() != TokenType.R_BRACKET) {
            
            ErrorReport.makeError(ErrorReportSyntax.class, "Missing Closing Bracket ']'", TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;
    	}

        //Parse the body
        BodyNode parsedBody = BodyNode.parseNode(tokens, false);

        //Parsed body is null -> null
        if (parsedBody == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "Parsed body is null", TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;
        }

    	tokens.popStack(false);
    	return new IfBlockNode(parsedExpression, parsedBody, parseElseIfBlocks(tokens), parseElseBlock(tokens));

	}

	private static ArrayList<ElseIfBlockNode> parseElseIfBlocks(TokenStack tokens) {

    	ArrayList<ElseIfBlockNode> elseIfBlocks = new ArrayList<>();
   	 
    	while (tokens.peekToken().getToken().equals("ElseIf")) {

        	elseIfBlocks.add(ElseIfBlockNode.parseNode(tokens));
    	}

    	return elseIfBlocks;


	}


	private static ElseBlockNode parseElseBlock(TokenStack tokens) {
    	if (tokens.peekToken().getToken().equals("Else"))
        	return ElseBlockNode.parseNode(tokens);
    	return null;

	}

    @Override
    public boolean validateTree() {
		boolean expectReturn = body.containsReturn();

		for (ElseIfBlockNode elIfChild : elseIfBlocks) {
			if (elIfChild.containsReturn() != expectReturn)
				return false;
		}

		if (elseBlock.containsReturn() != expectReturn)
			return false;

		return true;
    }

	@Override
	public String convertToJott() {
		String output = "If[" + expression.convertToJott() + "]" + body.convertToJott();
		for (ElseIfBlockNode elseIfBlockNode : elseIfBlocks) {
			output += elseIfBlockNode.convertToJott();
		}
		if (elseBlock != null){
			output += elseBlock.convertToJott();
		}
		return output;
	}
}


