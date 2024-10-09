package Interpreter.ProgramTree.Nodes.StatementNodes;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import provided.Token;

public class ReturnStatementNode extends BodyStatementNodeBase {
    public static ReturnStatementNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        Token next = tokens.popToken();

        if (!next.getToken().equals("Return")){
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return null;
    }
}
