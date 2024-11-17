package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BlockDeclareNodeBase;
import provided.TokenType;
import provided.Token;

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

	public static void reportNoPrecedingIf() {

		ErrorReport.makeError(
			ErrorReportSyntax.class,
			"ElseBlockNode -- Else block without preceding 'If'",
			TokenStack.get_last_token_popped()
		);

	}

		
	public static ElseBlockNode parseNode(TokenStack tokens) {

		/*
		 * Precaution to ensure that Else blocks won't be parsed
		 * unless a boolean value is passed into the overloaded
		 * version of the 'parseNode' function.
		*/
		
		reportNoPrecedingIf();

		return null;

	}

	public static ElseBlockNode parseNode(TokenStack tokens, boolean calledFromPrecedingIfBlock /* <- Doesn't do anything just for overloading */) {

    	tokens.pushStack();

    	//No "Else" keyword -> null
		Token nextToken = tokens.popToken();
    	if (!nextToken.getToken().equals("Else")) {

            ErrorReport.makeError(ErrorReportSyntax.class, "ElseBlockNode -- Expected 'Else', got "+nextToken.getTokenType(), TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;

    	}
        
        //Parse the body
        BodyNode parsedBody = BodyNode.parseNode(tokens, false);

        //Parsed body is null -> null
        if (parsedBody == null) {

			ErrorReport.makeError(ErrorReportSyntax.class, "ElseBlockNode -- Failed to parse body", TokenStack.get_last_token_popped());

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


    public BodyNode getBodyNode() {
        return body;
    }

}
