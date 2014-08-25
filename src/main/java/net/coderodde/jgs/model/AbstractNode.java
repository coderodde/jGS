package net.coderodde.jgs.model;

import java.util.Objects;
import static net.coderodde.jgs.Utilities.checkNotNull;

/**
 * This class defines the API for graph nodes.
 * 
 * @author Rodion Efremov
 * @version 1.6
 * @param <T> the actual graph node type extending this type.
 */
public abstract class AbstractNode<T extends AbstractNode<T>> implements Iterable<T> {

    /**
     * Holds the name of this node.
     */
    protected final String name;
    
    /**
     * Holds the reference to the graph this node belongs to.
     */
    protected Graph<T> ownerGraph;
    
    /**
     * Initializes this node with a name.
     * 
     * @param name the name of this node.
     */
    public AbstractNode(final String name) {
        checkNotNull(name, "A node must have a name; null received.");
        this.name = name;
    }
    
    /**
     * Returns the name of this node.
     * 
     * @return the name of this node. 
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the graph owning this node, or <code>null</code> if this node is
     * an "orphan" node.
     * 
     * @return the owner graph or <code>null</code> if there is no such.
     */
    public Graph<T> getOwnerGraph() {
        return ownerGraph;
    }
    
    /**
     * Sets the owner graph for this node.
     * 
     * @param graph new owner graph. 
     */
    public void setOwnerGraph(final Graph<T> graph) {
        if (ownerGraph != null) {
            clear();
        }
        ownerGraph = graph;
    }
    
    /**
     * Removes all edges incident on this node.
     */
    public abstract void clear();
    
    /**
     * Connects this node to <code>child</code>.
     * 
     * @param child the node to connect to.
     */
    public abstract void connectTo(final T child);
    
    /**
     * Checks whether this node is connected to <code>childCandidate</code>.
     * 
     * @param childCandidate the node to test against.
     * 
     * @return <code>true</code> if this node has an edge to
     * <code>childCandidate</code>, <code>false</code> otherwise.
     */
    public abstract boolean isConnectedTo(final T childCandidate);
    
    /**
     * If <code>this</code> and <code>child</code> belong to the same non-null
     * graph, removes the edge <code>(this, child)</code> from the graph;
     * otherwise does nothing.
     * 
     * @param child the child to disconnect from.
     */
    public abstract void disconnectFrom(final T child);
    
    /**
     * Returns an <code>Iterable</code> over this node's parent nodes.
     * 
     * @return an <code>Iterable</code> over this node's parent nodes. 
     */
    public abstract Iterable<T> parents();
    
    /**
     * Returns the amount of children incident on this node.
     * 
     * @return amount of children.
     */
    public abstract int childrenListSize();
    
    /**
     * Returns the amount of parents incident on this node.
     * 
     * @return amount of children.
     */
    public abstract int parentsListSize();
    
    /**
     * Returns the string representation of this node.
     * 
     * @return the string representation of this node.
     */
    @Override
    public abstract String toString();
    
    /**
     * Returns <code>true</code> if this node is considered to be same as
     * <code>o</code>.
     * 
     * @param o the object to compare against.
     * 
     * @return <code>true</code> if the two objects are "same"; 
     * <code>false</code> otherwise.
     */
    @Override
    public abstract boolean equals(final Object o);

    /**
     * Returns a hash code of the node.
     * 
     * @return a hash code.
     */
    @Override
    public abstract int hashCode();
    
    protected void addEdgeAmountDifference(final int diff) {
        ownerGraph.edgeAmount += diff;
    }
}
