package Interpreter.ProgramTree.Nodes;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import provided.Token;

public class TypeNode extends NodeBase {
    private Token type;

    public TypeNode(Token type) {
        super(NodeType.TYPE);

        this.type = type;
    }
}
