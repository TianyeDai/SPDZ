import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/*
 * Share secrets and MACKey, and store them in a static map for later access.
 */
public class ShareManager {
    public int numOfParty;
    public int numOfNode;
    private int partyID;
    public BigInteger MOD;
    private Share[] secrets; //an array of secrets eg: [X, Y, Z]
    private FieldValue MACKey;
    private BeaverTriple beaverTriple;
    private static Map<String, Share[]> shareMap = new HashMap<>(); //a global map that stores the shares

    public ShareManager(Share[] secrets, FieldValue MACKey, int numOfParty, int numOfNode, BigInteger MOD){
        this.partyID = -1;
        this.secrets = secrets;
        this.MOD = MOD;
        this.MACKey = MACKey;
        this.numOfParty = numOfParty;
        this.numOfNode = numOfNode;
        TripleGenerator generator = new TripleGenerator(MOD, this.MACKey);
        this.beaverTriple = generator.getRandomTriple();

        PositionMap positionMap = PositionMap.GlobalMap(numOfParty, numOfNode); //intialize global map that stores nodes

        ShareGenerator shareGenerator = new ShareGenerator(MOD, numOfParty);

        for (int i = 0; i < this.secrets.length; i++){
            String secretName = "Secret" + String.valueOf(i+1);
            Share[] sharedArray = shareGenerator.shareSecret(secrets[i]);
            shareMap.put(secretName, sharedArray);
        }

        //share MACKey
        Share holder = new Share(MACKey, MACKey); //MAC doesn't matter since Key is a FieldValue
        Share[] sharedKey = shareGenerator.shareSecret(holder);
        shareMap.put("MACKey", sharedKey);

        //share BeaverTriple
        Share[] AShare = shareGenerator.shareSecret(beaverTriple.geta());
        Share[] BShare = shareGenerator.shareSecret(beaverTriple.getb());
        Share[] CShare = shareGenerator.shareSecret(beaverTriple.getc());
        shareMap.put("a", AShare);
        shareMap.put("b", BShare);
        shareMap.put("c", CShare);
    }

    public ShareManager(int partyID){ //for other party's access to their own share
        this.partyID = partyID;
    }

    public Share getSecretShare(int secretID){
        String secretName = "Secret" + String.valueOf(secretID);

        return shareMap.get(secretName)[this.partyID];
    }

    public FieldValue getKeyShare(){
        return shareMap.get("MACKey")[this.partyID].getShareValue();
    }

    public Share getAShare(){
        return shareMap.get("a")[this.partyID];
    }

    public Share getBShare(){
        return shareMap.get("b")[this.partyID];
    }

    public Share getCShare(){
        return shareMap.get("c")[this.partyID];
    }

    public BeaverTriple getTripleShare(){
        Share a = shareMap.get("a")[this.partyID];
        Share b = shareMap.get("b")[this.partyID];
        Share c = shareMap.get("c")[this.partyID];
        BeaverTriple tripleShare = new BeaverTriple(a, b, c);
        return tripleShare;
    }
}
