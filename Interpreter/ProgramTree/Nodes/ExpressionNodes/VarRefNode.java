package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.Token;

public class VarRefNode extends OperandNodeBase {
    private Token id;

    public VarRefNode(Token id) {
        this.id = id;
    }
}
