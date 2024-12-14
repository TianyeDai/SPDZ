import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/*
 * Tester Class. 
 */
public class Test {
    public static void main(String[] args) {
        //initialize test data
        BigInteger MOD = BigInteger.valueOf(9991); //a prime
        int numOfParty = 3;
        int numOfNode = 5;

        FieldValue MACKey = new FieldValue(BigInteger.valueOf(5), MOD);
        
        //Test Case is  X = 10, Y = 15, Z = 25; Compute: (x+y)*(y*z)+(x+z) = 9410;

        //initialize secrets: XYZ
        BigInteger Xv = BigInteger.valueOf(10);
        BigInteger Yv = BigInteger.valueOf(15);
        BigInteger Zv = BigInteger.valueOf(25);
        FieldValue Xf = new FieldValue(Xv, MOD);
        FieldValue Yf = new FieldValue(Yv, MOD);
        FieldValue Zf = new FieldValue(Zv, MOD);
        Share X = new Share(Xf, Xf.multiply(MACKey)); 
        Share Y = new Share(Yf, Yf.multiply(MACKey));
        Share Z = new Share(Zf, Zf.multiply(MACKey));

        int numOfSecrets = 3;

        Share[] secrets = new Share[numOfSecrets]; //put all secrets in an array
        secrets[0] = X;
        secrets[1] = Y;
        secrets[2] = Z;

        ShareManager shareManager = new ShareManager(secrets, MACKey, numOfParty, numOfNode, MOD);

        for (int partyID = 0; partyID < numOfParty; partyID++){
            TreeManager treeManager = new TreeManager(partyID, numOfNode, numOfSecrets, numOfParty);
            treeManager.setAdderNode(0); //root node's ID is 0
            treeManager.setMultNode(1);
            treeManager.setAdderNode(2);
            treeManager.setAdderNode(3);
            treeManager.setMultNode(4);

            //set child nodes
            treeManager.setLeftChild(0,1);
            treeManager.setRightChild(0,2);
            treeManager.setLeftChild(1,3);
            treeManager.setRightChild(1,4);

            //set child inputs: nodeID: X-5, Y-6, Z-7
            treeManager.setLeftChild(2,5);
            treeManager.setRightChild(2,7);
            treeManager.setLeftChild(3,5);
            treeManager.setRightChild(3,6);
            treeManager.setLeftChild(4,6);
            treeManager.setRightChild(4,7);

            treeManager.imFinished(); //finished; construct tree
        }

        Share[] results = new Share[numOfParty]; //hold result shares of all parties
        
        getComputationResults(results, numOfParty);

        shareSumCheck(results, MACKey, numOfParty, MOD);
    }

    //Call this function to do the actual computation by calling getShare() of the root nodes
    public static void getComputationResults(Share[] results, int numOfParty){
        PositionMap positionMap = PositionMap.GlobalMap(0, 0); //singleton
        List<Thread> threads = new ArrayList<>();

        for (int partyID = 0; partyID < numOfParty; partyID++){
            //get the computation node root
            Node rootNode = positionMap.getNode(0, partyID);
            Runnable task = new ComputeRunnable(rootNode, partyID, results);
            Thread thread = new Thread(task);
            threads.add(thread);
        }

        for (Thread thread: threads){
            thread.start(); //start the thread
        }
        
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //check results
    public static void shareSumCheck(Share[] shares, FieldValue MACKey, int numOfParty, BigInteger MOD){
        FieldValue zero = new FieldValue(BigInteger.ZERO, MOD);
        Share sumCheck = new Share(zero, zero);
        for (int partyID = 0; partyID < numOfParty; partyID++) {
            System.out.println("Result share of Party " + partyID + ":");
            displayShare(shares[partyID]); // Display each party's result
            sumCheck = sumCheck.add(shares[partyID]); //for reconstruction value check
        }
        System.out.println("Sum of the shares: [should be (10+15)*(15*25)+(10+25) = 9410]: ");
        displayShare(sumCheck);
    }

    public static void displayShare(Share share){
        System.out.println("Share Value: " + share.getShareValue().getValue() + "  MAC: " + share.getMAC().getValue());
        }
    
    public static void displayFieldValue(FieldValue value){
        System.out.println("Share Value: " + value.getValue());
    }
}


