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
            ErrorReport.makeError(ErrorReportSyntax.class, "IfBlockNode -- Missing conditional expression", TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;
    	}

		Token poppedToken = popped.get(0);
    	if (!poppedToken.getToken().equals("If")) {

			ErrorReport.makeError(ErrorReportSyntax.class, "IfBlockNode -- Expected 'If', got "+poppedToken.getTokenType(), TokenStack.get_last_token_popped());

        	// https://youtu.be/cYMfJUMsLj4
        	tokens.popStack(true);
        	return null;
    	}
   	 
    	//Parse the expression
        ExpressionNodeBase parsedExpression = ExpressionNodeBase.parseNode(tokens);

        //Parsed expression is null -> null
        if (parsedExpression == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "IfBlockNode -- Parsed expression is null", TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;
        }
        
    	//No closing Right Bracket ( ] ) -> null
		Token nextToken = tokens.popToken();
    	if (nextToken.getTokenType() != TokenType.R_BRACKET) {
            
            ErrorReport.makeError(ErrorReportSyntax.class, "IfBlockNode -- Missing Closing Bracket ']', got "+nextToken.getTokenType(), TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;

    	}

        //Parse the body
        BodyNode parsedBody = BodyNode.parseNode(tokens, false);

        //Parsed body is null -> null
        if (parsedBody == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "IfBlockNode -- Failed to parse body", TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;

        }

    	tokens.popStack(false);
    	return new IfBlockNode(parsedExpression, parsedBody, parseElseIfBlocks(tokens), parseElseBlock(tokens));

	}

	private static ArrayList<ElseIfBlockNode> parseElseIfBlocks(TokenStack tokens) {

    	ArrayList<ElseIfBlockNode> elseIfBlocks = new ArrayList<>();
   	 
    	while (tokens.peekToken().getToken().equals("Elseif")) {

			elseIfBlocks.add(ElseIfBlockNode.parseNode(tokens, true)); /* The 'true' is a flag to call proper Elseif parsing */

    	}

    	return elseIfBlocks;
	}


	private static ElseBlockNode parseElseBlock(TokenStack tokens) {

		//Parsed Else Successfully
    	if (tokens.peekToken().getToken().equals("Else"))
        	return ElseBlockNode.parseNode(tokens, true); /* The 'true' is a flag to call proper Else parsing */
		
		//Failed to Parse Else
    	return null;
	}

    @Override
    public boolean validateTree() {
		// Validate return parity
		if (!validateReturnPairity()) {
			return false;
		}

		// Validate body
		if (!body.validateTree()) {
			return false;
		}

		// Validate else if child blocks
		for (ElseIfBlockNode elseIfNode : elseIfBlocks) {
			if (!elseIfNode.validateTree()){
				return false;
			}
		}

		// Validate else child block
		if (!elseBlock.validateTree())
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

	private boolean validateReturnPairity(){
		// Ensure that if one if child block contains a return, each contains a return. 
		boolean expectReturn = body.containsReturn();

		for (ElseIfBlockNode elIfChild : elseIfBlocks) {
			if (elIfChild.containsReturn() != expectReturn)
				return false;
		}

		if (elseBlock.containsReturn() != expectReturn)
			return false;

		return true;
	}

    public BodyNode getBodyNode() {
        return body;
    }
    
}


