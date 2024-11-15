package Interpreter.ProgramTree.Nodes.FunctionNodes;

import java.util.ArrayList;

import ErrorReporting.ErrorReport;
import ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.TypeNode;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.VarRefNode;
import Interpreter.ProgramTree.Nodes.StatementNodes.VariableDeclarationNode;
import provided.Token;
import provided.TokenType;

public class ParametersDefNode extends NodeBase<ParametersDefNode> {
    private ArrayList<VariableDeclarationNode> paramNodes;

    public ParametersDefNode(ArrayList<VariableDeclarationNode> paramNodes) {
        this.paramNodes = paramNodes;
    }

    public static ParametersDefNode parseNode(TokenStack tokens) {

        tokens.pushStack();

        ArrayList<VariableDeclarationNode> paramNodes = new ArrayList<>();

        Token curr = tokens.peekToken();
        if (curr.getTokenType() == TokenType.R_BRACKET) {
            tokens.popToken();
            return new ParametersDefNode(paramNodes);
        }
        
        do {

            ArrayList<Token> pops = new ArrayList<>();
            int errorCode = tokens.tokenSequenceMatch(
                new TokenType[] { TokenType.ID, TokenType.COLON, TokenType.KEYWORD },
                pops
            );

            String errorMessage = switch (errorCode) {
                case -1 -> null;
                case 0 -> "Expected parameter identifier";
                case 1 -> "Expected ':'";
                case 2 -> "Expected parameter type";
                default -> "Unknown ParametersDefNode error";
            };

            if (errorMessage != null) {

                ErrorReport.makeError(ErrorReportSyntax.class, "ParametersDefNode -- "+errorMessage, TokenStack.get_last_token_popped());

                tokens.popStack(true);
                return null;

            }

            //Get the type, ensure it is not Void
            TypeNode type = new TypeNode(pops.getLast());
            if (type.getType().getToken().equals("Void")) {
                
                ErrorReport.makeError(ErrorReportSyntax.class, "ParametersDefNode -- Cannot declare a parameter of type 'Void'", TokenStack.get_last_token_popped());

                tokens.popStack(true);
                return null;
                
            }    

            VarRefNode name = new VarRefNode(pops.getFirst());

            paramNodes.add(new VariableDeclarationNode(type, name, true));
            curr = tokens.popToken();

        } while (curr != null && curr.getTokenType() == TokenType.COMMA);

        if (curr.getTokenType() != TokenType.R_BRACKET) {

            ErrorReport.makeError(ErrorReportSyntax.class, "ParametersDefNode -- Expected Right Bracket ']'", TokenStack.get_last_token_popped());

            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new ParametersDefNode(paramNodes);
    }

    @Override
    public String convertToJott() {
        String output = "";
        for (int i = 0; i < paramNodes.size(); i++) {
            if (i != 0){
                output += ",";
            }

            output += paramNodes.get(i).convertToJott(true); 
        }
        return output;
    }
}
