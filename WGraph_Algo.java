package ex1;

import ex0.NodeData;
import ex0.node_data;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms, Serializable {
    private WGraph_DS g;

    public WGraph_Algo() {
        this.g = new WGraph_DS();
    }

//    //Copy constructor
//    public WGraph_Algo(weighted_graph g0){
//        this.g = g0;
//    }

    @Override
    public void init(weighted_graph g) {
        this.g = (WGraph_DS) g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.g;
    }

    @Override
    public weighted_graph copy() {
        weighted_graph ans = new WGraph_DS(this.g);

        return ans;
    }

    @Override
    public boolean isConnected() {
        //Necessary condition for a graph to be connected.
        //Based on graph theory.
        if (g.edgeSize() < (g.nodeSize() - 1)) {
            return false;
        }
        //Null graph is connected.
        //After deep search online I didn't find a conclusive answer.
        //Therefore, based on our usage, null graph will be connected.
        if (g.nodeSize() == 0) {
            return true;
        }
        //I will show connectivity using dfs based algo.
        node_info startingNode = new WGraph_DS.NodeInfo();

        //This is how i choose the starting node.
        for (node_info n : this.g.getV()) {
            startingNode = n;
            break;
        }
        return DFS(startingNode);
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        node_info start = g.getNode(src);
        node_info target = g.getNode(dest);

        if (dijkstra(start, target) != null) {
            return g.getNode(dest).getTag();
        }
        return -1;
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        node_info start = g.getNode(src);
        node_info target = g.getNode(dest);

        return dijkstra(start, target);
    }

    @Override
    public boolean save(String file) {
        boolean ans = false;
        ObjectOutputStream oos;

        try{
            FileOutputStream fout = new FileOutputStream(file);
            oos = new ObjectOutputStream(fout);
            oos.writeObject((WGraph_DS)this.getGraph());
            oos.close();
            ans = true;
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return ans;
    }

    @Override
    public boolean load(String file) {
        boolean ans = false;
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            WGraph_DS readGraph = (WGraph_DS) ois.readObject();
            this.init(readGraph);
            ois.close();
            ans = true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
         return ans;
    }

    /**
     * This method check's if a given graph is connected.(every node can reach to another)
     * This method based on DFS algo, as known.
     * It return true iff such path exists.
     *
     * @param node
     * @return boolean
     */
    private boolean DFS(node_info node) {
        HashMap<node_info, Boolean> visited = new HashMap<>(this.g.nodeSize());
        //Initializing all nodes to unvisited.
        for (node_info n : this.g.getV()) {
            visited.put(n, false);
        }

        Stack<node_info> s = new Stack<>();

        s.push(node);
        visited.replace(node, true);

        while (!s.isEmpty()) {
            WGraph_DS.NodeInfo n = (WGraph_DS.NodeInfo) s.pop();

            for (node_info neighbor : n.getNi()) {
                if (!visited.get(neighbor)) {
                    s.push(neighbor);
                    visited.replace(neighbor, true);
                }
            }
        }

        return !(visited.containsValue(false));
    }

    private List<node_info> dijkstra(node_info src, node_info dest) {

        PriorityQueue<WGraph_DS.NodeInfo> PQ = new PriorityQueue<WGraph_DS.NodeInfo>();
        //this map represent the status of each node.
        //-1 - unvisited, 0 - visited (but not the shortest path), 1 - finished(we found the shortest path to it)
        HashMap<WGraph_DS.NodeInfo, Integer> visited = new HashMap<WGraph_DS.NodeInfo, Integer>();
        HashMap<WGraph_DS.NodeInfo, WGraph_DS.NodeInfo> parents = new HashMap<WGraph_DS.NodeInfo, WGraph_DS.NodeInfo>();
        LinkedList<node_info> path = new LinkedList<node_info>();

        //Setting all distances to infinity, and all nodes to unvisited.
        for (node_info node : g.getV()) {
            node.setTag(Double.MAX_VALUE);
            visited.put((WGraph_DS.NodeInfo) node, -1);
        }
        //Enqueue {src, 0} to PQ.
        src.setTag(0);
        PQ.add((WGraph_DS.NodeInfo) src);
        parents.put((WGraph_DS.NodeInfo) src, null);

        while (!PQ.isEmpty()) {
            WGraph_DS.NodeInfo curr = PQ.remove();
            visited.put(curr, 1);
            if (curr == dest) {
                break;
            }
            for (node_info ne : curr.getNi()) {
                if (visited.get(ne) != 1) {
                    if(visited.get(ne) == -1) {
                        ne.setTag(curr.getTag() + g.getEdge(curr.getKey(), ne.getKey()));
                        PQ.add((WGraph_DS.NodeInfo) ne);
                        parents.put((WGraph_DS.NodeInfo) ne, curr);
                        visited.put((WGraph_DS.NodeInfo) ne, 0);
                    }
                    else{// ne is visited.
                        if(curr.getTag() + g.getEdge(curr.getKey(), ne.getKey()) < ne.getTag()){
                            //we found a shorter distance.
                            ne.setTag(curr.getTag() + g.getEdge(curr.getKey(), ne.getKey()));
                            //updating the PQ by removing and adding the node that changed.
                            parents.put((WGraph_DS.NodeInfo) ne, curr);
                            PQ.remove(ne);
                            PQ.add((WGraph_DS.NodeInfo) ne);
                        }
                    }
                }
            }
        }
        if(dest.getTag() != Double.MAX_VALUE) {
            //build our path.
            node_info runner = dest;
            path.add(runner);
            while (parents.get(runner) != null) {
                path.addFirst(parents.get(runner));
                runner = parents.get(runner);
            }
            return path;
        }
        return null;
    }
}

