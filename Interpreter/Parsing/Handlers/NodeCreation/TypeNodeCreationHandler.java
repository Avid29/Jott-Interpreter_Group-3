package Interpreter.Parsing.Handlers.NodeCreation;

import java.util.function.Supplier;

import Interpreter.Parsing.JottTreeBuilder;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.TypeNode;
import provided.Token;

public class TypeNodeCreationHandler extends NodeCreationHandler<TypeNode> {

    public TypeNodeCreationHandler() {
        super(null, false);
    }

    @Override
    public void apply(JottTreeBuilder builder, Token token, TokenStack tokens) {
        var node = new TypeNode(token);
        builder.InsertNode(node);
    }
}
