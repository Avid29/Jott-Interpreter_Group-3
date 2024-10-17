package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.BodyNode;
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
        
        //Parse the body
        BodyNode parsedBody = BodyNode.parseNode(tokens, false);

        //Parsed body is null -> null
        if (parsedBody == null) {
        	tokens.popStack(true);
        	return null;
        }

    	//No closing Right Brace ( } ) -> null
    	if (!tokens.popToken().getTokenType().equals(TokenType.R_BRACE)) {
        	tokens.popStack(true);
        	return null;
    	}

    	tokens.popStack(false);
    	return new ElseBlockNode();
    
	}
}
