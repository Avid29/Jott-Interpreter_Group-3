package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BlockDeclareNodeBase;

public class WhileBlockNode extends BlockDeclareNodeBase {

    public WhileBlockNode() {
        super(NodeType.WHILE_LOOP);
    }
    
}
