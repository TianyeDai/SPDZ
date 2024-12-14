/*
 * BeaverTriple. c = a * b.
 */
public class BeaverTriple {
    private final Share a;
    private final Share b;
    private final Share c;

    public BeaverTriple(Share a, Share b, Share c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Share geta(){
        return a;
    }

    public Share getb(){
        return b;
    }

    public Share getc(){
        return c;
    }
}
