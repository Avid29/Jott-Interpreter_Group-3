package Interpreter.ErrorReporting;

public class ErrorReportRuntime extends ErrorReport {
    
    private static final String errorType = "Runtime Error";

    // ErrorReportRuntime class constructor
    public ErrorReportRuntime(String _errorMessage, String _errorLocation) {

        super(errorType, _errorMessage, _errorLocation);

    }
    
}