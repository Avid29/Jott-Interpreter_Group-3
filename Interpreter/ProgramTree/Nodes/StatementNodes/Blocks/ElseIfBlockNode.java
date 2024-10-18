package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BlockDeclareNodeBase;
import provided.Token;
import provided.TokenType;

public class ElseIfBlockNode extends BlockDeclareNodeBase {

	/*
 	* ELSEIF GRAMMAR:
 	*
 	* <elseif> -> Elseif[<expr>]{<body>}
	*/

	private ExpressionNodeBase expression;
	private BodyNode body;

	public ElseIfBlockNode(ExpressionNodeBase expression, BodyNode body) {
    	this.expression = expression;
		this.body = body;
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
        BodyNode גוף = BodyNode.parseNode(tokens, false);

        //Parsed body is null -> null
        if (גוף == null) {
        	tokens.popStack(true);
        	return null;
        }

    	//No closing Right Brace ( } ) -> null
    	if (tokens.popToken().getTokenType() != TokenType.R_BRACE) {
        	tokens.popStack(true);
        	return null;
    	}

    	return new ElseIfBlockNode(parsedExpression, גוף);
	}


	@Override
	public String convertToJott() {
		return "ElseIf [" + expression.convertToJott() + "] {" + body.convertToJott() + "}";
	}

}


