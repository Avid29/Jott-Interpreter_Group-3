package Interpreter.Parsing.Handlers.NodeCreation;

import java.util.function.Supplier;

import Interpreter.Parsing.JottTreeBuilder;
import Interpreter.Parsing.ParserStateHandlerBase;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.IBranchNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import provided.Token;

public class NodeCreationHandler<T extends NodeBase> extends ParserStateHandlerBase {
    private Supplier<T> supplier;
    private boolean target;

    public NodeCreationHandler(Supplier<T> supplier){
        this(supplier, true);
    }

    public NodeCreationHandler(Supplier<T> supplier, boolean target) {
        this.supplier = supplier;
        this.target = target;
    }

    @Override
    public void apply(JottTreeBuilder builder, Token token, TokenStack tokens) {
        var node = supplier.get();
        builder.InsertNode(node);

        if (target) {
            if (!IBranchNode.class.isInstance(node)) {
                // This isn't supposed to happen
            }

            builder.SelectNode((IBranchNode)node);
        }
    }
}
