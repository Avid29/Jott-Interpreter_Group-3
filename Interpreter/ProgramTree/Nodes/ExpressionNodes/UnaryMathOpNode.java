package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;

public class UnaryMathOpNode extends OperandNodeBase {
    private NumberNode child;

    public UnaryMathOpNode(NumberNode child) {
        this.child = child;
    }
}
