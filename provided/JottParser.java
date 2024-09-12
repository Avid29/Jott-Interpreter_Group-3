package provided;

/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author Adam Dernis
 */

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.ProgramNode;
import Interpreter.ProgramTree.ExpressionNodes.StringNode;
import Interpreter.ProgramTree.FunctionNodes.BodyNode;
import Interpreter.ProgramTree.FunctionNodes.FunctionBodyNode;
import Interpreter.ProgramTree.FunctionNodes.FunctionNode;
import Interpreter.ProgramTree.FunctionNodes.ParameterDefNode;
import Interpreter.ProgramTree.FunctionNodes.ParametersDefNode;
import Interpreter.ProgramTree.StatementNodes.AssignmentNode;
import Interpreter.ProgramTree.StatementNodes.ExpressionNode;
import Interpreter.ProgramTree.StatementNodes.FunctionCallNode;
import Interpreter.ProgramTree.StatementNodes.VariableDefinitionNode;

public class JottParser {
    private TokenStack tokens;

    public JottParser(TokenStack tokens) {
        this.tokens = tokens;
    }

    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * 
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens) {
        JottParser parser = new JottParser(new TokenStack(tokens));
        return parser.parseProgram();
    }

    public ProgramNode parseProgram() {
        tokens.pushStack();
        ArrayList<FunctionNode> funcNodes = new ArrayList<>();

        FunctionNode node = null;
        while (true) {
            node = parseFunc();
            if (node == null) {
                break;
            }

            funcNodes.add(node);
        }

        tokens.popStack(false);
        return new ProgramNode(funcNodes);
    }

    public FunctionNode parseFunc() {
        tokens.pushStack();

        // Perform mential tokenType checks on function id/definition.
        ArrayList<Token> pops = new ArrayList<>();

        // TODO: Replace with error objects
        int errorCode = tokens.tokenSequenceMatch(
                new TokenType[] { TokenType.ID_KEYWORD, TokenType.ID_KEYWORD, TokenType.L_BRACKET }, pops);
        String error = switch (errorCode) {
            case -1 -> null;
            case 0 -> "Expected \"Def\" keyword";
            case 1 -> "Expected function identifier";
            case 2 -> "Expected function parameters";
            default -> "Unknown error";
        };

        if (error != null) {
            tokens.popStack(true);
            return null;
        }

        // TODO: Process identifier from popped tokens
        if (pops.get(0).getToken() != "Def") {
            tokens.popStack(true);
            return null;
        }

        Token fIdentifier = pops.get(1);

        // Parse parameters
        ParametersDefNode paramsNode = parseParamsDef();

        // TODO: Replace with error objects
        pops = new ArrayList<>();
        errorCode = tokens
                .tokenSequenceMatch(new TokenType[] { TokenType.COLON, TokenType.ID_KEYWORD, TokenType.L_BRACE }, pops);
        error = switch (errorCode) {
            case -1 -> null;
            case 0 -> "Expected ':' keyword";
            case 1 -> "Expected function return type";
            case 2 -> "'{'";
            default -> "Unknown error";
        };

        if (error != null) {
            tokens.popStack(true);
            return null;
        }

        FunctionBodyNode fBody = parseFuncBody();

        tokens.popStack(false);
        return new FunctionNode(fIdentifier, paramsNode, fBody);
    }

    public ParametersDefNode parseParamsDef() {
        tokens.pushStack();

        ArrayList<ParameterDefNode> paramNodes = new ArrayList<>();

        Token curr = tokens.popToken();
        while (curr != null && curr.getTokenType() == TokenType.COMMA) {
            ArrayList<Token> pops = new ArrayList<>();
            int errorCode = tokens.tokenSequenceMatch(
                    new TokenType[] { TokenType.ID_KEYWORD, TokenType.SEMICOLON, TokenType.ID_KEYWORD }, pops);

            // TODO: Replace with error objects
            String error = switch (errorCode) {
                case -1 -> null;
                case 0 -> "Expected parameter identifier";
                case 1 -> "Expected ':";
                case 2 -> "Expected parameter type";
                default -> "Unknown error";
            };

            if (error != null) {
                tokens.popStack(true);
                return null;
            }

            paramNodes.add(new ParameterDefNode(pops.getFirst(), pops.getLast()));
            curr = tokens.popToken();
        }

        if (curr.getTokenType() != TokenType.R_BRACKET) {
            // Error: Expected parameters closing brosing.
            // TODO: Log error
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new ParametersDefNode(paramNodes);
    }

    public FunctionBodyNode parseFuncBody() {
        tokens.pushStack();

        // Parse the variable definitions at the top of the function body.
        VariableDefinitionNode varDefNode = null;
        do {
            varDefNode = parseVarDef();
        } while (varDefNode != null);

        // Parse the remainder of the function body.

        tokens.popStack(false);
        return null;
    }

    public VariableDefinitionNode parseVarDef() {
        tokens.pushStack();

        ArrayList<Token> popped = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(
                new TokenType[] { TokenType.ID_KEYWORD, TokenType.ID_KEYWORD, TokenType.SEMICOLON }, popped);

        if (errorCode != -1) {
            // No need to write an error message because the line still may be valid. We
            // just need to backtrack and pass it along to the function body.
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new VariableDefinitionNode(popped.get(0), popped.get(1));
    }

    public BodyNode parseBody() {
        tokens.pushStack();

        tokens.popStack(false);
        return null;
    }

    public JottTree parseBodyStatement() {
        var token = tokens.peekToken();

        if (token == null) {
            return null;
        }

        JottTree node = switch (token.getToken()) {
            case "If" -> parseSubBlock(false);
            case "While" -> parseSubBlock(true);
            case "::" -> parseFuncCall();
            default -> parseAssign();
        };

        if (node == null) {
            // Unrecognizable statement
            return null; 
        }

        return node;
    }

    public JottTree parseSubBlock(boolean isWhile) {
        tokens.pushStack();

        // We don't need to check these tokens' details. The first is guarenteed to be
        // an If/While keyword, the second is either an '[' or we're missing the
        // conditional expression. As a result we can pass a null popped array list.
        int errorCode = tokens.tokenSequenceMatch(new TokenType[] { TokenType.ID_KEYWORD, TokenType.L_BRACKET }, null);

        if (errorCode != -1) {
            // Missing conditional expression
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return null;
    }

    public FunctionCallNode parseFuncCall() {
        tokens.pushStack();

        ArrayList<Token> pops = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(new TokenType[] {TokenType.FC_HEADER, TokenType.ID_KEYWORD, TokenType.L_BRACKET}, pops);

        if (errorCode != -1)
        {
            // Malformed function call
            tokens.popStack(true);
            return null;
        }


        tokens.popStack(false);
        return null;
    }
    
    public AssignmentNode parseAssign() {
        tokens.pushStack();

        ArrayList<Token> popped = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(new TokenType[] { TokenType.ID_KEYWORD, TokenType.ASSIGN }, popped);

        if (errorCode != -1) {
            // Let the parent function handle reporting this error.
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return null;
    }

    public JottTree parseExpr() {
        tokens.pushStack();

        Token next = tokens.popToken();

        // Handle string literal
        if (next.getTokenType() == TokenType.STRING) {
            tokens.popStack(false);
            return new StringNode(next);
        }

        tokens.popStack(false);
        return null;
    }
}
