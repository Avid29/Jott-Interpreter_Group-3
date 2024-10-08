package Interpreter.ProgramTree.Nodes.StatementNodes.Abstract;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.AssignmentNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.FuncCallStatement;
import Interpreter.ProgramTree.Nodes.StatementNodes.ReturnStatementNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.IfBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.WhileBlockNode;

public abstract class BodyStatementNodeBase extends NodeBase<BodyStatementNodeBase> {
    public static BodyStatementNodeBase parseNode(TokenStack tokens) {
        var token = tokens.peekToken();

        if (token == null) {
            return null;
        }

        BodyStatementNodeBase node = switch (token.getToken()) {
            case "If" -> IfBlockNode.parseNode(tokens);
            case "While" -> WhileBlockNode.parseNode(tokens);
            case "::" -> FuncCallStatement.parseNode(tokens);
            case "Returns" -> ReturnStatementNode.parseNode(tokens);
            default -> AssignmentNode.parseNode(tokens);
        };

        if (node == null) {
            // Unrecognizable statement
            return null;
        }

        return node;
    }
}
