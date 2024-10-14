package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BlockDeclareNodeBase;
import provided.Token;
import provided.TokenType;

public class ElseIfBlockNode extends BlockDeclareNodeBase {

	/*
 	* ELSEIF GRAMMAR:
 	*
 	* <elseif> -> Elseif[<expr>]{<body>}
	*/


	ArrayList<ElseIfBlockNode> elseIfBlocks;


	public ElseIfBlockNode() {
    	/*... */
	}


	public static ElseIfBlockNode parseNode(TokenStack tokens) {

    	//First token not 'ElseIf' -> null
    	if (!tokens.popToken().getToken().equals("ElseIf"))
        	return null;



    	//No opening Left Bracket ( [ ) -> null
    	if (tokens.peekToken().getTokenType() != TokenType.L_BRACKET) {
        	tokens.popStack(true);
        	return null;
    	}

    	/*
        	TODO : Parse the expression

        	...
    	*/

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

    	/*
        	TODO : Parse the body

        	if (!BlockDeclareNodeBase.parseBody(tokens)) {
            	...
        	}

    	*/

    	//No closing Right Brace ( } ) -> null
    	if (tokens.popToken().getTokenType() != TokenType.R_BRACE) {
        	tokens.popStack(true);
        	return null;
    	}



    	return new ElseIfBlockNode();

	}

}


