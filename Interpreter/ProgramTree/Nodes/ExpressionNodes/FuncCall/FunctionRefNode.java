package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import Interpreter.ProgramTree.FunctionSymbolTable;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import provided.Token;

public class FunctionRefNode extends NodeBase<FunctionRefNode> {
    
    private final Token id;

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

    @Override
    public void execute() {

        //Check if function is defined
        if (!FunctionSymbolTable.programContainsFunction(id.getToken())) {
            //System.out.println("[EX]  Function "+id.getToken()+" is not defined!");
            return;
        }

        //Execute function
        FunctionSymbolTable.executeFunction(id.getToken());

    }

}
