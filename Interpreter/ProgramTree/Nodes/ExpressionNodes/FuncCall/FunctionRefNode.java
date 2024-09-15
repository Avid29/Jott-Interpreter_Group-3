package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import provided.Token;

public class FunctionRefNode extends NodeBase {
    private Token id;

    public FunctionRefNode(Token id){
        super(NodeType.FUNC_REF);

        this.id = id;
    }
}
