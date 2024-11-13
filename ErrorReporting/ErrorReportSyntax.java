package ErrorReporting;

public class ErrorReportSyntax extends ErrorReport {
    
    private static final String errorType = "Syntax Error";

    // ErrorReportSyntax class constructor
    public ErrorReportSyntax(String _errorMessage, String _errorLocation) {

        super(errorType, _errorMessage, _errorLocation);

    }

}
