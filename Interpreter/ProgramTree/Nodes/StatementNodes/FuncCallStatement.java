package Interpreter.ProgramTree.Nodes.StatementNodes;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionCallNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;

public class FuncCallStatement extends BodyStatementNodeBase {
    private FunctionCallNode wrapped;

    public FuncCallStatement(FunctionCallNode wrapped) {
        this.wrapped = wrapped;
    }

    public static FuncCallStatement parseNode(TokenStack tokens) {
        FunctionCallNode funcCall = FunctionCallNode.parseNode(tokens);
        return new FuncCallStatement(funcCall);
    }
}
