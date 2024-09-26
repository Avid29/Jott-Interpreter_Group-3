package Interpreter.ProgramTree.Nodes.StatementNodes.Abstract;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;

public abstract class BlockDeclareNodeBase extends BodyStatementNodeBase {
    public BlockDeclareNodeBase(NodeType type) {
        super(type);
    }

    protected ExpressionNodeBase condition;
    protected BodyNode body;
}
