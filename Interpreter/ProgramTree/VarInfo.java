package Interpreter.ProgramTree;

import Interpreter.ProgramTree.Nodes.TypeNode;

public class VarInfo {
    public TypeNode type; 
    public Boolean isInitialized = false;  

    //constructor 
    public VarInfo(TypeNode type){
        this.type = type;
    }

    //get type
    public TypeNode getType(){
        return type;
    }
}
