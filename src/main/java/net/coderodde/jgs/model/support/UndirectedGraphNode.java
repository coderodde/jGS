package net.coderodde.jgs.model.support;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import net.coderodde.jgs.model.AbstractNode;
import static net.coderodde.jgs.Utilities.checkNodesBelongToSameGraph;
import static net.coderodde.jgs.Utilities.checkNotNull;

/**
 * This class models nodes of undirected graphs where all neighbor nodes of this
 * node are considered both children and parents. The <b>edge invariant</b> is 
 * that if there is an (undirected) edge between two nodes <code>a</code> and 
 * <code>b</code>, both <code>a.neighbors.contains(b)</code> and
 * <code>b.neighbors.contains(a)</code> are <code>true</code>.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class UndirectedGraphNode extends AbstractNode<UndirectedGraphNode> {

    /**
     * The adjacency list.
     */
    private final Set<UndirectedGraphNode> neighbors;
    
    /**
     * Constructs a new undirected graph node with a given name.
     * 
     * @param name the name for the new node.
     */
    public UndirectedGraphNode(final String name) {
        super(name);
        this.neighbors = new LinkedHashSet<>();
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void clear() {
        if (getOwnerGraph() != null) {
            int edges = 0;
            Iterator<UndirectedGraphNode> iterator = iterator();
            
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
                ++edges;
            }
            
            addEdgeAmountDifference(-edges); // Note the minus sign!
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void connectTo(UndirectedGraphNode child) {
        checkNotNull(getOwnerGraph(), 
                     "The parent node does not belong to any graph.");
        checkNotNull(child, "The child node is null.");
        checkNotNull(child.getOwnerGraph(),
                     "The child node does not belong to any graph.");
        checkNodesBelongToSameGraph(this, child);
        
        if (!neighbors.contains(child)) {
            if (child.neighbors.contains(this)) {
                // NOTE1: This should never happen in production. Only debugging 
                // session should be able to break the "edge invariant"
                // mentioned in the javadoc of this class.
                throw new IllegalStateException(
                "Broken edge: node \"" + child.getName() + "\" has " +
                "\"" + this.getName() + "\" in its adjacency list, but not " +
                "otherwise.");
            }
            
            neighbors.add(child);
            child.neighbors.add(this);
            addEdgeAmountDifference(1);
        } else if (!child.neighbors.contains(this)) {
            // See NOTE1 and note the symmetry.
            throw new IllegalStateException(
            "Broken edge: node \"" + this.getName() + "\" has " + 
            "\"" + child.getName() + "\" in its adjacency list, but not " +
            "otherwise.");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @return returns <code>true</code> if this node is connected to 
     * <code>childCandidate</code>; <code>false</code> otherwise.
     */
    @Override
    public boolean isConnectedTo(final UndirectedGraphNode childCandidate) {
        checkNotNull(childCandidate, "The child candidate is null.");
        if (childCandidate.getOwnerGraph() != getOwnerGraph()) {
            return false;
        }
        
        return neighbors.contains(childCandidate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnectFrom(UndirectedGraphNode child) {
        checkNotNull(child, "Child is null.");
        
        if (neighbors.contains(child)) {
            neighbors.remove(child);
            child.neighbors.remove(this);
            addEdgeAmountDifference(-1);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @return an {@link Iterable} over this node's parents.
     */
    @Override
    public Iterable<UndirectedGraphNode> parents() {
        return new Iterable<UndirectedGraphNode>() {
            @Override
            public Iterator<UndirectedGraphNode> iterator() {
                return new NeighbourIterator();
            } 
        };
    }

    /**
     * {@inheritDoc}
     * 
     * @return an {@link Iterator} over this node's children.
     */
    @Override
    public Iterator<UndirectedGraphNode> iterator() {
        return new NeighbourIterator();
    }

    /**
     * {@inheritDoc} 
     * 
     * @return the amount of children of this node.
     */
    @Override
    public int childrenListSize() {
        return neighbors.size();
    }

    /**
     * {@inheritDoc} 
     * 
     * @return the amount of parents of this node.
     */
    @Override
    public int parentsListSize() {
        return neighbors.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @return a string representation of this node.
     */
    @Override
    public final String toString() {
        return "[UndirectedGraphNode " + name + "]";
    }
    
    /**
     * This class implements an iterator over enclosing node's neighbors.
     */
    private final class NeighbourIterator
    implements Iterator<UndirectedGraphNode> {

        /**
         * Caches the last node returned.
         */
        private UndirectedGraphNode lastReturned;
        
        /**
         * The actual iterator.
         */
        private final Iterator<UndirectedGraphNode> iterator = 
                UndirectedGraphNode.this.neighbors.iterator();
        
        /**
         * Returns <code>true</code> if there is more neighbors to iterate,
         * <code>false</code> otherwise.
         * 
         * @return a boolean indicating whether there is more neighbor nodes to
         * iterate.
         */
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
         * Returns the next neighbor.
         * 
         * @return the next neighbor. 
         */
        @Override
        public UndirectedGraphNode next() {
            return (lastReturned = iterator.next());
        }
        
        /**
         * Disconnects the enclosing node from the neighbor iterated most 
         * recently.
         */
        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new NoSuchElementException("No current neighbour node.");
            }
            
            iterator.remove();
            lastReturned.neighbors.remove(UndirectedGraphNode.this);
            lastReturned = null;
        }
    }
}
