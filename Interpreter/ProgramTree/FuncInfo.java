package Interpreter.ProgramTree;

import java.util.ArrayList;

import Interpreter.ProgramTree.Nodes.FunctionNodes.FunctionDefNode;
import provided.Token;

public class FuncInfo {
    public Token returnType;
    public ArrayList<String> paramTypes;
    public FunctionSymbolTable funcTable;
    
    public FuncInfo(FunctionDefNode fdn){
        returnType = fdn.returnType.getType();
        paramTypes = new ArrayList<>(); 
        for (var param : fdn.params.paramNodes){
            paramTypes.add(param.type.getType().getToken());
        }
    }
}
