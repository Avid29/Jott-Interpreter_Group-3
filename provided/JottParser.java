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
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.ElseBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.ElseIfBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.IfBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.WhileBlockNode;

public class JottParser {
    private static final Set<String> KeywordSet = Set.of(
        "Def", "Return",
        "If", "ElseIf", "Else", "While",
        "Integer", "Double", "String", "Boolean", "Void");

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
        SplitIdsandKeys(tokens);
        JottParser parser = new JottParser(new TokenStack(tokens));
        return parser.parseProgram();

        // return parser.parse();
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
                new TokenType[] { TokenType.KEYWORD, TokenType.ID, TokenType.L_BRACKET }, pops);
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

        FunctionRefNode identifier = new FunctionRefNode(pops.get(1));

        // Parse parameters
        ParametersDefNode paramsNode = parseParamsDef();

        // TODO: Replace with error objects
        pops = new ArrayList<>();
        errorCode = tokens
                .tokenSequenceMatch(new TokenType[] { TokenType.COLON, TokenType.ID, TokenType.L_BRACE }, pops);
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

        BodyNode fBody = parseBody();

        tokens.popStack(false);
        return new FunctionNode(identifier, paramsNode, fBody);
    }

    public ParametersDefNode parseParamsDef() {
        tokens.pushStack();

        ArrayList<VariableDeclarationNode> paramNodes = new ArrayList<>();

        Token curr = tokens.popToken();
        while (curr != null && curr.getTokenType() == TokenType.COMMA) {
            ArrayList<Token> pops = new ArrayList<>();
            int errorCode = tokens.tokenSequenceMatch(
                    new TokenType[] { TokenType.ID, TokenType.SEMICOLON, TokenType.KEYWORD }, pops);

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

            TypeNode type = new TypeNode(pops.getLast());
            VarRefNode name = new VarRefNode(pops.getFirst());

            paramNodes.add(new VariableDeclarationNode(type, name, true));
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

    public VariableDeclarationNode parseVarDef() {
        tokens.pushStack();

        ArrayList<Token> popped = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(
                new TokenType[] { TokenType.KEYWORD, TokenType.ID, TokenType.SEMICOLON }, popped);

        if (errorCode != -1) {
            // No need to write an error message because the line still may be valid. We
            // just need to backtrack and pass it along to the function body.
            tokens.popStack(true);
            return null;
        }

        TypeNode type = new TypeNode(popped.get(0));
        VarRefNode name = new VarRefNode(popped.get(1));

        tokens.popStack(false);
        return new VariableDeclarationNode(type, name, false);
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
        int errorCode = tokens.tokenSequenceMatch(new TokenType[] { TokenType.KEYWORD, TokenType.L_BRACKET }, null);

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
        int errorCode = tokens.tokenSequenceMatch(
                new TokenType[] { TokenType.FC_HEADER, TokenType.ID, TokenType.L_BRACKET }, pops);

        if (errorCode != -1) {
            // Malformed function call
            tokens.popStack(true);
            return null;
        }

        FunctionRefNode fRefNode = new FunctionRefNode(pops.get(1));

        FuncCallParamsNode fParamsNode = null;
        // TODO: Build params node

        tokens.popStack(false);
        return new FunctionCallNode(fRefNode, fParamsNode);
    }

    public AssignmentNode parseAssign() {
        tokens.pushStack();

        ArrayList<Token> popped = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(new TokenType[] { TokenType.ID, TokenType.ASSIGN }, popped);

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
