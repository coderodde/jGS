package net.coderodde.jgs.model.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static net.coderodde.jgs.Utilities.checkNodesBelongToSameGraph;
import static net.coderodde.jgs.Utilities.checkNotNull;
import net.coderodde.jgs.model.AbstractHeuristicFunction;
import net.coderodde.jgs.model.AbstractNode;
import net.coderodde.jgs.model.AbstractWeightFunction;
import net.coderodde.jgs.model.Path;
import net.coderodde.jgs.model.PathFinder;
import net.coderodde.jgs.model.Weight;
import net.coderodde.jgs.model.ds.MinPriorityQueue;
import net.coderodde.jgs.model.ds.support.DaryHeap;

public class BidirectionalAStarPathFinder<T extends AbstractNode<T>, 
                                W extends Comparable<? super W>,
                                E extends Weight<W>>
extends PathFinder<T, W> {

    /**
     * The weight function to use in search.
     */
    private final AbstractWeightFunction<T, W> f;
    
    /**
     * The heuristic function to use in search.
     */
    private final AbstractHeuristicFunction<T, W> hf;
    
    /**
     * The weight implementation.
     */
    private final Weight<W> weight;
    
    /**
     * The open set for forward search frontier.
     */
    private final MinPriorityQueue<T, W> openSet1;
    
    /**
     * The open set for backward search frontier.
     */
    private final MinPriorityQueue<T, W> openSet2;
    
    /**
     * Maps a node to its parent on a shortest path in forward search.
     */
    private final Map<T, T> parentMap1;
    
    /**
     * Does exactly the same as <code>parentMap1</code>, but in the backward
     * search space.
     */
    private final Map<T, T> parentMap2;
    
    /**
     * Stores the distance scores to the source node in the forward search.
     */
    private final Map<T, W> distanceMap1;
    
    /**
     * Stores the distance scores to the target node in the backward search.
     */
    private final Map<T, W> distanceMap2;
    
    /**
     * Maintains the closed set in forward search.
     */
    private final Set<T> closedSet1;
    
    /**
     * Maintains the closed set in backward search.
     */
    private final Set<T> closedSet2;
    
    /**
     * Constructs a path finder using bidirectional Dijkstra's algorithm with 
     * give weight function, weight implementation type and priority queue.
     * 
     * @param f the weight function.
     * @param hf the heuristic function.
     * @param weight the weight type operations.
     * @param queue the priority queue implementation.
     */
    public BidirectionalAStarPathFinder(
            final AbstractWeightFunction<T, W> f,
            final AbstractHeuristicFunction<T, W> hf,
            final E weight,
            final MinPriorityQueue<T, W> queue) {
        checkNotNull(f, "The weight function is null.");
        checkNotNull(hf, "The heuristic function is null.");
        checkNotNull(weight, "The weight function is null.");
        checkNotNull(queue, "The queue is null.");
        
        this.f = f;
        this.hf = hf;
        this.weight = weight;
        this.openSet1 = queue.spawn();
        this.openSet2 = queue.spawn();
        this.parentMap1 = new HashMap<>();
        this.parentMap2 = new HashMap<>();
        this.distanceMap1 = new HashMap<>();
        this.distanceMap2 = new HashMap<>();
        this.closedSet1 = new HashSet<>();
        this.closedSet2 = new HashSet<>();
    }
    
    /**
     * Constructs a path finder using bidirectional Dijkstra's algorithm with
     * give weight function and weight type implementation. Uses binary heap 
     * for open sets under the hood.
     * 
     * @param f the weight function.
     * @param hf the heuristic function.
     * @param weight the weight type operations.
     */
    public BidirectionalAStarPathFinder(
            final AbstractWeightFunction<T, W> f,
            final AbstractHeuristicFunction<T, W> hf,
            final E weight) {
        this(f, hf, weight, new DaryHeap<T, W>());
    }
    
    /**
     * {@inheritDoc}
     * 
     * @param source the source node.
     * @param target the target node.
     * 
     * @return a shortest path or an empty path if target is not reachable from
     * source.
     */
    @Override
    public Path<T> search(T source, T target) {
        checkNotNull(source, "The source node is null.");
        checkNotNull(target, "The target node is null.");
        checkNotNull(source.getOwnerGraph(), 
                     "The source node belongs to no graph.");
        
        checkNotNull(target.getOwnerGraph(),
                     "The target node belongs to no graph.");
        
        checkNodesBelongToSameGraph(source, target);
        
        openSet1.clear();
        openSet2.clear();
        parentMap1.clear();
        parentMap2.clear();
        distanceMap1.clear();
        distanceMap2.clear();
        closedSet1.clear();
        closedSet2.clear();
        
        openSet1.add(source, hf.evaluate(source, target));
        openSet2.add(target, hf.evaluate(source, target));
        
        parentMap1.put(source, null);
        parentMap2.put(target, null);
        
        distanceMap1.put(source, weight.identity());
        distanceMap2.put(target, weight.identity());
        
        T touch = null;
        W m = weight.largest();
        
        while (!openSet1.isEmpty() && !openSet2.isEmpty()) {
            
            if (touch != null) {
                final T m1 = openSet1.min();
                final T m2 = openSet2.min();
                
                W tmp1 = weight.append(distanceMap1.get(m1),
                                       hf.evaluate(source, m1));
                
                W tmp2 = weight.append(distanceMap2.get(m2),
                                       hf.evaluate(target, m2));
                
                if (m.compareTo(tmp1) <= 0 || m.compareTo(tmp2) <= 0) {
                    return constructPath(touch, parentMap1, parentMap2);
                }
            }
            
            if (openSet1.size() < openSet2.size()) {
                // Expand the forward frontier.
                T current = openSet1.extractMinimum();
                
                closedSet1.add(current);
                
                for (final T child : current) {
                    if (closedSet1.contains(child)) {
                        continue;
                    }
                    
                    W tmpg = weight.append(distanceMap1.get(current),
                                           f.get(current, child));
                    
                    if (distanceMap1.containsKey(child) == false) {
                        openSet1.add(child, 
                                     weight.append(tmpg,
                                                   hf.evaluate(child, target)));
                        distanceMap1.put(child, tmpg);
                        parentMap1.put(child, current);
                        
                        // Improvement possible.
                        if (closedSet2.contains(child)) {
                            final W pathLength = 
                                    weight.append(tmpg, 
                                                  distanceMap2.get(child));
                            
                            if (m.compareTo(pathLength) > 0) {
                                m = pathLength;
                                touch = child;
                            }
                        }
                    } else if (tmpg.compareTo(distanceMap1.get(child)) < 0) {
                        W newf = weight.append(tmpg,
                                               hf.evaluate(child, target));
                        
                        openSet1.decreasePriority(child, newf);
                        distanceMap1.put(child, tmpg);
                        parentMap1.put(child, current);
                        
                        // Improvement possible.
                        if (closedSet2.contains(child)) {
                            final W pathLength = 
                                    weight.append(tmpg, 
                                                  distanceMap2.get(child));
                            
                            if (m.compareTo(pathLength) > 0) {
                                m = pathLength;
                                touch = child;
                            }
                        }
                    }
                }
            } else {
                // Expand the backward frontier.
                final T current = openSet2.extractMinimum();
                
                closedSet2.add(current);
                
                for (final T parent : current.parents()) {
                    if (closedSet2.contains(parent)) {
                        continue;
                    }
                    
                    W tmpg = weight.append(distanceMap2.get(current),
                                           f.get(parent, current));
                    
                    
                    if (distanceMap2.containsKey(parent) == false) {
                        W newf = weight.append(tmpg,
                                               hf.evaluate(parent, source));
                        
                        openSet2.add(parent, newf);
                        distanceMap2.put(parent, tmpg);
                        parentMap2.put(parent, current);
                        
                        // Improvement possible.
                        if (closedSet1.contains(parent)) {
                            final W pathLength = 
                                    weight.append(tmpg, 
                                                  distanceMap1.get(parent));
                            
                            if (m.compareTo(pathLength) > 0) {
                                m = pathLength;
                                touch = parent;
                            }
                        }
                    } else if (tmpg.compareTo(distanceMap2.get(parent)) < 0) {
                        W newf = weight.append(tmpg,
                                               hf.evaluate(parent, source));
                        
                        openSet2.decreasePriority(parent, newf);
                        distanceMap2.put(parent, tmpg);
                        parentMap2.put(parent, current);
                        
                        // Improvement possible.
                        if (closedSet1.contains(parent)) {
                            final W pathLength = 
                                    weight.append(tmpg, 
                                                  distanceMap1.get(parent));
                            
                            if (m.compareTo(pathLength) > 0) {
                                m = pathLength;
                                touch = parent;
                            }
                        }
                    }
                }
            }
        }
        
        return emptyPath;
    }
}