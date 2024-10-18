package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BlockDeclareNodeBase;
import provided.TokenType;

public class ElseBlockNode extends BlockDeclareNodeBase {

	private BodyNode body;

	/*
 	* ELSE GRAMMAR:
 	*
 	* <else> -> Else{<body>} | ϵ
	*/

	public ElseBlockNode(BodyNode body) {
    	this.body = body;
	}

	public static ElseBlockNode parseNode(TokenStack tokens) {

    	tokens.pushStack();

    	//No "Else" keyword -> null
    	if (!tokens.popToken().getToken().equals("Else")) {
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

    	tokens.popStack(false);
    	return new ElseBlockNode(parsedBody);
	}

	@Override
	public String convertToJott() {
		return "Else" + body.convertToJott();
	}

}
