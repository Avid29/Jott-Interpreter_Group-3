package Interpreter.ProgramTree.Nodes.FunctionNodes;

import java.util.ArrayList;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;

public class ParametersDefNode extends NodeBase {
    private ArrayList<VariableDeclarationNode> paramNodes;

    public ParametersDefNode() {
        this(new ArrayList<>());
    }

    public ParametersDefNode(ArrayList<VariableDeclarationNode> paramNodes) {
        super(NodeType.FUNC_DEF_PARAMS);

        this.paramNodes = paramNodes;
    }
}
