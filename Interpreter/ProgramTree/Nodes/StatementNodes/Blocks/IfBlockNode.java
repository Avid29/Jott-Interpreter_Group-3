package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BlockDeclareNodeBase;

public class IfBlockNode extends BlockDeclareNodeBase {

    public IfBlockNode() {
        super(NodeType.IF_STMT);
    }
    
}
