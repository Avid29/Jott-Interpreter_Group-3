package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Enums.NodeTypeGroup;
import Interpreter.ProgramTree.Nodes.Abstract.IBranchNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;
import provided.TokenType;

public class FunctionCallNode extends OperandNodeBase implements IBranchNode {
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

    @Override
    public void AddChild(NodeBase child) {
        if (!FunctionRefNode.class.isInstance(child)) {
            // This isn't supposed to happen
        }

        if (funcName != null) {
            // This isn't supposed to happen either
        }

        funcName = (FunctionRefNode)child;
        child.setParent(this);
    }

    @Override
    public NodeTypeGroup getExpectedGroup() {
        return NodeTypeGroup.FUNCTION_CALL;
    }
    
    @Override
    public TokenType getClosureType() {
        return TokenType.SEMICOLON;
    }

    @Override
    public boolean isComplete() {
        return funcName != null;
    }
}
