import java.math.BigInteger;
import java.security.SecureRandom;

/*
 * Split a secret into multiple shares. 
 */
public class ShareGenerator {
    private final BigInteger MOD;
    private final int numOfParty;

    public ShareGenerator(BigInteger MOD, int numOfParty){
        this.MOD = MOD;
        this.numOfParty = numOfParty;
    }

    public FieldValue generateRandomValue() {
        SecureRandom random = new SecureRandom();
        BigInteger randomBigInt = new BigInteger(MOD.bitLength(), random);
        return new FieldValue(randomBigInt, MOD);
    }

    //for share
    public Share[] shareSecret(Share secret){
        Share[] generatedShares = new Share[numOfParty];

        FieldValue[] fieldValueShares = shareSecret(secret.getShareValue());
        FieldValue[] MACShares = shareSecret(secret.getMAC());
        
        for (int i = 0; i < numOfParty; i++){
            generatedShares[i] = new Share(fieldValueShares[i], MACShares[i]);
        }

        return generatedShares;
    }

    //for field value
    public FieldValue[] shareSecret(FieldValue fieldValue){
        FieldValue[] shareResult = new FieldValue[this.numOfParty];

        BigInteger sum = BigInteger.ZERO;

        for (int i = 0; i < numOfParty - 1; i++){
            shareResult[i] = generateRandomValue();
            sum = sum.add(shareResult[i].getValue()).mod(MOD); // Sum modulo MOD
        }

        BigInteger lastShareValue = fieldValue.getValue().subtract(sum).mod(MOD);

        if (lastShareValue.signum() < 0) { 
            lastShareValue = lastShareValue.add(MOD); // Ensure non-negative result
        }
        shareResult[this.numOfParty - 1] = new FieldValue(lastShareValue, MOD);
        return shareResult;
    }
}
