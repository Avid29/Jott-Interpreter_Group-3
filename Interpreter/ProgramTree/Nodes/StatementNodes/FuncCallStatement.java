package Interpreter.ProgramTree.Nodes.StatementNodes;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionCallNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;

public class FuncCallStatement extends BodyStatementNodeBase {
    private FunctionCallNode wrapping;

    public FuncCallStatement() {
        super(NodeType.FUNC_CALL);

        wrapping = new FunctionCallNode();
    }
}
