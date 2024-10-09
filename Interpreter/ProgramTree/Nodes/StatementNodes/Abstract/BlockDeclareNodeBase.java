package Interpreter.ProgramTree.Nodes.StatementNodes.Abstract;

import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;

public abstract class BlockDeclareNodeBase extends BodyStatementNodeBase {
    protected ExpressionNodeBase condition;
    protected BodyNode body;
}
