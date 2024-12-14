/*
 * A user-friendly class to set computation nodes & position map for each party.
 */
public class TreeManager {
   private int partyID;
   private int numOfNodes; //number of computation node; not including the inputs
   private int numOfSecrets;
   private int numOfParty;
   private Node[] computationNodes;
   private Node[] leftChilds;
   private Node[] rightChilds;
   private Node[] inputs;

   public TreeManager(int partyID, int numOfNodes, int numOfSecrets, int numOfParty){
        this.partyID = partyID;
        this.numOfNodes = numOfNodes;
        this.numOfSecrets = numOfSecrets;
        this.numOfParty = numOfParty;
        computationNodes = new Node[this.numOfNodes]; //initialize the array that holds all the computation nodes
        inputs = new Node[this.numOfSecrets];
        setInputNode();
        leftChilds = new Node[this.numOfNodes]; //num of leftchilds must be smaller than num of node
        rightChilds = new Node[this.numOfNodes];
   }

   public void setAdderNode(int nodeID){
        Node adder = new Adder(nodeID, partyID);
        computationNodes[nodeID] = adder;
   }

   public void setMultNode(int nodeID){
        ShareManager manager = new ShareManager(this.partyID);
        BeaverTriple myTripleShare = manager.getTripleShare();
        FieldValue myKeyShare = manager.getKeyShare();

        Node multiplier = new Multiplier(numOfParty, myTripleShare, nodeID, this.partyID, myKeyShare);
        computationNodes[nodeID] = multiplier;
   }

   public void setInputNode(){ //done by constructor
        ShareManager manager = new ShareManager(this.partyID);
        for (int i = 0; i < numOfSecrets; i++){
            int secretID = i + 1;
            Share secretShare = manager.getSecretShare(secretID);
            Input input = new Input(secretShare);
            inputs[i] = input;
        }
   }

   public void setLeftChild(int parentID, int childID){
        //we assume that the index of the inputs is right after the computation nodes. So if the largest computation
        //node ID is 10, then the inputID will be 11, 12, 13, etc.
        if (childID >= numOfNodes){ //this is a input node
            leftChilds[parentID] = inputs[childID - numOfNodes];
        }
        else{ //this is a computation node
            leftChilds[parentID] = computationNodes[childID];
        }
   }

   public void setRightChild(int parentID, int childID){
        if (childID >= numOfNodes){ //if true, this is a input.
            rightChilds[parentID] = inputs[childID - numOfNodes];
        }
        else{ //else, this is a computation node.
            rightChilds[parentID] = computationNodes[childID];
        }
    }

    //get functions(for now)
    public Node[] getComputationNodes(){
        return computationNodes;
    }

    public Node[] getLeftChilds(){
        return leftChilds;
    }

    public Node[] getRightChilds(){
        return rightChilds;
    }

    public void imFinished(){ //construct tree using the three array
        //access position map
        PositionMap positionMap = PositionMap.GlobalMap(0, 0); //singleton
        for (int i = 0; i < this.numOfNodes; i++){
            //put this node in position map
            positionMap.putNode(i, this.partyID, computationNodes[i]);
            //set children
            computationNodes[i].setLeftChild(leftChilds[i]);
            computationNodes[i].setRightChild(rightChilds[i]);
        }
    }
}
