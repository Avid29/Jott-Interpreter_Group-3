package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Enums.NodeTypeGroup;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BlockDeclareNodeBase;

public class ElseBlockNode extends BlockDeclareNodeBase {
    
    public ElseBlockNode() {
        super(NodeType.ELSE_BLOCK);
    }

    @Override
    public void AddChild(NodeBase child) {
        if (ExpressionNodeBase.class.isInstance(child)) {
            // This isn't supposed to happen
        }

        super.AddChild(child);
    }

    @Override
    public NodeTypeGroup getExpectedGroup() {
        return NodeTypeGroup.BLOCK;
    }

    @Override
    public boolean isComplete() {
        return condition == null && body != null;
    }
}
