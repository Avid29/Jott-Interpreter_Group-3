package Interpreter;

public class Tuple<T1, T2> {

    // Public fields is bad design, but I can't be bothered if I can't do properties.
    // Tuples should just be a thing in the first place. God I hate this language.

    public T1 Item1;
    public T2 Item2;

    public Tuple(T1 item1, T2 item2){
        Item1 = item1;
        Item2 = item2;
    }
}
