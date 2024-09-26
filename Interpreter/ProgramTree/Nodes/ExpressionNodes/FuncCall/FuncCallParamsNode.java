package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import java.util.ArrayList;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;

public class FuncCallParamsNode extends NodeBase {
    private ArrayList<ExpressionNodeBase> paramNodes;

    public FuncCallParamsNode(ArrayList<ExpressionNodeBase> paramNodes) {
        super(NodeType.FUNC_CALL_PARAMS);

        this.paramNodes = paramNodes;
    }
}
