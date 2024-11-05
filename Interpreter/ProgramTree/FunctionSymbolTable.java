package Interpreter.ProgramTree;

import java.util.HashMap;

import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;
import provided.JottTokenizer;

public class FunctionSymbolTable {
    private HashMap<String,VarInfo> table;

    public void DefineSymbol(String symbol, VariableDeclarationNode var, int lineNum, String fileName)throws Exception{
        if(table.containsKey(symbol)){
            //throw new Exception, 
            //System.err.println(SymbolTableError);
            System.err.println(new SymbolTableError(fileName, lineNum));
            return;
            
            // TODO: Error, duplicate symbol definition.
        }

        table.put(symbol, new VarInfo(var.type));
    }
}

