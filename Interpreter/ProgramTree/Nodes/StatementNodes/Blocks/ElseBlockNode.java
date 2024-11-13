package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
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

        // //Check if the previous token was an "If" or "ElseIf" keyword
        // if (!tokens.checkPreviousToken(TokenType.KEYWORD)) {
        //     ErrorReport.makeError(ErrorReportSyntax.class, "Expected 'If' or 'ElseIf' keyword", TokenStack.get_last_token_popped());
        // 	tokens.popStack(true);
        // 	return null;
        // }
        

    	//No "Else" keyword -> null
    	if (!tokens.popToken().getToken().equals("Else")) {

            ErrorReport.makeError(ErrorReportSyntax.class, "Expected 'Else'", TokenStack.get_last_token_popped());

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
