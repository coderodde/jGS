package net.coderodde.jgs.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static net.coderodde.jgs.Utilities.checkNotNull;

/**
 * This class models a graph as a container of nodes extending 
 * {@link net.coderodde.jgs.model.AbstractNode}.
 * 
 * @author Rodion Efremov
 * @version 1.6 
 * @param <T> the actual node implementation extending
 * <code>net.coderodde.jgs.model.Node</code>.
 */
public class Graph<T extends AbstractNode<T>> {
    
    /**
     * The list of nodes for constant time access by an index.
     */
    private final List<T> nodeList;
    
    /**
     * The set of nodes for constant time inclusion query. At any given moment,
     * has the same nodes as the <code>nodeList</code>.
     */
    private final Set<T> nodeSet;
    
    /**
     * The cached amount of edges in this graph.
     */
    int edgeAmount;
    
    /**
     * Constructs an empty graph.
     */
    public Graph() {
        this.nodeList = new ArrayList<>();
        this.nodeSet = new HashSet<>();
    }
    
    /**
     * Adds a node <tt>node</tt> to this graph only if this graph contains no 
     * node with the same name, namely <code>node.getName()</code>.
     * 
     * @param node the node to add.
     * 
     * @return <code>true</code> if <code>node</code> was actually added, 
     * <code>false</code> otherwise.
     */
    public boolean addNode(final T node) {
        checkNotNull(node, "Node is null.");
        
        if (nodeSet.contains(node)) {
            return false;
        }
        
        final Graph<T> oldOwnerGraph = node.getOwnerGraph();
        
        if (oldOwnerGraph != null) {
            oldOwnerGraph.removeNode(node);
        }
        
        nodeList.add(node);
        nodeSet.add(node);
        node.setOwnerGraph(this);
        return true;
    }
    
    /**
     * Returns the <tt>index</tt>th node in this graph.
     * @param index
     * @return 
     */
    public T get(final int index) {
        return nodeList.get(index);
    }
    
    /**
     * Returns <code>true</code> if <code>node</code> is in this graph;
     * <code>false</code> otherwise.
     * 
     * @param node the node to query.
     * @return <code>true</code> or <code>false</code> as above.
     */
    public boolean containsNode(final T node) {
        return nodeSet.contains(node);
    }
    
    /**
     * Removes a node from this graph if it is contained by this graph.
     * 
     * @param node the node to remove.
     * 
     * @return <code>true</code> if actual node removal took place, 
     * <code>false</code> otherwise.
     */
    public boolean removeNode(final T node) {
        checkNotNull(node, "Node is null.");
        
        if (node.getOwnerGraph() == this) {
            node.clear();
            nodeList.remove(node);
            nodeSet.remove(node);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Resets this graph to an empty graph with no edges and no nodes.
     */
    public void clear() {
        for (final T node : nodeList) {
            node.clear();
        }
        
        nodeList.clear();
        nodeSet.clear();
        edgeAmount = 0;
    }
    
    /**
     * Returns the amount of nodes in this graph.
     * 
     * @return the amount of nodes in this graph. 
     */
    public int size() {
        return nodeList.size();
    }
    
    /**
     * Returns the amount of edges in this graph.
     * 
     * @return the amount of edges in this graph. 
     */
    public int edgeCount() {
        return edgeAmount;
    }
}
