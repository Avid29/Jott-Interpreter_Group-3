package Interpreter.ProgramTree.Nodes.StatementNodes;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionCallNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;

public class FuncCallStatementNode extends BodyStatementNodeBase {
    private FunctionCallNode wrapped;

    public FuncCallStatementNode(FunctionCallNode wrapped) {
        this.wrapped = wrapped;
    }

    public static FuncCallStatementNode parseNode(TokenStack tokens) {
        FunctionCallNode funcCall = FunctionCallNode.parseNode(tokens);
        return new FuncCallStatementNode(funcCall);
    }
}
