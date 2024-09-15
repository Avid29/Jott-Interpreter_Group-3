package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import java.util.ArrayList;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Enums.NodeTypeGroup;
import Interpreter.ProgramTree.Nodes.Abstract.IBranchNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import provided.TokenType;

public class FuncCallParamsNode extends NodeBase implements IBranchNode {
    private ArrayList<ExpressionNodeBase> paramNodes;

    public FuncCallParamsNode() {
        this(new ArrayList<>());
    }

    public FuncCallParamsNode(ArrayList<ExpressionNodeBase> paramNodes) {
        super(NodeType.FUNC_CALL_PARAMS);

        this.paramNodes = paramNodes;
    }

    @Override
    public void AddChild(NodeBase child) {
        if (!ExpressionNodeBase.class.isInstance(child)) {
            // This isn't supposed to happen
        }

        paramNodes.add((ExpressionNodeBase)child);
        child.setParent(this);
    }

    @Override
    public NodeTypeGroup getExpectedGroup() {
        return NodeTypeGroup.EXPRESSION;
    }

    @Override
    public TokenType getClosureType() {
        return TokenType.R_BRACKET;
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}
