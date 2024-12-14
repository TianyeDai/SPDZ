/*
 * Class that we use to keeps all values within a MOD.
 */
import java.math.BigInteger;

public class FieldValue {
    private final BigInteger value;
    private final BigInteger MOD;

    public FieldValue(BigInteger value, BigInteger MOD){
        this.value = value;
        this.MOD = MOD;
    }

    public BigInteger getValue(){
        return value;
    }

    public BigInteger getMOD(){
        return MOD;
    }

    public FieldValue add(FieldValue value){
        BigInteger sum = this.value.add(value.getValue()).mod(MOD);
        return new FieldValue(sum, MOD);
    }

    public FieldValue subtract(FieldValue value){
        BigInteger dif = this.value.subtract(value.getValue()).mod(MOD);
        if (dif.compareTo(BigInteger.ZERO) < 0){
            dif = dif.add(MOD);
        }
        return new FieldValue(dif, MOD);
    }

    public FieldValue multiply(FieldValue value){
        BigInteger product = this.value.multiply(value.getValue()).mod(MOD);
        return new FieldValue(product, MOD);
    }

    public FieldValue divide(BigInteger divisor) {
        BigInteger inverse = divisor.modInverse(MOD);
        BigInteger result = this.value.multiply(inverse).mod(MOD);
        return new FieldValue(result, MOD);
    }
}
