package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import java.util.ArrayList;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSyntax;
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

	public static void reportNoPrecedingIf() {

		ErrorReport.makeError(
			ErrorReportSyntax.class,
			"ElseIfBlockNode -- Elseif block without preceding 'If'",
			TokenStack.get_last_token_popped()
		);

	}

	public static ElseIfBlockNode parseNode(TokenStack tokens) {

		/*
		 * Precaution to ensure that Elseif blocks won't be parsed
		 * unless a boolean value is passed into the overloaded
		 * version of the 'parseNode' function.
		*/
		
		reportNoPrecedingIf();

		return null;

	}

	public static ElseIfBlockNode parseNode(TokenStack tokens, boolean calledFromPrecedingIfBlock /* <- Doesn't do anything just for overloading */) {

    	//First token not 'Elseif' -> null
		Token nextToken = tokens.popToken();
    	if (!nextToken.getToken().equals("Elseif")) {

			ErrorReport.makeError(ErrorReportSyntax.class, "ElseIfBlockNode -- Expected 'Elseif', got "+nextToken.getTokenType(), TokenStack.get_last_token_popped());
        	return null;

		}


    	//No opening Left Bracket ( [ ) -> null
		Token nextTokenPeeked = tokens.peekToken();
    	if (nextTokenPeeked.getTokenType() != TokenType.L_BRACKET) {

			ErrorReport.makeError(ErrorReportSyntax.class, "ElseIfBlockNode -- Expected Left Bracket '[', got "+nextTokenPeeked.getTokenType(), TokenStack.get_last_token_popped());
			
        	tokens.popStack(true);
        	return null;
    	}

    	//Parse the expression
        ExpressionNodeBase parsedExpression = ExpressionNodeBase.parseNode(tokens);

        //Parsed expression is null -> null
        if (parsedExpression == null) {

			ErrorReport.makeError(ErrorReportSyntax.class, "ElseIfBlockNode -- Failed to parse expression", TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;
        }
        
    	//No closing Right Bracket ( ] ) -> null
		nextTokenPeeked = tokens.peekToken();
    	if (nextTokenPeeked.getTokenType() != TokenType.R_BRACKET) {

			ErrorReport.makeError(ErrorReportSyntax.class, "ElseIfBlockNode -- Expected Right Bracket ']', got "+nextTokenPeeked.getTokenType(), TokenStack.get_last_token_popped());
			
        	tokens.popStack(true);
        	return null;
    	}

        //Parse the body
        BodyNode body = BodyNode.parseNode(tokens, false);

        //Parsed body is null -> null
        if (body == null) {

			ErrorReport.makeError(ErrorReportSyntax.class, "ElseIfBlockNode -- Failed to parse body", TokenStack.get_last_token_popped());
			
        	tokens.popStack(true);
        	return null;
        }

    	return new ElseIfBlockNode(parsedExpression, body);
	}


	@Override
	public String convertToJott() {
		return "Elseif [" + expression.convertToJott() + "]" + body.convertToJott();
	}

    public BodyNode getBodyNode() {
        return body;
    }

}


