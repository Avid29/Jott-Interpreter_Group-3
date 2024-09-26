package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;

public class UnaryMathOpNode extends OperandNodeBase {
    private NumberNode child;

    public UnaryMathOpNode(NumberNode child) {
        super(NodeType.OPERATOR);

        this.child = child;
    }
}
