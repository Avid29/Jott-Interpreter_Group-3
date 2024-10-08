package Interpreter.ProgramTree.Nodes.Abstract;

import provided.JottTree;

public abstract class NodeBase<TSelf extends NodeBase<TSelf>> implements JottTree {

// NOTE:
// If generics were implemented properly it would be possible to actually make use of CRTP.
// Should I get bored and return to this code in an era where Oracle gets their act together
// (a hypothetical near certain to remain that way), the following method should be added:
//
//  public static abstract TSelf parseNode(TokenStack tokens); 
//
// Each child type would be declared to override this method, but as is this is not possible because
// type of TSelf is an instance variable, and cannot effect a static method. This pattern would also
// cascade down the the child abstract types, but currently that is not possible. As usual, praise be C#
// and the introduction of Generic Math in .NET 7.
//
// P.S. Professor Johnson, please let me know how what Professor Brown has to say about this.  

    @Override
    public String convertToJott() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'convertToJott'");
    }

    @Override
    public boolean validateTree() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateTree'");
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
}
