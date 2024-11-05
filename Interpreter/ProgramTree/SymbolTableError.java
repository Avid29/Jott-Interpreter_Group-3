package Interpreter.ProgramTree;

public class SymbolTableError {
    private String filename;
    private int lineNum;

    public SymbolTableError(String filename, int lineNum){
        this.filename = filename;
        this.lineNum = lineNum;

    } 

    @Override
    public String toString(){
        String header = "Semantic error:\n";
        String message = "Duplicate symbol definition.\n";
        String footer = String.format("%s:%d",filename,lineNum);
        return header+message+footer;
    }

}
