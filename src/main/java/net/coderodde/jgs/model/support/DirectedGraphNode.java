package net.coderodde.jgs.model.support;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import net.coderodde.jgs.model.AbstractNode;
import static net.coderodde.jgs.Utilities.checkNodesBelongToSameGraph;
import static net.coderodde.jgs.Utilities.checkNotNull;

/**
 * This class models nodes of directed graphs. The <b>edge invariant</b> is 
 * that if there is a directed edge <code>(a, b)</code>, both 
 * <code>a.out.contains(b)</code> and <code>b.in.contains(a)</code> are 
 * <code>true</code>.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class DirectedGraphNode extends AbstractNode<DirectedGraphNode> {

    /**
     * The adjacency list of parents.
     */
    private final Set<DirectedGraphNode> in;
    
    /**
     * The adjacency list of children.
     */
    private final Set<DirectedGraphNode> out;
    
    /**
     * Constructs a new directed graph node with a given name.
     * 
     * @param name the name for the new node.
     */
    public DirectedGraphNode(final String name) {
        super(name);
        this.in  = new LinkedHashSet<>();
        this.out = new LinkedHashSet<>();
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void clear() {
        if (getOwnerGraph() != null) {
            int edges = 0;
            Iterator<DirectedGraphNode> iterator = iterator();
            
            // Disconnect from all the children.
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
                ++edges;
            }
            
            iterator = parents().iterator();
            
            // Disconnect from all the parents.
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
    public void connectTo(DirectedGraphNode child) {
        checkNotNull(getOwnerGraph(), 
                     "The parent node does not belong to any graph.");
        checkNotNull(child, "The child node is null.");
        checkNotNull(child.getOwnerGraph(),
                     "The child node does not belong to any graph.");
        checkNodesBelongToSameGraph(this, child);
        
        if (!out.contains(child)) {
            if (child.in.contains(this)) {
                // NOTE1: This should never happen in production. Only debugging 
                // session should be able to break the "edge invariant"
                // mentioned in the javadoc of this class.
                throw new IllegalStateException(
                    "Broken edge: node \"" + 
                    child.getName() + 
                    "\" has \"" +
                    this.getName() + 
                    "\" in its in-list, but not otherwise.");
            }
            
            out.add(child);
            child.in.add(this);
            addEdgeAmountDifference(1);
        } else if (!child.in.contains(this)) {
            // See NOTE1 and note the symmetry.
            throw new IllegalStateException(
                "Broken edge: node \"" + 
                this.getName() + 
                "\" has \"" + 
                child.getName() + 
                "\" in its out-list, but not otherwise.");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @return returns <code>true</code> if this node is connected to 
     * <code>childCandidate</code>; <code>false</code> otherwise.
     */
    @Override
    public boolean isConnectedTo(final DirectedGraphNode childCandidate) {
        checkNotNull(childCandidate, "The child candidate is null.");
        if (childCandidate.getOwnerGraph() != getOwnerGraph()) {
            return false;
        }
        
        return out.contains(childCandidate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnectFrom(DirectedGraphNode child) {
        checkNotNull(child, "Child is null.");
        
        if (out.contains(child)) {
            out.remove(child);
            child.in.remove(this);
            addEdgeAmountDifference(-1);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @return an {@link Iterable} over this node's parents.
     */
    @Override
    public Iterable<DirectedGraphNode> parents() {
        return new Iterable<DirectedGraphNode>() {
            @Override
            public Iterator<DirectedGraphNode> iterator() {
                return new ParentIterator();
            } 
        };
    }

    /**
     * {@inheritDoc}
     * 
     * @return an {@link Iterator} over this node's children.
     */
    @Override
    public Iterator<DirectedGraphNode> iterator() {
        return new ChildIterator();
    }

    /**
     * {@inheritDoc} 
     * 
     * @return the amount of children of this node.
     */
    @Override
    public int childrenListSize() {
        return out.size();
    }

    /**
     * {@inheritDoc} 
     * 
     * @return the amount of parents of this node.
     */
    @Override
    public int parentsListSize() {
        return in.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @return a string representation of this node.
     */
    @Override
    public final String toString() {
        return "[DirectedGraphNode " + name + "]";
    }
    
    /**
     * This class implements an iterator over enclosing node's children.
     */
    private final class ChildIterator
    implements Iterator<DirectedGraphNode> {

        /**
         * Caches the last node returned.
         */
        private DirectedGraphNode lastReturned;
        
        /**
         * The actual iterator.
         */
        private final Iterator<DirectedGraphNode> iterator = 
                DirectedGraphNode.this.out.iterator();
        
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
        public DirectedGraphNode next() {
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
            lastReturned.in.remove(DirectedGraphNode.this);
            lastReturned = null;
        }
    }
    
    /**
     * This class implements an iterator over enclosing node's parents.
     */
    private final class ParentIterator
    implements Iterator<DirectedGraphNode> {

        /**
         * Caches the last node returned.
         */
        private DirectedGraphNode lastReturned;
        
        /**
         * The actual iterator.
         */
        private final Iterator<DirectedGraphNode> iterator = 
                DirectedGraphNode.this.in.iterator();
        
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
        public DirectedGraphNode next() {
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
            lastReturned.out.remove(DirectedGraphNode.this);
            lastReturned = null;
        }
    }
}
