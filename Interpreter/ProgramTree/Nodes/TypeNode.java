package Interpreter.ProgramTree.Nodes;

import Interpreter.ErrorReporting.ErrorReport;
import Interpreter.ErrorReporting.ErrorReportSyntax;
import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import provided.Token;
import provided.TokenType;

public class TypeNode extends NodeBase<TypeNode> {

    private final Token type;

    public TypeNode(Token type) {

        //Validate the provided token type
        if (!isValidType(type)) {

            ErrorReport.makeError(ErrorReportSyntax.class, "TypeNode -- Got invalid Type Token: " + type.getToken(), TokenStack.get_last_token_popped());

        }

        this.type = type;

    }

    public static TypeNode parseNode(TokenStack tokens) {

        Token token = tokens.popToken();

        if (token.getTokenType() != TokenType.KEYWORD) {

            ErrorReport.makeError(ErrorReportSyntax.class, "TypeNode -- Provided Token is not a KEYWORD", TokenStack.get_last_token_popped());
            return null;

        }

        if (!isValidType(token)) {

            ErrorReport.makeError(ErrorReportSyntax.class, "TypeNode -- Got invalid Type Token: " + token.getTokenType(), TokenStack.get_last_token_popped());
            return null;

        }

        return new TypeNode(token);

    }


    public Token getType() {
        return type;
    }

    public static boolean isValidType(Token type) {
        
        final Set<String> validTypes = new HashSet<>(Arrays.asList(
            "ANY",  //<-- For the print function
            "Double",
            "Integer",
            "String",
            "Boolean",
            "Void"
        ));

        return validTypes.contains(type.getToken());

    }


    
    /* JottTree Overrides */

    @Override
    public boolean validateTree() {
        return isValidType(type);
    }

    @Override
    public String convertToJott() {
        return type.getToken();
    }

}
