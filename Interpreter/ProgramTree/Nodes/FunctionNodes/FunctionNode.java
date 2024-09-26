package Interpreter.ProgramTree.Nodes.FunctionNodes;

import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionRefNode;
import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.BodyNode;

public class FunctionNode extends NodeBase {
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
}
