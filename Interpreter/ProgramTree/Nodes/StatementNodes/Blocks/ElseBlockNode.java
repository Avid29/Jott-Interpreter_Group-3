package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BlockDeclareNodeBase;
import provided.Token;
import provided.TokenType;

public class ElseBlockNode extends BlockDeclareNodeBase {

	/*
 	* ELSE GRAMMAR:
 	*
 	* <else> -> Else{<body>} | ϵ
	*/

	public ElseBlockNode() {
    	/*... */
	}

	public static ElseBlockNode parseNode(TokenStack tokens) {

    	tokens.pushStack();

    	//No "Else" keyword -> null
    	if (!tokens.popToken().getToken().equals("Else")) {
        	tokens.popStack(true);
        	return null;
    	}

    	//No opening Left Brace ( { ) -> null
    	if (!tokens.popToken().getTokenType().equals(TokenType.L_BRACE)) {
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
    	if (!tokens.popToken().getTokenType().equals(TokenType.R_BRACE)) {
        	tokens.popStack(true);
        	return null;
    	}

    	tokens.popStack(false);
    	return new ElseBlockNode();
    
	}

}


