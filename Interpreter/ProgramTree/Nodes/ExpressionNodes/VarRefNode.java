package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.Token;

public class VarRefNode extends OperandNodeBase {
    private Token id;

    public VarRefNode(Token id) {
        super(NodeType.VAR_REF);

        this.id = id;
    }
}
