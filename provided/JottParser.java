package provided;

/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author Adam Dernis
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSemantic;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.FunctionSymbolTable;
import Interpreter.ProgramTree.ProgramSymbolTable;
import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.ProgramNode;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.StringNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FuncCallParamsNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionCallNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionRefNode;
import Interpreter.ProgramTree.Nodes.FunctionNodes.FunctionDefNode;
import Interpreter.ProgramTree.Nodes.FunctionNodes.ParametersDefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.AssignmentNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.ReturnStatementNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;
import Interpreter.ProgramTree.FunctionSymbolTable;

public class JottParser {

    private static final Set<String> KeywordSet = Set.of(
        "Def", "Return",
        "If", "ElseIf", "Else", "While",
        "Integer", "Double", "String", "Boolean", "Void");
    private static final Set<String> keywordSetLower = new HashSet<String>();
    static {
        for (String keyword : KeywordSet) {
            keywordSetLower.add(keyword.toLowerCase());
        }
    }

    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * 
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens) {
        SplitIdsandKeys(tokens);

        ProgramNode output = ProgramNode.parseProgram(new TokenStack(tokens));

        //Check if program contains a main function
        if ((output != null) && !FunctionSymbolTable.programContainsMain()) {
            ErrorReport.makeError(ErrorReportSemantic.class, "Program does not contain a main function", "");
            output = null;
        }

        //Output is null, display an error message
        if (output == null)
            ErrorReport.print_error_message();

        //Clear last token recorded
        TokenStack.clear_last_token_popped();

        //Clear Symbol Maps
        FunctionSymbolTable.clearTable();
        ProgramSymbolTable.clearTable();

        return output;
    }

    private static void SplitIdsandKeys(ArrayList<Token> tokens){
        for (Token token : tokens) {
            if (token.getTokenType() != TokenType.ID_KEYWORD) {
                continue;
            }

            // Ideally, an uppercase check could be added here too, however that would be handling a phase 3 validation problem in phase 2. Alas.
            TokenType updatedType = KeywordSet.contains(token.getToken()) ? TokenType.KEYWORD : TokenType.ID;
            token.updateTokenType(updatedType);
        }
    }

    public static boolean isKeyword(String token) {
        return KeywordSet.contains(token) || keywordSetLower.contains(token);
    }

}
