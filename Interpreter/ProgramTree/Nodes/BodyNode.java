package Interpreter.ProgramTree.Nodes;

import java.util.ArrayList;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;

public class BodyNode extends NodeBase {
    private ArrayList<BodyStatementNodeBase> statements;

    public BodyNode() {
        super(NodeType.BODY);

        statements = new ArrayList<>();
    }
}
