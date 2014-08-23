package net.coderodde.jgs.model;

import java.util.LinkedHashMap;
import java.util.Map;
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
     * The name of this graph.
     */
    private final String name;
    
    /**
     * The map mapping node names to nodes.
     */
    private final Map<String, T> map;
    
    /**
     * The cached amount of edges in this graph.
     */
    int edgeAmount;
    
    /**
     * Constructs an empty graph with name <code>name</code>.
     * 
     * @param name the name of the new graph.
     */
    public Graph(final String name) {
        checkNotNull(name, "The graph must have a name. Null received.");
        this.name = name;
        this.map = new LinkedHashMap<>();
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
        
        if (map.containsKey(node.getName())) {
            return false;
        }
        
        final Graph<T> oldOwnerGraph = node.getOwnerGraph();
        
        if (oldOwnerGraph != null) {
            oldOwnerGraph.removeNode(node);
        }
        
        map.put(node.getName(), node);
        node.setOwnerGraph(this);
        return true;
    }
    
    /**
     * Removes a node from this graph if it is contained by the graph.
     * 
     * @param node the node to remove.
     */
    public void removeNode(final T node) {
        checkNotNull(node, "Node is null.");
        
        if (node.getOwnerGraph() == this) {
            node.clear();
            map.remove(node.getName());
        }
    }
    
    /**
     * Resets this graph to an empty graph with no edges and no nodes.
     */
    public void clear() {
        // We have to copy the map values (nodes) into an array because 
        // otherwise we would have to modify it (via remove()) which would
        // throw ConcurrentModificationException.
        AbstractNode[] nodeArray = new AbstractNode[map.size()];
        int index = 0;
        
        // Copy.
        for (final T node : map.values()) {
            nodeArray[index++] = node;
        }
        
        // Remove.
        for (final AbstractNode node : nodeArray) {
            removeNode((T) node);
        }
    }
    
    /**
     * Returns the amount of nodes in this graph.
     * 
     * @return the amount of nodes in this graph. 
     */
    public int size() {
        return map.size();
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
