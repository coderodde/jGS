package net.coderodde.jgs.model.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static net.coderodde.jgs.Utilities.checkNotNull;
import net.coderodde.jgs.model.AbstractNode;
import net.coderodde.jgs.model.AbstractWeightFunction;
import net.coderodde.jgs.model.Path;
import net.coderodde.jgs.model.PathFinder;
import net.coderodde.jgs.model.Weight;
import net.coderodde.jgs.model.ds.MinPriorityQueue;
import net.coderodde.jgs.model.ds.support.DaryHeap;

public class DijkstraPathFinder<T extends AbstractNode<T>, 
                                W extends Comparable<? super W>,
                                E extends Weight<W>>
extends PathFinder<T, W> {

    private final AbstractWeightFunction<T, W> f;
    private final Weight<W> weight;
    private final Map<T, T> parentMap;
    private final Map<T, W> distanceMap;
    private final MinPriorityQueue<T, W> openSet;
    private final Set<T> closedSet;
    
    public DijkstraPathFinder(final AbstractWeightFunction<T, W> f,
                              final E weight,
                              final MinPriorityQueue<T, W> openSet) {
        checkNotNull(f, "The weight function is null.");
        checkNotNull(weight, "The weight object is null.");
        checkNotNull(openSet, "The priority queue is null.");
        
        this.f = f;
        this.weight = weight;
        this.openSet = openSet;
        this.closedSet = new HashSet<>();
        this.parentMap = new HashMap<>();
        this.distanceMap = new HashMap<>();
    }
    
    public DijkstraPathFinder(final AbstractWeightFunction<T, W> f,
                              final E weight) {
        this(f, weight, new DaryHeap<T, W>());
    }
    
    @Override
    public Path<T> search(T source, T target) {
        checkNotNull(source, "The source node is null.");
        checkNotNull(target, "The target node is null.");
        checkNotNull(source.getOwnerGraph(), 
                     "The source node belongs to no graph.");
        
        checkNotNull(target.getOwnerGraph(),
                     "The target node belongs to no graph.");
        
        openSet.clear();
        closedSet.clear();
        parentMap.clear();
        distanceMap.clear();
        
        openSet.add(source, weight.identity());
        parentMap.put(source, null);
        distanceMap.put(source, weight.identity());
        
        while (openSet.size() > 0) {
            final T current = openSet.extractMinimum();
            
            if (current.equals(target)) {
                return constructPath(target, parentMap);
            }
            
            closedSet.add(current);
            
            for (final T child : current) {
                if (closedSet.contains(child)) {
                    continue;
                }
                
                final W tmpg = weight.append(distanceMap.get(current),
                                             f.get(current, child));
                
                if (parentMap.containsKey(child) == false) {
                    // This is the first time 'child' is discovered.
                    openSet.add(child, tmpg);
                    parentMap.put(child, current);
                    distanceMap.put(child, tmpg);
                } else if (tmpg.compareTo(distanceMap.get(child)) < 0) {
                    // Improving the distance to 'child'.
                    openSet.decreasePriority(child, tmpg);
                    parentMap.put(child, current);
                    distanceMap.put(child, tmpg);
                }
            }
        }
        
        return emptyPath;
    }
}
