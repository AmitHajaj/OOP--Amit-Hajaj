package ex0;

import java.util.Collection;
import java.util.HashMap;

public class NodeData implements node_data {

    private int key;
    private String info;
    private int tag;
    private static int keySetter=0;
    private HashMap<Integer, node_data> neighbor = new HashMap<>();

    public NodeData(){
        this.key = keySetter;
        keySetter++;
        this.info = "";
        this.tag= 0;
        this.neighbor = new HashMap<Integer, node_data>();

    }

    public NodeData(int k, String i, int t){
        this.key = k;
        this.info = i;
        this.tag = t;
        this.neighbor = new HashMap<Integer, node_data>();
    }

    //Copy constructor.
    public NodeData(NodeData node){
        NodeData n = new NodeData(node.key, node.info, node.tag);
        this.tag = n.tag;
        this.info = n.info;
        this.key = n.key;
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public Collection<node_data> getNi() {
        return this.neighbor.values();
    }

    @Override
    public boolean hasNi(int key) {
        node_data temp = this.neighbor.get(key);
        if (temp != null) {return true;}
        else {return false;}
    }

    @Override
    public void addNi(node_data t) {
        this.neighbor.put(t.getKey(), t);
    }

    @Override
    public void removeNode(node_data node) {
        //Undirected graph forces us to remove both ways.
        node.getNi().remove(this);
        this.neighbor.remove(node);
    }
    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    /**
     * This is a normal toString method which print the key of a given node, out to the console.
     * Example: if n.key = 11, it will print "11".
     * @return
     */
    public String toString (){
        return "" + this.key;
    }
}

