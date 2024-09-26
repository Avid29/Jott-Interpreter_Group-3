package Interpreter.ProgramTree.Nodes.StatementNodes;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;

public class AssignmentNode extends NodeBase {

    public AssignmentNode() {
        super(NodeType.ASSIGNMENT);
    }
}
