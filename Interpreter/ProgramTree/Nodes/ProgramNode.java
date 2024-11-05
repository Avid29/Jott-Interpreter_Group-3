package Interpreter.ProgramTree.Nodes;

import java.util.ArrayList;
import java.util.HashMap;

import Interpreter.Parsing.TokenStack;
import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.FunctionNodes.FunctionDefNode;
import provided.Token;

public class ProgramNode extends NodeBase<ProgramNode> {
    private ArrayList<FunctionDefNode> funcNodes;
    private HashMap<String, ArrayList<Token>> funcMap;

    public ProgramNode(){
        this(new ArrayList<>());
    }

    public ProgramNode(ArrayList<FunctionDefNode> funcNodes){
        this.funcNodes = funcNodes;
        this.funcMap = new HashMap<>();
        for (FunctionDefNode funcDefNode : funcNodes) {
            funcMap.put(funcDefNode.getName().getToken(), null);
        }
    }

    public static ProgramNode parseProgram(TokenStack tokens) {
        tokens.pushStack();
        ArrayList<FunctionDefNode> funcNodes = new ArrayList<>();

        FunctionDefNode node = null;
        Token peek = tokens.peekToken();
        while (peek != null && peek.getToken().equals("Def")) {
            node = FunctionDefNode.parseNode(tokens);
            if (node == null) {
                tokens.popStack(true);
                return null;
            }

            funcNodes.add(node);
            peek = tokens.peekToken();
        }

        if (!tokens.isEmpty())
        {
            tokens.popStack(true);
            return null;
        }

        tokens.popStack(false);
        return new ProgramNode(funcNodes);
    }

    @Override
    public String convertToJott() {
        String output = "";
        for (FunctionDefNode node : funcNodes) {
            output += node.convertToJott() + "\n"; 
        }
        
        return output;
    }
}
