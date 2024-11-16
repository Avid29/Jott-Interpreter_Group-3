package Interpreter.ProgramTree.Nodes.StatementNodes.Abstract;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.AssignmentNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.FuncCallStatementNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.ReturnStatementNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.ElseBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.ElseIfBlockNode;
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
            case "::" -> FuncCallStatementNode.parseNode(tokens);
            case "Return" -> ReturnStatementNode.parseNode(tokens);

            case "Else" -> {
                ElseBlockNode.reportNoPrecedingIf();
                yield null;
            }
            case "Elseif" -> {
                ElseIfBlockNode.reportNoPrecedingIf();
                yield null;
            }

            default -> AssignmentNode.parseNode(tokens);
        };

        if (node == null) {
            // Unrecognizable statement
            return null;
        }

        return node;
    }
}
