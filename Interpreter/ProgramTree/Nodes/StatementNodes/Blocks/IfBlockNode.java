package Interpreter.ProgramTree.Nodes.StatementNodes.Blocks;

import java.util.ArrayList;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.StatementNodes.Abstract.BlockDeclareNodeBase;
import provided.Token;
import provided.TokenType;

public class IfBlockNode extends BlockDeclareNodeBase {
    private ArrayList<ElseIfBlockNode> elseIfBlocks;
    private ElseBlockNode elseBlock;

    public static IfBlockNode parseNode(TokenStack tokens) {
        tokens.pushStack();

        ArrayList<Token> popped = new ArrayList<>();
        int errorCode = tokens.tokenSequenceMatch(new TokenType[]{TokenType.KEYWORD, TokenType.L_BRACKET}, popped);

        if (errorCode != -1) {
            // Missing conditional expression
            tokens.popStack(true);
            return null;
        }

        if (!popped.get(0).getToken().equals("If")) {
            // https://youtu.be/cYMfJUMsLj4
            tokens.popStack(true);
            return null;
        }

        

        tokens.popStack(false);
        return null;
    }
}
