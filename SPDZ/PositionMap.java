/*
 * A map that stores all party's node. Maps partyID to an array of their computation nodes.
 */
import java.util.HashMap;
import java.util.Map;

public class PositionMap {
    private static PositionMap instance = null;
    private Map<Integer, Node[]> positionMap;
    
    private PositionMap(int numOfParty, int numOfNode){
        positionMap = new HashMap<>();
        
        for (int i = 0; i < numOfNode; i++){
            Node[] partyData = new Node[numOfParty];
            positionMap.put(i, partyData);
        }
    }

    // Static method to get the singleton instance
    public static PositionMap GlobalMap(int numOfParty, int numOfNode) {
        if (instance == null) {
            instance = new PositionMap(numOfParty, numOfNode);
        }
        return instance;
    }

    //put node into map
    public void putNode(int nodeIndex, int partyIndex, Node data){
        positionMap.get(nodeIndex)[partyIndex] = data;
    }

    //return a node in positionMap
    public Node getNode(int nodeIndex, int partyIndex){
        return positionMap.get(nodeIndex)[partyIndex];
    }
}
