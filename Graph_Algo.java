package ex0;

//import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class implements graph_algorithms interface.
  */
public class Graph_Algo implements graph_algorithms {
    graph g0;

    //Constructor.
    public Graph_Algo(){
        this.g0 = new Graph_DS();
    }

    //Copy constructor.
    public Graph_Algo (graph g){
        this.g0 = g;
    }


    @Override
    public void init(graph g) {
        this.g0 = g;
    }

    /**
     * Here we will perform a deep copy of a given graph.
     * Thee copy will be performed at Graph_DS copy constructor.
     * @return graph.
     */
    @Override
    public graph copy() {
        return new Graph_DS(this.g0);
    }


    @Override
    public boolean isConnected() {
        //Necessary condition for a graph to be connected.
        //Based on graph theory.
        if (g0.edgeSize() < (g0.nodeSize()-1)) {
            return false;
        }
        //I will show connectivity using dfs based algo.
        node_data startingNode = new NodeData();

        //This is how i choose the starting node.
        for(node_data n : this.g0.getV()){
            startingNode = n;
            break;
        }

        return DFS(startingNode);
    }


    /**
     * This method find the lowest distance between two given nodes.
     * It calls a help function which is BFS based.
     * If such path exists, it returns it's length.
     * Otherwise, (-1).
     * Note: length from node to itself is 0.

     * @param src - start node
     * @param dest - end (target) node
     * @return int
     */
    @Override
    public int shortestPathDist(int src, int dest) {
        if(src==dest){
            return 0;
        }

        BFS(this.g0.getNode(src));
        return this.g0.getNode(dest).getTag();
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        node_data start = this.g0.getNode(src);
        node_data target = this.g0.getNode(dest);
        ArrayList<node_data> path = new ArrayList<>();

        //If path is from node to itself.
        if(src == dest){
            return path;
        }

        BFS(start);
        if(target.getTag() != 0){
            path.add(target);
            int level = target.getTag();
            node_data temp = target;
            while(level != 0){
                for(node_data n : temp.getNi()){
                    if(n.getTag() == (temp.getTag()-1)){
                        path.add(n);
                        temp = n;
                        level--;
                        break;
                    }
                }
            }
        }
        return path;
    }

    /**
     * This method check's if a given graph is connected.(every node can reach to another)
     * This method based on DFS algo, as known.
     * It return true iff such path exists.
     * @param node
     * @return boolean
     */
    private boolean DFS (node_data node){
        HashMap<node_data, Boolean> visited = new HashMap<>(this.g0.nodeSize());
        //Initializing all nodes to unvisited.
        for(node_data n : this.g0.getV()) {
            visited.put(n, false);
        }

        Stack<node_data> s = new Stack<>();

        s.push(node);
        visited.replace(node, true);

        while(!s.isEmpty()){
            node_data n = s.pop();

            for(node_data neighbor : n.getNi()){
                if(!visited.get(neighbor)){
                    s.push(neighbor);
                    visited.replace(neighbor, true);
                }
            }
        }

        return !(visited.values().contains(false));
    }


    /**
     * Helper function based on the idea of BFS.(from Eric Demaine lecture as we told to see)
     * Work similarly to my DFS method, with some changes.
     * Here we give each node a level, that represents it length from starting node.
     * Note: Path from node to itself returns 0.
     * Note: This implementation uses queue(LinkedList based) to travel the graph.

     * @param src - start node
     */

    private void BFS (node_data src) {
        if (this.g0.getV().contains(src)) {
                HashMap<node_data, Boolean> visited = new HashMap<>(this.g0.nodeSize());
                LinkedList<node_data> s = new LinkedList<>();

                //Initializing all nodes to unvisited, and path unknown yet.
                for (node_data n : this.g0.getV()) {
                    visited.put(n, false);
                    n.setTag(-1);
                }

                s.add(src);
                visited.replace(src, true);
                src.setTag(0);

                while (s.size() != 0) {
                    node_data n = s.poll();

                    for (node_data neighbor : n.getNi()) {
                        if (!visited.get(neighbor)) {
                            s.add(neighbor);
                            visited.replace(neighbor, true);
                            neighbor.setTag(n.getTag() + 1);
                        }
                    }
                }
            }
    }
}