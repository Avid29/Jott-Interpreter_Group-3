package Interpreter.ProgramTree.Nodes.ExpressionNodes;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import provided.Token;

public class StringNode extends ExpressionNodeBase {
    private Token string;

    public StringNode(Token string) {
        super(NodeType.STRING);

        this.string = string;
    }
}
