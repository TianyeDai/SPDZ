/*
 * Generate a random beaver triple.
 */
import java.math.BigInteger;

public class TripleGenerator {
    private final FieldValue MACKey;
    private final BigInteger MOD;

    public TripleGenerator(BigInteger MOD, FieldValue MACKey){
        this.MACKey = MACKey;
        this.MOD = MOD;
    }

    public FieldValue getMACKey(){
        return MACKey;
    }

    public BeaverTriple getRandomTriple(){
        //get two random FieldValue
        ShareGenerator generator = new ShareGenerator(MOD, 0);
        FieldValue value1 = generator.generateRandomValue();
        FieldValue value2 = generator.generateRandomValue();

        Share valueA = new Share(value1, value1.multiply(MACKey));
        Share valueB = new Share(value2, value2.multiply(MACKey));

        Share valueC = new Share(value1.multiply(value2), value1.multiply(value2).multiply(MACKey));

        BeaverTriple result = new BeaverTriple(valueA, valueB, valueC);

        return result;
    }
}
