package Interpreter.ProgramTree;

import java.util.HashMap;

import Interpreter.Tuple2;

//holds a function symbol table in a dict
//Takes soemthing as an argument 
//I have no idea what goes here 
public class ProgramSymbolTable {
    private HashMap<String,Tuple2<FuncInfo, FunctionSymbolTable>> table;

    //constructor
    public void defineSymbol(String symbol, FunctionSymbolTable table2, FuncInfo info,int lineNum, String fileName){
        if(table.containsKey(symbol)){
            //throw new Exception
            //System.err.println(SymbolTableError);
            System.err.println(new SymbolTableError(fileName, lineNum));
            return;
            
            
        }

        table.put(symbol, new Tuple2<>(info, table2));
    }
}
