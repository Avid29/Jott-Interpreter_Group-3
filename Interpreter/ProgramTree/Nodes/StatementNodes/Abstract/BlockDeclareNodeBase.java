package Interpreter.ProgramTree.Nodes.StatementNodes.Abstract;

import Interpreter.ProgramTree.Nodes.BodyNode;

public abstract class BlockDeclareNodeBase extends BodyStatementNodeBase {
    protected BodyNode body;

    public boolean containsReturn() {
      return body.containsReturn();
    }
}
