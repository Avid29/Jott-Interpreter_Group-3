package Interpreter.ErrorReporting;

import Interpreter.Parsing.TokenStack;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;
import provided.Token;

public class ErrorReport {
    
    private static ErrorReport errorReportStatic = null;

    //ErrorReport class variables
    private final static Stack<ErrorReport> errorStack = new Stack<>();
    private final String errorMessage;

    //ErrorReport class constructor
    public ErrorReport(String _errorType, String _errorMessage, String _errorLocation) {
        
        //Format Error Message
        this.errorMessage = ("\n\t" + _errorType + "\n\t" + _errorMessage + "\n\t" + _errorLocation + "\n");

        errorStack.push(this);

    }

    public static void print_error_message() {

        //No Error Message instance to print! (Just print an unknown error instead?)
        if (errorReportStatic == null) {
            
            /*
                Get the location from the last Token popped (if it exists) -- but it will likely just be the last possible Token in the file.
            */
            Token lastTokenPopped = TokenStack.get_last_token_popped();

            if (lastTokenPopped != null)
                errorReportStatic = new ErrorReport("<< Unknown Error >>", "An unknown error occured!", (lastTokenPopped.getFilename() + ":" + lastTokenPopped.getLineNum()));
            else
                errorReportStatic = new ErrorReport("<< Unknown Error >>", "An unknown error occured!", "<< Unknown Error Location >>");
            
        }

        //Print Error Message instance
        System.err.println(errorReportStatic.errorMessage);

        //Clear the Error Message instance
        errorReportStatic = null;

    }

    public static void print_error_message_stack() {

        System.out.println("DISPLAYING *ALL* ERROR MESSAGES:\n");

        if (errorStack.isEmpty()) {
            System.out.println("\t...Found no errors to display");
            return;
        }

        int itr = 0;
        while (true) {

            //Stack is empty, break
            if (errorStack.isEmpty())
                break;

            ErrorReport reportCur = errorStack.pop();
            System.out.println("\t"+itr+":");

            //Print the error message
            errorReportStatic = reportCur;
            print_error_message();

            itr++;

        }

    }


    public static void makeError(Class<? extends ErrorReport> errorClassType, String errorMessage, String errorLocation) {
        
        try {

            //Fetch the constructor for whatever ErrorReport class was passed in
            Constructor<? extends ErrorReport> constructor = errorClassType.getConstructor(String.class, String.class);
            
            //Create a new instance using the constructor
            ErrorReport errorReport = constructor.newInstance(errorMessage, errorLocation);

            //Only record the first/lowest level error
            if (errorReportStatic == null)
                errorReportStatic = errorReport;

        }
        
        //Failed to create ErrorReport instance
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("PROGRAM ERROR -- Failed to create ErrorReport instance!");
            e.printStackTrace();
        }

    }

    public static void makeError(Class<? extends ErrorReport> errorClassType, String errorMessage, Token tokenTarget) {

        /*
            Automatically pulls the filename and line number from the token

            If the token is null, the error location will be set to an unknown location (e.g. syntax error in 'singleExpr.jott')
        */

        String errorLocation;

        //Passed null token target
        if (tokenTarget == null) {
            errorLocation = "(Unknown Error Location -- Supplied Token target is null)";
        }

        //Token target is not null
        else {
            errorLocation = (tokenTarget.getFilename() + ":"+ tokenTarget.getLineNum());
            errorMessage += " -- at token '" + tokenTarget.getToken() + "'";
        }
        
        makeError(errorClassType, errorMessage, errorLocation);

    }

}
