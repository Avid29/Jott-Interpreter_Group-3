package Interpreter.ProgramTree.Nodes;

import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;
import provided.Token;


public class SymbolInfo {
    
    
    private final Token idToken;
    private final Token varRefToken;
    private ExpressionNodeBase value;

    private Object valueRaw = null;


    public SymbolInfo(Token idToken, Token varRefToken, ExpressionNodeBase value) {
        this.idToken = idToken;             //e.g. 'Integer'
        this.varRefToken = varRefToken;     //e.g. 'x'
        this.value = value;                 //e.g. '5'
    } 


    public Token getIdToken(){
        return idToken;
    }
    public Token getVarRefToken(){
        return varRefToken;
    }
    public ExpressionNodeBase getValue(){
        return value;
    }


    public void setValue(ExpressionNodeBase value){
        this.value = value;
    }


    public void evaluate() {
        valueRaw = value.evaluate();
    }
    public Object getValueRaw(){
        return valueRaw;
    }
    public void setValueRaw(Object valueRaw){
        this.valueRaw = valueRaw;
    }


    @Override
    public String toString() {
        return "SymbolInfo{" +
                "idToken=" + idToken.getToken() +
                ", varRefToken=" + varRefToken.getToken() +
                ", value=" + value +
                ", valueRaw=" + valueRaw +
                '}';
    }


}
