package Interpreter.ProgramTree.Nodes.StatementNodes;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Enums.NodeTypeGroup;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.Nodes.Abstract.IBranchNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;
import provided.TokenType;

public class VariableDeclarationNode extends BodyStatementNodeBase implements IBranchNode {
    private TypeNode type;
    private VarRefNode name;

    public VariableDeclarationNode(TypeNode type, VarRefNode name, boolean param) {
        this(param);
        this.type = type;
        this.name = name;
    }

    public VariableDeclarationNode(boolean param) {
        super(param ? NodeType.FUNC_DEF_PARAM : NodeType.VAR_DECL);
    }

    @Override
    public void AddChild(NodeBase child) {
        switch (child) {
            case TypeNode node:
                if (type != null) {
                    // This isn't supposed to happen
                }

                type = node;
                break;
            case VarRefNode node:
                if (name != null) {
                    // This isn't supposed to happen
                }

                name = node;
                break;

            default:
                // This isn't supposed to happen either
                break;
        }

        child.setParent(this);
    }

    @Override
    public NodeTypeGroup getExpectedGroup() {
        return NodeTypeGroup.VAR_DECL;
    }
    
    @Override
    public TokenType getClosureType() {
        return TokenType.SEMICOLON;
    }

    @Override
    public boolean isComplete() {
        return type != null && name != null;
    }
}
