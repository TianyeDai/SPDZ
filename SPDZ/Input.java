/*
 * A node that contains a secret share value. Will be the lowest nodes of the tree.
 */
public class Input implements Node {
    private Share shares;

    public Input(Share shares) {
        this.shares = shares;
    }

    public Share getShare(){
        return shares;
    }

    public void setLeftChild(Node node){
        return;
    }

    public void setRightChild(Node node){
        return;
    }

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
