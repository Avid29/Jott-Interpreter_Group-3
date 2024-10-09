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

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.BodyNode;
import Interpreter.ProgramTree.Nodes.ProgramNode;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.StringNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FuncCallParamsNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionCallNode;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall.FunctionRefNode;
import Interpreter.ProgramTree.Nodes.FunctionNodes.FunctionNode;
import Interpreter.ProgramTree.Nodes.FunctionNodes.ParametersDefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.AssignmentNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.ReturnStatementNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;

public class JottParser {
    private static final Set<String> KeywordSet = Set.of(
        "Def", "Return",
        "If", "ElseIf", "Else", "While",
        "Integer", "Double", "String", "Boolean", "Void");

    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * 
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens) {
        SplitIdsandKeys(tokens);
        return ProgramNode.parseProgram(new TokenStack(tokens));
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
}
