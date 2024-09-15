package Interpreter.ProgramTree.Nodes.FunctionNodes;

import java.util.ArrayList;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Enums.NodeTypeGroup;
import Interpreter.ProgramTree.Nodes.Abstract.IBranchNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;
import provided.TokenType;

public class ParametersDefNode extends NodeBase implements IBranchNode {
    private ArrayList<VariableDeclarationNode> paramNodes;

    public ParametersDefNode() {
        this(new ArrayList<>());
    }

    public ParametersDefNode(ArrayList<VariableDeclarationNode> paramNodes) {
        super(NodeType.FUNC_DEF_PARAMS);

        this.paramNodes = paramNodes;
    }

    @Override
    public void AddChild(NodeBase child) {
        if (!VariableDeclarationNode.class.isInstance(child)) {
            // This isn't supposed to happen
        }

        if (child != null) {
            // This isn't supposed to happen either.
        }
        
        child = (VariableDeclarationNode)child;        
        child.setParent(this);
    }

    @Override
    public NodeTypeGroup getExpectedGroup() {
        return NodeTypeGroup.FUNCTION_DECL;
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
