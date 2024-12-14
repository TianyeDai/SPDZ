/*
 * Class that defines the task of each thread. Calls the getShare() function of a root node.
 */
public class ComputeRunnable implements Runnable {
    private Node node;
    private int partyID;
    private Share[] results; //where we store the computation result

    public ComputeRunnable(Node node, int partyID, Share[] results) {
        this.node = node;
        this.partyID = partyID;
        this.results = results;
    }

    public int getPartyID(){
        return this.partyID;
    }

    public static void displayShare(Share share){
        System.out.println("Share Value: " + share.getShareValue().getValue() + "  MAC: " + share.getMAC().getValue());
    }

    public static void displayFieldValue(FieldValue value){
        System.out.println("Share Value: " + value.getValue());
    }

    @Override
    public void run() {
        // Compute product for the root node
        Share result = node.getShare();

        synchronized (results) {
            results[this.partyID] = result; // Store the result for this partyID
        }
    }
}

