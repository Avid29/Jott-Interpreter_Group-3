package Interpreter.ProgramTree.Nodes.StatementNodes;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;

public class VariableDeclarationNode extends BodyStatementNodeBase {
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
}
