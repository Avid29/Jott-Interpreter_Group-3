package Interpreter.ProgramTree.Nodes;

import provided.Token;


public class SymbolInfo {
    
    
    private Token idToken;
    private Token varRefToken;


    public SymbolInfo(Token idToken, Token varRefToken){
        this.idToken = idToken;
        this.varRefToken = varRefToken;
    }

    public Token getIdToken(){
        return idToken;
    }
    public Token getVarRefToken(){
        return varRefToken;
    }

    public boolean acceptsAssignment(Token source){

        return (idToken.getTokenType() == source.getTokenType());

    }


}
