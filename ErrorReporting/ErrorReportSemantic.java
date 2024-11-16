package ErrorReporting;

public class ErrorReportSemantic extends ErrorReport {
    
    private static final String errorType = "Semantic Error";

    // ErrorReportSemantic class constructor
    public ErrorReportSemantic(String _errorMessage, String _errorLocation) {

        super(errorType, _errorMessage, _errorLocation);

    }
    
}
