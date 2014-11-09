package net.coderodde.jgs.model.support;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import static net.coderodde.jgs.Utilities.checkNotNull;
import net.coderodde.jgs.model.AbstractNode;
import net.coderodde.jgs.model.Path;
import net.coderodde.jgs.model.PathFinder;

/**
 * This class implements (unidirectional) breadth-first search.
 * 
 * @author Rodion Efremov
 * @param <T> the type of the graph nodes, must extend {@code AbstractNode}.
 */
public class BreadthFirstSearchPathFinder<T extends AbstractNode<T>> 
extends PathFinder<T, Integer> {

    private final Map<T, T> parentMap;
    private final Deque<T> queue;
    
    public BreadthFirstSearchPathFinder(final Deque<T> queue) {
        checkNotNull(queue, "The given queue is null.");
        queue.clear();
        this.queue = queue;
        this.parentMap = new HashMap<>();
    }
    
    public BreadthFirstSearchPathFinder() {
        this(new ArrayDeque<T>());
    }
    
    @Override
    public Path<T> search(T source, T target) {
        queue.clear();
        parentMap.clear();
        
        queue.add(source);
        parentMap.put(source, null);
        
        while (queue.size() > 0) {
            final T current = queue.removeFirst();
            
            if (current.equals(target)) {
                return constructPath(target, parentMap);
            }
            
            for (final T child : current) {
                if (!parentMap.containsKey(child)) {
                    parentMap.put(child, current);
                    queue.addLast(child);
                }
            }
        }
        
        return emptyPath;
    }
}
