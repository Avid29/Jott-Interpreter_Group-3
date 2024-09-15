package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BlockDeclareNodeBase;

public class ElseIfBlockNode extends BlockDeclareNodeBase {

    public ElseIfBlockNode() {
        super(NodeType.ELSEIF_BLOCK);
    }
}
