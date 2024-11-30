
import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ProgramTree.ProgramSymbolTable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import provided.JottParser;
import provided.JottTokenizer;
import provided.JottTree;
import provided.Token;

public class Jott {
    private static final boolean DEBUGGING = false;

    private static void debugln() {
        if (!DEBUGGING)
            return;

        System.out.println();
    }
    
    private static void debugln(String str) {
        if (!DEBUGGING)
            return;

        System.out.println(str);
    }

    //Main
    public static void main(String[] args) {
    

        //Fetch name of provided Jott file from arguments
        debugln();
        debugln("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        debugln();

        String inputFile = args[0];
        debugln("Input file: " + inputFile);

        debugln();
        debugln("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        debugln();


        //Attempt the orginal Jott code from the provided file path
        String orginalJottCode;
        try {
            orginalJottCode = new String(Files.readAllBytes(Paths.get(inputFile)));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        debugln("Orginal Jott Code:\n");
        debugln(orginalJottCode);
        
        debugln();
        debugln("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        debugln();


        //Tokenize the input
        ArrayList<Token> tokens = JottTokenizer.tokenize(inputFile);


        //Parse the tokens
        JottTree root = JottParser.parse(tokens);


        debugln("Parse Status:");

        if (root == null) {
            debugln();
            debugln("(X) Failed to parse Jott code tokens, exiting program...");
            debugln();

            ProgramSymbolTable.printWholeTable();

            return;
        }
        else {
            debugln();
            debugln("(+) Successfully parsed Jott code tokens, proceeding...");
            debugln();
        }

        debugln();
        debugln("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        debugln();


        //Convert the parsed tree to Jott code
        String jottCode = root.convertToJott();

        debugln("Resulting Jott Code:\n");
        debugln(jottCode);

    
        debugln();
        debugln("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        debugln();


        //Execute the parsed tree
        //System.out.println("[EX]  Root Class: " + root.getClass().getSimpleName());
        
        try {
            root.execute();
            debugln();
            debugln("(+) Successfully executed Jott code, exiting program...");
            debugln();
        }
        catch (Exception e) {

            System.out.println();
            System.out.println("(X) Failed to execute Jott code, aborting program...");
            System.out.println();

            ErrorReport.print_error_message_stack();

            System.out.println();
            System.out.println("\tJava Exception:");
            System.out.println();

            e.printStackTrace();

        }

        /*

            FOR DEBUGGING:
            Display the final symbol (variables/parameters) table

            //  System.out.println();
            //  ProgramSymbolTable.printWholeTable();

        */

    }

}
