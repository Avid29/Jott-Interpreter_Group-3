package Interpreter.ProgramTree.Nodes.FunctionNodes;

import Interpreter.ProgramTree.Nodes.Abstract.IBranchNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionRefNode;
import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Enums.NodeTypeGroup;
import Interpreter.ProgramTree.Nodes.BodyNode;
import provided.TokenType;

public class FunctionNode extends NodeBase implements IBranchNode {
    private FunctionRefNode funcName;
    private ParametersDefNode params;
    private BodyNode body;

    public FunctionNode(FunctionRefNode name, ParametersDefNode params, BodyNode body) {
        this();
        this.funcName = name;
        this.params = params;
        this.body = body;
    }

    public FunctionNode(){
        super(NodeType.FUNC_DEF);
    }

    @Override
    public void AddChild(NodeBase child) {

        switch (child) {
            case FunctionRefNode node:
                if (funcName != null) {
                    // This isn't supposed to happen
                }

                funcName = node;
                break;
            case ParametersDefNode node:
                if (params != null) {
                    // This isn't supposed to happen
                }

                params = node;
                break;
            case BodyNode node:
                if (body != null) {
                    // This isn't supposed to happen
                }

                body = node;
                break;
        
            default:
                // This isn't supposed to happen either
                break;
        }

        child.setParent(this);
    }

    @Override
    public NodeTypeGroup getExpectedGroup() {
        return NodeTypeGroup.FUNCTION_DECL;
    }
    
    @Override
    public TokenType getClosureType() {
        return TokenType.R_BRACE;
    }

    @Override
    public boolean isComplete() {
        return funcName != null && params != null && body != null;
    }
}
