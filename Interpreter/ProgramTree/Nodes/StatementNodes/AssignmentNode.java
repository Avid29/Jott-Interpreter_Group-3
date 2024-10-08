package Interpreter.ProgramTree.Nodes.StatementNodes;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import provided.Token;
import provided.TokenType;

public class AssignmentNode extends BodyStatementNodeBase {
    public static AssignmentNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        ArrayList<Token> popped = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(new TokenType[] { TokenType.ID, TokenType.ASSIGN }, popped);

        if (errorCode != -1) {
            // Let the parent function handle reporting this error.
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return null;
    }
}
