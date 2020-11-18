package ex0;

import java.util.Collection;
import java.util.HashMap;

public class Graph_DS implements graph {
    int numOfEdges, MC;
    HashMap<Integer, node_data> map;


    //Constructor.
    public Graph_DS(){
        this.numOfEdges=0; this.MC=0;
        this.map = new HashMap<>();
    }

    //Copy constructor.
    public Graph_DS(graph g){
        this.map = new HashMap<>();

        //copying nodes one by one, via temp variable to avoid future override.
        //the copy is without the neighbors.
        for(node_data n : g.getV()){
            NodeData temp = new NodeData((NodeData)n);
            this.addNode(temp);
        }
        //here we will (deep)copy the neighbors.
        //by establishing an independent new connection.
        for(node_data n : g.getV()){
            int key1 = n.getKey();
            for(node_data ni : n.getNi()){
                int key2 = ni.getKey();
                this.connect(key1, key2);
            }
        }
    }

    @Override
    public node_data getNode(int key) {
        return map.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        node_data src = this.map.get(node1);
        node_data dst = this.map.get(node2);

        return src.getNi().contains(dst);
    }

    @Override
    public void addNode(node_data n) {
        if(!(n == null)){
        map.put(n.getKey(), n);
        MC++;
        }
    }

    @Override
    public void connect(int node1, int node2) {
        if (node1 != node2) {
            if (map.get(node1) != null && map.get(node2) != null) {
                node_data src = this.getNode(node1);
                node_data dst = this.getNode(node2);
                if (!src.getNi().contains(dst)) {
                    src.addNi(dst);
                    dst.addNi(src);
                    numOfEdges++;
                    MC++;
                }
            }
        }
    }

    @Override
    public Collection<node_data> getV() {
        return this.map.values();
    }

    @Override
    public Collection<node_data> getV(int node_id) {
        if (this.map.containsKey(node_id) && this.map.get(node_id) != null){
        node_data temp = this.map.get(node_id);
        return temp.getNi();
        }
        return null;
    }

    @Override
    public node_data removeNode(int key) {
        if(!this.map.containsKey(key)){
            return null;
        }

        node_data temp = this.map.get(key);

        for(node_data n : this.map.get(key).getNi()){
            n.getNi().remove(this.map.get(key));
            numOfEdges--;
        }

        this.map.get(key).getNi().clear();
        map.remove(key, this.map.get(key));
        MC++;
        return temp;
    }

    @Override
    public void removeEdge(int node1, int node2) {
        if (this.map.get(node1).getNi().contains(this.map.get(node2)) &&
                this.map.get(node2).getNi().contains(this.map.get(node1))) {
            //Enter iff they are neighbors.
            this.map.get(node1).getNi().remove(this.map.get(node2));
            this.map.get(node2).getNi().remove(this.map.get(node1));
            numOfEdges--;
            MC++;
        }
    }

    @Override
    public int nodeSize() {
        return this.map.size();
    }

    @Override
    public int edgeSize() {
        return this.numOfEdges;
    }

    @Override
    public int getMC() {
        return this.MC;
    }

    /**
     * This is a normal toString method which print the graph out to the console.
     * Example: node --> [List of neighbors]
     * @return String
     */
    public String toString() {
        String ans ="\n";
        for (node_data n : this.map.values()){
            ans += n+"-->" + n.getNi()+ "\n";
        }
        return ans;
    }

}

