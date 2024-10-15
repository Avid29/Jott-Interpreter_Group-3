package Interpreter.ProgramTree.Nodes.StatementNodes;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import provided.Token;
import provided.TokenType;

public class AssignmentNode extends BodyStatementNodeBase {
    private Token target;
    private ExpressionNodeBase expression;

    public AssignmentNode(Token target, ExpressionNodeBase expression){
        this.target = target;
        this.expression = expression;
    }

    public static AssignmentNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        ArrayList<Token> popped = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(new TokenType[] { TokenType.ID, TokenType.ASSIGN }, popped);
        if (errorCode != -1) {
            // Let the parent function handle reporting this error.
            tokens.popStack(true);
            return null;
        }

        var id = popped.get(0);

        var expression = ExpressionNodeBase.parseNode(tokens);
        if (expression == null)
        {
            tokens.popStack(true);
            return null;
        }

        // Ensure semi-colon
        var statementEnd = tokens.popToken();
        if (statementEnd.getTokenType() != TokenType.SEMICOLON)
        {
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new AssignmentNode(id, expression);
    }
}
