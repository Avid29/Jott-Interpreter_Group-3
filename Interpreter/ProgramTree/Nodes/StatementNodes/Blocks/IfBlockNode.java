package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import java.util.ArrayList;

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

	private ArrayList<ElseIfBlockNode> elseIfBlocks;
	private ElseBlockNode elseBlock;

	public IfBlockNode(ArrayList<ElseIfBlockNode> elseIfBlocks, ElseBlockNode elseBlock) {

    	this.elseIfBlocks = elseIfBlocks;
    	this.elseBlock = elseBlock;

	}

	public static IfBlockNode parseNode(TokenStack tokens) {
    	tokens.pushStack();

    	ArrayList<Token> popped = new ArrayList<>();
    	int errorCode = tokens.tokenSequenceMatch(new TokenType[]{TokenType.KEYWORD, TokenType.L_BRACKET}, popped);

    	if (errorCode != -1) {
        	// Missing conditional expression
        	tokens.popStack(true);
        	return null;
    	}

    	if (!popped.get(0).getToken().equals("If")) {
        	// https://youtu.be/cYMfJUMsLj4
        	tokens.popStack(true);
        	return null;
    	}



    	//No opening Left Bracket ( [ ) -> null
    	if (tokens.peekToken().getTokenType() != TokenType.L_BRACKET) {
        	tokens.popStack(true);
        	return null;
    	}
   	 
    	//Parse the expression
        ExpressionNodeBase parsedExpression = ExpressionNodeBase.parseNode(tokens);

        //Parsed expression is null -> null
        if (parsedExpression == null) {
        	tokens.popStack(true);
        	return null;
        }
        
    	//No closing Right Bracket ( ] ) -> null
    	if (tokens.popToken().getTokenType() != TokenType.R_BRACKET) {
        	tokens.popStack(true);
        	return null;
    	}

    	//No opening Left Brace ( { ) -> null
    	if (tokens.popToken().getTokenType() != TokenType.L_BRACE) {
        	tokens.popStack(true);
        	return null;
    	}

        //Parse the body
        BodyNode parsedBody = BodyNode.parseNode(tokens, false);

        //Parsed body is null -> null
        if (parsedBody == null) {
        	tokens.popStack(true);
        	return null;
        }

    	//No closing Right Brace ( } ) -> null
    	if (tokens.popToken().getTokenType() != TokenType.R_BRACE) {
        	tokens.popStack(true);
        	return null;
    	}


    	tokens.popStack(false);
    	return new IfBlockNode(parseElseIfBlocks(tokens), parseElseBlock(tokens));

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
}


