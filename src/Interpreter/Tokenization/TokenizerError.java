package src.Interpreter.Tokenization;

public class TokenizerError {
    private String filename;
    private int lineNum;
    private int columnNum;
    private TokenizerState state;
    private String tokenProgress;
    private char nextChar;

    public TokenizerError(String filename, int lineNum, int columnNum, TokenizerState state, String tokenProgress,
            char nextChar) {
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
        String message = errorAsMessage();
        String footer = String.format("%s:%d%n", filename, lineNum, columnNum);
        return header + message + "\n" + footer;
    }

    private String errorAsMessage() {
        if (state == TokenizerState.BANG) {
            // Failed due to lone '!'
            return "Invalid token \"!\". Expected '='.";
        }

        if (state == TokenizerState.DECIMAL) {
            // Failed due to lone '.'
            return "Invalid token \".\". Expected leading or trailing digit";
        }

        if (state == TokenizerState.STRING) {
            // Failed due to unclosed string
            return "String literal missing closing quotes";
        }

        if (nextChar == '\0') {
            // Failed in finalization
            return String.format("\"%s\" is not a complete token.", tokenProgress);
        }

        return "Unrecognizable error";
    }
}
