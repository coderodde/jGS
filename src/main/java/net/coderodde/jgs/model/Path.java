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
    
    public Path() {
        this.nodeList = new ArrayList<>();
    }
    
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
