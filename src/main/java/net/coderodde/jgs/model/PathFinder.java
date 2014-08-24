package net.coderodde.jgs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static net.coderodde.jgs.Utilities.checkNotNull;

/**
 * This abstract class defines the basic API for various shortest-path 
 * algorithms and systems.
 * 
 * @author Rodion Efremov
 * @param <T> the actual node implementation. 
 */
public abstract class PathFinder<T extends AbstractNode<T>> {

    /**
     * Performs the search for the path from node <code>source</code> to node
     * <code>target</code>.
     * 
     * @param source the source node.
     * @param target the target node.
     * 
     * @return the path from 
     */
    public abstract Path<T> search(final T source, final T target);
    
    protected Path<T> constructPath(final T target, 
                                    final Map<T, T> parentMap) {
        checkNotNull(target, "The target node is null.");
        checkNotNull(parentMap, "The parent map is null.");
        
        final List<T> list = new ArrayList<>();
        
        T current = target;
        
        while (current != null) {
            list.add(current);
            current = parentMap.get(current);
        }
        
        Collections.<T>reverse(list);
        return new Path<>(list);
    }
    
    protected Path<T> constructPath(final T touch,
                                    final Map<T, T> parentMapA,
                                    final Map<T, T> parentMapB) {
        checkNotNull(touch, "The touch node is null.");
        checkNotNull(parentMapA, "The forward parent map is null.");
        checkNotNull(parentMapB, "The backward parent map is null.");
        
        final List<T> list = new ArrayList<>();
        
        T current = touch;
        
        while (current != null) {
            list.add(current);
            current = parentMapA.get(current);
        }
        
        Collections.<T>reverse(list);
        
        current = parentMapB.get(touch);
        
        while (current != null) {
            list.add(current);
            current = parentMapB.get(current);
        }
        
        return new Path<>(list);
    }
}
