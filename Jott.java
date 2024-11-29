
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
 
    //Main
    public static void main(String[] args) {
    

        //Fetch name of provided Jott file from arguments
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println();

        String inputFile = args[0];
        System.out.println("Input file: " + inputFile);

        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println();


        //Attempt the orginal Jott code from the provided file path
        String orginalJottCode;
        try {
            orginalJottCode = new String(Files.readAllBytes(Paths.get(inputFile)));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Orginal Jott Code:\n");
        System.out.println(orginalJottCode);
        
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println();


        //Tokenize the input
        JottTokenizer tokenizer = new JottTokenizer(inputFile);
        ArrayList<Token> tokens = tokenizer.tokenize(inputFile);


        //Parse the tokens
        JottTree root = JottParser.parse(tokens);


        System.out.println("Parse Status:");

        if (root == null) {
            System.out.println();
            System.out.println("(X) Failed to parse Jott code tokens, exiting program...");
            System.out.println();

            ProgramSymbolTable.printWholeTable();

            return;
        }
        else {
            System.out.println();
            System.out.println("(+) Successfully parsed Jott code tokens, proceeding...");
            System.out.println();
        }

        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println();


        //Convert the parsed tree to Jott code
        String jottCode = root.convertToJott();

        System.out.println("Resulting Jott Code:\n");
        System.out.println(jottCode);

    
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println();


        //Execute the parsed tree
        //System.out.println("[EX]  Root Class: " + root.getClass().getSimpleName());
        
        try {
            root.execute();
            System.out.println();
            System.out.println("(+) Successfully executed Jott code, exiting program...");
            System.out.println();
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
