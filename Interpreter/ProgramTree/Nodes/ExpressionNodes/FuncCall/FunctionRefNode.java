package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import provided.Token;

public class FunctionRefNode extends NodeBase<FunctionRefNode> {
    private Token id;

    public FunctionRefNode(Token id) {
        this.id = id;
    }
}
