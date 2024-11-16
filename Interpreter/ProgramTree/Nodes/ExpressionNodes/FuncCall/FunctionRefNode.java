package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import provided.Token;
import Interpreter.ProgramTree.FunctionSymbolTable;

public class FunctionRefNode extends NodeBase<FunctionRefNode> {
    
    private Token id;

    public FunctionRefNode(Token id) {

        this.id = id;
        
    }

    public Token getId() {
        return id;
    }

    @Override
    public String convertToJott() {
        return id.getToken();
    }
}
