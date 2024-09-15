package provided;

/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author Adam Dernis
 */

import java.util.ArrayList;
import java.util.Map;

import Interpreter.Parsing.JottTreeBuilder;
import Interpreter.Parsing.ParserStateHandlerBase;
import Interpreter.Parsing.TokenStack;
import Interpreter.Parsing.Handlers.LogErrorHandler;
import Interpreter.Parsing.Handlers.NodeCreation.NodeCreationHandler;
import Interpreter.Parsing.Handlers.NodeCreation.TypeNodeCreationHandler;
import Interpreter.ProgramTree.Nodes.ProgramNode;
import Interpreter.ProgramTree.Nodes.FunctionNodes.FunctionNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.ReturnStatementNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.ElseBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.ElseIfBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.IfBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.WhileBlockNode;

public class JottParser {
    private static final Map<String, ParserStateHandlerBase> KeywordBehaviorMap = Map.ofEntries(
        Map.entry("Def", new NodeCreationHandler<>(FunctionNode::new)),
        Map.entry("Return", new NodeCreationHandler<>(ReturnStatementNode::new)),
        Map.entry("If", new NodeCreationHandler<>(IfBlockNode::new)),
        Map.entry("ElseIf", new NodeCreationHandler<>(ElseIfBlockNode::new)),
        Map.entry("Else", new NodeCreationHandler<>(ElseBlockNode::new)),
        Map.entry("While", new NodeCreationHandler<>(WhileBlockNode::new)),
        Map.entry("Integer", new TypeNodeCreationHandler()),
        Map.entry("Double", new TypeNodeCreationHandler()),
        Map.entry("String", new TypeNodeCreationHandler()),
        Map.entry("Boolean", new TypeNodeCreationHandler()),
        Map.entry("Void", new TypeNodeCreationHandler()));

    private static final Map<TokenType, ParserStateHandlerBase> TokenBehaviorMap = Map.ofEntries(
        Map.entry(TokenType.COMMA, new LogErrorHandler()),
        Map.entry(TokenType.R_BRACKET, new LogErrorHandler()),
        Map.entry(TokenType.L_BRACKET, new LogErrorHandler()),
        Map.entry(TokenType.R_BRACE, new LogErrorHandler()),
        Map.entry(TokenType.L_BRACE, new LogErrorHandler()),
        Map.entry(TokenType.ASSIGN, new LogErrorHandler()),
        Map.entry(TokenType.REL_OP, new LogErrorHandler()),
        Map.entry(TokenType.MATH_OP, new LogErrorHandler()),
        Map.entry(TokenType.SEMICOLON, new LogErrorHandler()),
        Map.entry(TokenType.NUMBER, new LogErrorHandler()),
        Map.entry(TokenType.ID_KEYWORD, new LogErrorHandler()),
        Map.entry(TokenType.COLON, new LogErrorHandler()),
        Map.entry(TokenType.STRING, new LogErrorHandler()),
        Map.entry(TokenType.FC_HEADER, new LogErrorHandler()),
        Map.entry(TokenType.ID, new LogErrorHandler()),
        Map.entry(TokenType.KEYWORD, new LogErrorHandler()));

    private TokenStack tokens;
    private JottTreeBuilder builder;

    public JottParser(TokenStack tokens) {
        this.tokens = tokens;
        builder = new JottTreeBuilder();
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
        // return parser.parseProgram();

        return parser.parse();
    }

    private static void SplitIdsandKeys(ArrayList<Token> tokens){
        for (Token token : tokens) {
            if (token.getTokenType() != TokenType.ID_KEYWORD) {
                continue;
            }

            // Ideally, an uppercase check could be added here too, however that would be handling a phase 3 validation problem in phase 2. Alas.
            TokenType updatedType = KeywordBehaviorMap.containsKey(token.getToken()) ? TokenType.KEYWORD : TokenType.ID;
            token.updateTokenType(updatedType);
        }
    }

    public ProgramNode parse() {

        while (!tokens.isEmpty()) {
            var token = tokens.popToken();

            if (token.getTokenType() == TokenType.KEYWORD) {
                var handler = KeywordBehaviorMap.get(token.getToken());
                handler.apply(builder, token, tokens);
            } else {
                var handler = TokenBehaviorMap.get(token.getTokenType());
                handler.apply(builder, token, tokens);
            }
        }

        return builder.getTree();
    }

    /*
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

        BodyNode fBody = parseBody();

        tokens.popStack(false);
        return new FunctionNode(fIdentifier, paramsNode, fBody);
    }

    public ParametersDefNode parseParamsDef() {
        tokens.pushStack();

        ArrayList<VariableDeclarationNode> paramNodes = new ArrayList<>();

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

            paramNodes.add(new VariableDeclarationNode(pops.getLast(), pops.getFirst()));
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
                new TokenType[] { TokenType.ID_KEYWORD, TokenType.ID_KEYWORD, TokenType.SEMICOLON }, popped);

        if (errorCode != -1) {
            // No need to write an error message because the line still may be valid. We
            // just need to backtrack and pass it along to the function body.
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new VariableDeclarationNode(popped.get(0), popped.get(1));
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
        int errorCode = tokens.tokenSequenceMatch(
                new TokenType[] { TokenType.FC_HEADER, TokenType.ID_KEYWORD, TokenType.L_BRACKET }, pops);

        if (errorCode != -1) {
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
 */
}