package net.coderodde.jgs.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import static net.coderodde.jgs.Utilities.checkNotNull;

/** 
 * This class implements a path in a graph.
 * 
 * @author Rodion Efremov
 * @param <T> the actual node type implementation.
 */
public class Path<T extends AbstractNode<T>> 
implements Iterable<T> {
    
    /**
     * The node list constituting the path.
     */
    private final List<T> nodeList;
    
    /**
     * Constructs a new path holding the nodes of <code>list</code>.
     * 
     * @param list the list containing the nodes; the first node in the list
     * is supposed to be the start node of the path, and the last one is 
     * supposed to be the goal node.
     */
    public Path(final List<T> list) {
        final Set<T> filter = new HashSet<>(list.size());
        
        for (final T node : list) {
            checkNotNull(node.getOwnerGraph(), 
                         "The node " + node + " does not belong to a graph.");   
            filter.add(node);
        }
        
        if (filter.size() < list.size()) {
            throw new IllegalStateException(
                    "Duplicate node detected in a path.");
        }
        
        for (int i = 0; i < list.size() - 1; ++i) {
            if (list.get(i).isConnectedTo(list.get(i + 1)) == false) {
                throw new IllegalStateException("Disconnected edge.");
            }
        }
        
        this.nodeList = new ArrayList<>(list);
    }

    public T get(final int index) {
        return nodeList.get(index);
    }
    
    /**
     * Returns the amount of nodes in this path.
     * 
     * @return the amount of nodes in this path. 
     */
    public int size() {
        return nodeList.size();
    }
    
    /**
     * Returns <code>true</code> if this path denotes a non-existent path (has 
     * no nodes). Returns <code>false</code> if the path contains at least one
     * node.
     * 
     * @return whether this is a non-existent path.
     */
    public boolean isEmpty() {
        return nodeList.isEmpty();
    }
    
    /**
     * Returns an iterator over this paths nodes. The first node iterated is 
     * the source node of this path and the last one is the target.
     * 
     * @return an iterator over this paths nodes.
     */
    @Override
    public Iterator<T> iterator() {
        return new NodeIterator();
    }
    
    public boolean equals(final Object o) {
        if (!(o instanceof Path)) {
            return false;
        }
        
        final Iterator<T> it1 = this.iterator();
        final Iterator<T> it2 = ((Path<T>) o).iterator();
        
        while (it1.hasNext()) {
            if (it2.hasNext() == false) {
                return false;
            }
            
            final T node1 = it1.next();
            final T node2 = it2.next();
            
            if (!(node1.equals(node2))) {
                return false;
            }
        }
        
        if (it2.hasNext()) {
            return false;
        }
        
        return true;
    }
    
    /**
     * The actual iterator implementation.
     */
    private class NodeIterator implements Iterator<T> {

        private final Iterator<T> iterator = Path.this.nodeList.iterator();
        
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            return iterator.next();
        }
    }
}
