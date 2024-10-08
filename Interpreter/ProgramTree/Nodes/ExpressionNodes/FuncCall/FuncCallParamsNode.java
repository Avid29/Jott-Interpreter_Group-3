package Interpreter.ProgramTree.Nodes.ExpressionNodes.FuncCall;

import java.util.ArrayList;

import Interpreter.ProgramTree.Nodes.Abstract.NodeBase;
import Interpreter.ProgramTree.Nodes.ExpressionNodes.Abstract.ExpressionNodeBase;

public class FuncCallParamsNode extends NodeBase<FuncCallParamsNode> {
    private ArrayList<ExpressionNodeBase> paramNodes;
}
