package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.OperandNodeBase;

public class NumberNode extends OperandNodeBase {

    public NumberNode() {
        super(NodeType.NUMBER);
    }
    
}
