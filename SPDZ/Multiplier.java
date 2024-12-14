/*
 * Computation Node: Multiplier.
 */
import java.math.BigInteger;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Multiplier implements Node {
    private int numOfParty;
    private Share multiplicationResult;
    private BeaverTriple beaverTripleShare; //contains the share of a, b, and c of this specific party
    private int nodeID; //mark the position of the node in a circuit
    private int partyID; //records which party this node belongs to
    private Node leftChild;
    private Node rightChild;
    private FieldValue MACKeyShare;
    private CyclicBarrier barrier1;
    private CyclicBarrier barrier2;

    public Multiplier(int numOfParty, BeaverTriple beaverTriple, int nodeID, int partyID, FieldValue MACKeyShare){
        this.nodeID = nodeID;
        this.partyID = partyID;
        this.numOfParty = numOfParty;
        this.beaverTripleShare = beaverTriple;
        this.leftChild = null;
        this.rightChild = null;
        this.MACKeyShare = MACKeyShare;
        BarrierManager barrierManager = BarrierManager.getInstance(numOfParty);
        this.barrier1 = barrierManager.getBarrier1();
        this.barrier2 = barrierManager.getBarrier2();
    }

    public Share getd(){
        return leftChild.getShare().subtract(beaverTripleShare.geta());
    }

    public Share gete(){
        return rightChild.getShare().subtract(beaverTripleShare.getb());
    }

    //check method1
    public FieldValue getSumDMAC(FieldValue Dsum){
        return Dsum.multiply(MACKeyShare);
    }

    //check method2
    public FieldValue getSumEMAC(FieldValue Esum){
        return Esum.multiply(MACKeyShare);
    }

    //check3 (alpha_i * dsum - d_i's MAC)
    public FieldValue getMacShareCheckD(Share sumOfd){
        FieldValue sumDShare = getSumDMAC(sumOfd.getShareValue());
        return sumDShare.subtract(getd().getMAC());
    }

    //check4 (alpha_i * esum - e_i's MAC)
    public FieldValue getMacShareCheckE(Share sumOfe){
        FieldValue sumEShare = getSumDMAC(sumOfe.getShareValue());
        return sumEShare.subtract(gete().getMAC());
    }

    public int getNodeID(){
        return nodeID;
    }

    public int getPartyID(){
        return partyID;
    }

    public void setLeftChild(Node leftChild){
        this.leftChild = leftChild;
    }

    public void setRightChild(Node rightChild){
        this.rightChild = rightChild;
    }

    public Node getLeftChild(){
        return leftChild;
    }

    public Node getRightChild(){
        return rightChild;
    }

    public Share getShare(){
        //get local D and E
        Share D = getd();
        Share E = gete();
        PositionMap positionMap = PositionMap.GlobalMap(0, 0); //singleton

        //wait for other threads to reach barrier1
        try {
            barrier1.await();

        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        //after the barrier is unlocked, do the following:
        Share sumOfd = D;
        Share sumOfe = E;

        for (int party = 0; party < numOfParty; party++){
            if (party != partyID){
                //get the x-a and y-b of other parties
                Node otherPartyNode = positionMap.getNode(nodeID, party);
                sumOfd = sumOfd.add(otherPartyNode.getd());
                sumOfe = sumOfe.add(otherPartyNode.gete());
            }
        }

        //get local mac
        FieldValue MacCheckD = getMacShareCheckD(sumOfd);
        FieldValue MacCheckE = getMacShareCheckE(sumOfe);

        //wait for other threads to reach barrier2
        try {
            barrier2.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        //get the sum of D and E mac shares(should be 0)
        FieldValue macSumD = MacCheckD;
        FieldValue macSumE = MacCheckE;

        for (int party = 0; party < numOfParty; party++){
            if (party != partyID){
                Node otherPartyNode = positionMap.getNode(nodeID, party);
                macSumD = macSumD.add(otherPartyNode.getMacShareCheckD(sumOfd)); 
                //check DID NOT PASS case
                //macSumD = macSumD.add(new FieldValue(BigInteger.valueOf(5), BigInteger.valueOf(9991)));
                macSumE = macSumE.add(otherPartyNode.getMacShareCheckE(sumOfe));
            }
        }
        //see if the two checks match for D and E (exit if fail)
        if (! (macSumD.getValue().equals(BigInteger.ZERO) && macSumE.getValue().equals(BigInteger.ZERO))){
            System.out.println("DID NOT PASS MAC Check!!!!!!! Exit");
            System.exit(1);
        }

        //MAC check passed. Get multiplication result.

        Share DtimesB = sumOfd.multiply(beaverTripleShare.getb()); //(x-a)*bShare
        Share EtimesA = sumOfe.multiply(beaverTripleShare.geta()); //(y-b)*aShare
        Share DSharetimesE = D.multiply(sumOfe);

        this.multiplicationResult = beaverTripleShare.getc().add(DtimesB).add(EtimesA).add(DSharetimesE);

        return multiplicationResult;
    }
}
