package Interpreter.ProgramTree.Nodes;

import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import provided.Token;

public class TypeNode extends NodeBase<TypeNode> {
    private Token type;

    public TypeNode(Token type) {
        this.type = type;
    }
}
