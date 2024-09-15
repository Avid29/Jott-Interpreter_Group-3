package Interpreter.ProgramTree.Nodes.StatementNodes;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BodyStatementNodeBase;

public class ReturnStatementNode extends BodyStatementNodeBase {

    public ReturnStatementNode() {
        super(NodeType.RETURN_STATEMENT);
    }
    
}
