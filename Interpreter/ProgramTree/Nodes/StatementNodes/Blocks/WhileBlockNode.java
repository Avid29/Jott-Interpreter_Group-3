package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BlockDeclareNodeBase;
import java.util.ArrayList;
import provided.Token;
import provided.TokenType;

public class WhileBlockNode extends BlockDeclareNodeBase {

    private ExpressionNodeBase expression;

    public WhileBlockNode(ExpressionNodeBase expression, BodyNode body){
        this.expression = expression;
        this.body = body;
    }

    public static WhileBlockNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        ArrayList<Token> popped = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(new TokenType[]{TokenType.KEYWORD, TokenType.L_BRACKET}, popped);

        if (errorCode != -1) {

            // Missing conditional expression
            ErrorReport.makeError(ErrorReportSyntax.class, "WhileBlockNode -- Missing conditional expression", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        if (!popped.get(0).getToken().equals("While")) {

            ErrorReport.makeError(ErrorReportSyntax.class, "WhileBlockNode -- Expected 'While'", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }
   	 
    	//Parse the expression
        ExpressionNodeBase parsedExpression = ExpressionNodeBase.parseNode(tokens);

        //Parsed expression is null -> null
        if (parsedExpression == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "WhileBlockNode -- Failed to parse Expression", TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;
        }
        
    	//No closing Right Bracket ( ] ) -> null
    	if (tokens.popToken().getTokenType() != TokenType.R_BRACKET) {

            ErrorReport.makeError(ErrorReportSyntax.class, "WhileBlockNode -- Expecting Right Bracket ']'", TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;
    	}

        //Parse the body
        BodyNode parsedBody = BodyNode.parseNode(tokens, false);

        //Parsed body is null -> null
        if (parsedBody == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "While Block -- Failed to parse Body", TokenStack.get_last_token_popped());

        	tokens.popStack(true);
        	return null;
        }

        return new WhileBlockNode(parsedExpression, parsedBody);
    }

    @Override
    public String convertToJott() {
		return "While [" + expression.convertToJott() + "]" + body.convertToJott();
    }

    public BodyNode getBodyNode() {
        return body;
    }

    @Override
    public void execute() {

        //System.out.println("[EX]ecuting 'WhileBlockNode': ");

        //Evaluate the expression
        while (expression.evaluateBoolean()) {

            //System.out.println("[EX] 'WhileBlockNode' (execute) ...Executing Body");
            body.execute();
            
        }
        
    }
}
