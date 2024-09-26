package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BlockDeclareNodeBase;

public class ElseBlockNode extends BlockDeclareNodeBase {
    
    public ElseBlockNode() {
        super(NodeType.ELSE_BLOCK);
    }
}
