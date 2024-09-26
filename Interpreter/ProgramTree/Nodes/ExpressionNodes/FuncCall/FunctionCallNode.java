package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;

public class FunctionCallNode extends OperandNodeBase {
    private FunctionRefNode funcName;
    private FuncCallParamsNode callParams;

    public FunctionCallNode(FunctionRefNode name, FuncCallParamsNode params) {
        this();
        this.funcName = name;
        this.callParams = params;
    }

    public FunctionCallNode() {
        super(NodeType.FUNC_CALL);
    }
}
