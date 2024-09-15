package Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;

public abstract class ExpressionNodeBase extends NodeBase {

    public ExpressionNodeBase(NodeType type) {
        super(type);
    }
}
