package Interpreter.ProgramTree.Nodes;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;

public class BodyNode extends NodeBase<BodyNode> {
    private ArrayList<BodyStatementNodeBase> statements;

    public BodyNode() {
        statements = new ArrayList<>();
    }

    public static BodyNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        tokens.popStack(false);
        return null;
    }
}
