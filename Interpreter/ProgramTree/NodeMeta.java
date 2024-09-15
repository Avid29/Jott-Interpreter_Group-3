package Interpreter.ProgramTree;

import java.util.EnumSet;

import Interpreter.ProgramTree.Enums.NodeType;
import Interpreter.ProgramTree.Enums.NodeTypeGroup;

public class NodeMeta {
    private NodeType type;

    public NodeMeta(NodeType type) {
        this.type = type;
    }

    public EnumSet<NodeTypeGroup> getGroups() {
        return switch (type) {
            case PROGRAM -> EnumSet.of(NodeTypeGroup.PROGRAM);
            case FUNC_DEF -> EnumSet.of(NodeTypeGroup.FUNCTION);
            case ASSIGNMENT -> EnumSet.of(NodeTypeGroup.BODY_STATEMENT);
            case BODY -> EnumSet.of(NodeTypeGroup.BLOCK);
            case BOOL -> EnumSet.of(NodeTypeGroup.EXPRESSION, NodeTypeGroup.CONDITIONAL_BLOCK);
            case ELSEIF_BLOCK -> EnumSet.of(NodeTypeGroup.BLOCK);
            case ELSE_BLOCK -> EnumSet.of(NodeTypeGroup.BLOCK);
            case FUNC_CALL -> EnumSet.of(NodeTypeGroup.OPERAND, NodeTypeGroup.BODY_STATEMENT);
            case FUNC_CALL_PARAMS -> EnumSet.of(NodeTypeGroup.EXPRESSION);
            case FUNC_DEF_PARAMS -> EnumSet.of(NodeTypeGroup.FUNCTION_DECL);
            case FUNC_DEF_PARAM -> throw new UnsupportedOperationException("Unimplemented case: " + type);
            case IF_STMT -> EnumSet.of(NodeTypeGroup.BLOCK, NodeTypeGroup.BODY_STATEMENT);
            case NUMBER -> EnumSet.of(NodeTypeGroup.OPERAND);
            case OPERATOR -> EnumSet.of(NodeTypeGroup.EXPRESSION);
            case RETURN_STATEMENT -> EnumSet.of(NodeTypeGroup.BODY_STATEMENT);
            case STRING -> EnumSet.of(NodeTypeGroup.EXPRESSION);
            case TYPE -> EnumSet.of(NodeTypeGroup.FUNCTION_DECL, NodeTypeGroup.VAR_DECL);
            case VAR_DECL -> EnumSet.of(NodeTypeGroup.FUNCTION_DECL, NodeTypeGroup.BODY_STATEMENT);
            case VAR_REF -> EnumSet.of(NodeTypeGroup.OPERAND, NodeTypeGroup.VAR_DECL, NodeTypeGroup.CONDITIONAL_BLOCK);
            case FUNC_REF -> throw new UnsupportedOperationException("Unimplemented case: " + type);
            case WHILE_LOOP -> EnumSet.of(NodeTypeGroup.BODY_STATEMENT, NodeTypeGroup.BLOCK);
        };
    }
}
