/*
 * Computation Node: Adder.
 */
public class Adder implements Node {
    private Share additionResult;
    private Node leftChild;
    private Node rightChild;
    private int nodeID;
    private int partyID;

    public Adder(int nodeID, int partyID){
        this.leftChild = null;
        this.rightChild = null;
        this.nodeID = nodeID;
        this.partyID = partyID;
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
        this.additionResult = leftChild.getShare().add(rightChild.getShare());

        // DoneMap doneMap = DoneMap.GlobalMap(PublicAccess.numOfNode);
        // doneMap.markDone(nodeID, 0);

        return additionResult;
    }

    //useless
    public Share getd(){
        return null;
    }

    public Share gete(){
        return null;
    }
    
    public FieldValue getSumDMAC(FieldValue Dsum){
        return null;
    }

    public FieldValue getSumEMAC(FieldValue Dsum){
        return null;
    }

    public FieldValue getMacShareCheckD(Share sumOfd){
        return null;
    }

    public FieldValue getMacShareCheckE(Share sumOfe){
        return null;
    }
}
