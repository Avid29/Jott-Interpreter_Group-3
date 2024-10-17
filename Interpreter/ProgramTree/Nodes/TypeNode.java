package Interpreter.ProgramTree.Nodes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import provided.Token;
import provided.TokenType;

public class TypeNode extends NodeBase<TypeNode> {
    private Token type;

    public TypeNode(Token type) {

        //Validate the provided token type
        if (!isValidType(type))
            throw new IllegalArgumentException("Invalid Type Token: " + type.getToken());

        this.type = type;

    }

    public static TypeNode parseNode(TokenStack tokens) {

        Token token = tokens.popToken();

        if (token.getTokenType() != TokenType.KEYWORD) {
            System.err.println("Provided Token is not a KEYWORD: " + token.getToken());
            return null;
        }

        if (!isValidType(token)) {
            System.err.println("Invalid Type Token: " + token.getToken());
            return null;
        }

        return new TypeNode(token);

    }


    public static boolean isValidType(Token type) {
        
        final Set<String> validTypes = new HashSet<String>(Arrays.asList(
            "Double",
            "Integer",
            "String",
            "Boolean"
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
