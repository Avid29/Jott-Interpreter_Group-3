package Interpreter.ProgramTree.Nodes.StatementNodes.Abstract;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;

public abstract class BlockDeclareNodeBase extends BodyStatementNodeBase {
    protected ExpressionNodeBase condition;
    protected BodyNode body;

    public static BlockDeclareNodeBase parseNode(TokenStack tokens) {

    }
}
