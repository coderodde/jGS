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

public class BidirectionalDijkstraPathFinder<T extends AbstractNode<T>, 
                                W extends Comparable<? super W>,
                                E extends Weight<W>>
extends PathFinder<T, W> {

    private final AbstractWeightFunction<T, W> f;
    private final Weight<W> weight;
    private final MinPriorityQueue<T, W> openSet1;
    private final MinPriorityQueue<T, W> openSet2;
    private final Map<T, T> parentMap1;
    private final Map<T, T> parentMap2;
    private final Map<T, W> distanceMap1;
    private final Map<T, W> distanceMap2;
    private final Set<T> closedSet1;
    private final Set<T> closedSet2;
    
    public BidirectionalDijkstraPathFinder(
            final AbstractWeightFunction<T, W> f,
            final E weight,
            final MinPriorityQueue<T, W> openSet1) {
        checkNotNull(f, "The weight function is null.");
        checkNotNull(weight, "The weight function is null.");
        checkNotNull(openSet1, "The queue is null.");
        
        this.f = f;
        this.weight = weight;
        this.openSet1 = openSet1.spawn();
        this.openSet2 = openSet1.spawn();
        this.parentMap1 = new HashMap<>();
        this.parentMap2 = new HashMap<>();
        this.distanceMap1 = new HashMap<>();
        this.distanceMap2 = new HashMap<>();
        this.closedSet1 = new HashSet<>();
        this.closedSet2 = new HashSet<>();
    }
    
    public BidirectionalDijkstraPathFinder(
            final AbstractWeightFunction<T, W> f,
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
        
        openSet1.clear();
        openSet2.clear();
        parentMap1.clear();
        parentMap2.clear();
        distanceMap1.clear();
        distanceMap2.clear();
        closedSet1.clear();
        closedSet2.clear();
        
        openSet1.add(source, weight.identity());
        openSet2.add(target, weight.identity());
        
        parentMap1.put(source, null);
        parentMap2.put(target, null);
        
        distanceMap1.put(source, weight.identity());
        distanceMap2.put(target, weight.identity());
        
        T touch = null;
        W m;
        
        while (!openSet1.isEmpty() && !openSet2.isEmpty()) {
            
            
            if (openSet1.size() < openSet2.size()) {
                // Expand the forward frontier.
                final T current = openSet1.extractMinimum();
                
                if (closedSet2.contains(current)) {
                    return constructPath(current, parentMap1, parentMap2);
                }
                
                closedSet1.add(current);
                
                for (final T child : current) {
                    if (closedSet1.contains(child)) {
                        continue;
                    }
                    
                    W tmpg = weight.append(distanceMap1.get(current),
                                           f.get(current, child));
                    
                    if (distanceMap1.containsKey(child) == false) {
                        openSet1.add(child, tmpg);
                        distanceMap1.put(child, tmpg);
                        parentMap1.put(child, current);
                        
                    } else if (tmpg.compareTo(distanceMap1.get(child)) < 0) {
                        openSet1.add(child, tmpg);
                        distanceMap1.put(child, tmpg);
                        parentMap1.put(child, current);
                    }
                }
            } else {
                // Expand the backward frontier.
                final T current = openSet2.extractMinimum();
                
                if (closedSet1.contains(current)) {
                    return constructPath(current, parentMap1, parentMap2);
                }
                
                closedSet2.add(current);
                
                for (final T parent : current.parents()) {
                    if (closedSet2.contains(parent)) {
                        continue;
                    }
                    
                    W tmpg = weight.append(distanceMap2.get(current),
                                           f.get(parent, current));
                    
                    
                    if (distanceMap2.containsKey(parent) == false) {
                        openSet2.add(parent, tmpg);
                        distanceMap2.put(parent, tmpg);
                        parentMap2.put(parent, current);
                        
                    } else if (tmpg.compareTo(distanceMap2.get(parent)) < 0) {
                        openSet2.add(parent, tmpg);
                        distanceMap2.put(parent, tmpg);
                        parentMap2.put(parent, current);
                    }
                }
            }
        }
        
        return emptyPath;
    }
}