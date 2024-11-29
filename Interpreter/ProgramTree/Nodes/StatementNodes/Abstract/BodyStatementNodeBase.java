package Interpreter.ProgramTree.Nodes.StatementNodes.Abstract;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.StatementNodes.AssignmentNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.ElseBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.ElseIfBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.IfBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.Blocks.WhileBlockNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.FuncCallStatementNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.ReturnStatementNode;

public abstract class BodyStatementNodeBase extends NodeBase<BodyStatementNodeBase> {

    public static BodyStatementNodeBase parseNode(TokenStack tokens) {

        var token = tokens.peekToken();

        if (token == null) {

            ErrorReport.makeError(ErrorReportSyntax.class, "BodyStatementNodeBase -- Peeked token is null", TokenStack.get_last_token_popped());
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
            
            ErrorReport.makeError(
                ErrorReportSyntax.class,
                "BodyStatementNodeBase -- Got unrecognizable body statement: " + token.getToken(),
                TokenStack.get_last_token_popped()
            );
            return null;

        }

        return node;
    }
}
