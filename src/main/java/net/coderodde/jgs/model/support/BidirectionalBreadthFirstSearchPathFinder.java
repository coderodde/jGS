package net.coderodde.jgs.model.support;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import net.coderodde.jgs.model.AbstractNode;
import net.coderodde.jgs.model.Path;
import net.coderodde.jgs.model.PathFinder;

public class 
        BidirectionalBreadthFirstSearchPathFinder<T extends AbstractNode<T>> 
extends PathFinder<T, Object> {

    private final Map<T, T> parentMapA;
    private final Map<T, T> parentMapB;
    
    private Deque<T> queueA;
    private Deque<T> queueB;
    
    public BidirectionalBreadthFirstSearchPathFinder(final Deque<T> queue) {
        this.parentMapA = new HashMap<>();
        this.parentMapB = new HashMap<>();
        
        if (queue == null) {
            this.queueA = new ArrayDeque<>();
            this.queueB = new ArrayDeque<>();
        } else {
            try {
                this.queueA = queue.getClass().newInstance();
                this.queueB = queue.getClass().newInstance();
            } catch (final InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
        }
    }
    
    public BidirectionalBreadthFirstSearchPathFinder() {
        this(null);
    }
    
    public Path<T> search(final T source, final T target) {
        queueA.clear();
        queueB.clear();
        
        parentMapA.clear();
        parentMapB.clear();
        
        queueA.addLast(source);
        queueB.addLast(target);
        
        parentMapA.put(source, null);
        parentMapB.put(target, null);
        
        while (queueA.size() * queueB.size() > 0) {
            T current = queueA.removeFirst();
            
            if (parentMapB.containsKey(current)) {
                return constructPath(current, parentMapA, parentMapB);
            }
            
            for (final T child : current) {
                if (!parentMapA.containsKey(child)) {
                    parentMapA.put(child, current);
                    queueA.addLast(child);
                }
            }
            
            current = queueB.removeFirst();
            
            if (parentMapA.containsKey(current)) {
                return constructPath(current, parentMapA, parentMapB);
            }
            
            for (final T parent : current.parents()) {
                if (!parentMapB.containsKey(parent)) {
                    parentMapB.put(parent, current);
                    queueB.addLast(parent);
                }
            }
        }
        
        return emptyPath;
    }
}
