package src.Interpreter.Tokenization;

public class TokenizerError {
    private String filename;
    private int lineNum;
    private int columnNum;
    private TokenizerState state;
    private String tokenProgress;
    private char nextChar;

    public TokenizerError(String filename, int lineNum, int columnNum, TokenizerState state, String tokenProgress, char nextChar) {
        this.filename = filename;
        this.lineNum = lineNum;
        this.columnNum = columnNum;
        this.state = state;
        this.tokenProgress = tokenProgress;
        this.nextChar = nextChar;
    }

    @Override
    public String toString() {
        String header = "Syntax Error\n";
        String message = String.format("'%c' is not a valid character in %s state", nextChar, state.name());
        String footer = String.format("%s:%d%n", filename, lineNum, columnNum);
        return header + message + "\n" + footer;
    }
}
