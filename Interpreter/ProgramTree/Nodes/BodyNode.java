package Interpreter.ProgramTree.Nodes;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import provided.TokenType;



public class BodyNode extends NodeBase<BodyNode> {
    private ArrayList<BodyStatementNodeBase> statements;

    public BodyNode() {
        statements = new ArrayList<>();
    }

    public static BodyNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        BodyNode body = new BodyNode();
        BodyStatementNodeBase statement;

        while (tokens.peekToken().getTokenType() != TokenType.R_BRACE) {
            statement = BodyStatementNodeBase.parseNode(tokens);
            if (statement == null) {
                tokens.popStack(true);
                return null;
            }
            body.statements.add(statement);
        }

        tokens.popStack(false);
        return body;
    }
}
