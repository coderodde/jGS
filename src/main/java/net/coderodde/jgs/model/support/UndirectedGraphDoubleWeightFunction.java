package net.coderodde.jgs.model.support;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static net.coderodde.jgs.Utilities.checkNotInfinite;
import static net.coderodde.jgs.Utilities.checkNotNaN;
import static net.coderodde.jgs.Utilities.checkNotNull;
import net.coderodde.jgs.model.AbstractWeightFunction;
import net.coderodde.jgs.model.Path;

/**
 * This class implements a real-valued weight function over an undirected graph.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class UndirectedGraphDoubleWeightFunction 
extends AbstractWeightFunction<UndirectedGraphNode, Double> {
    
    /**
     * {@inheritDoc}
     * 
     * @param from the tail node of the edge.
     * @param to the head node of the edge.
     * @param weight the weight to associate with the edge
     * <code>(from, to)</code>.
     */
    @Override
    public void put(final UndirectedGraphNode from, 
                    final UndirectedGraphNode to, 
                    final Double weight) {
        checkNotNull(from, "The tail node is null.");
        checkNotNull(to, "The head node is null.");
        checkNotNull(weight, "The weight is null.");
        checkNotInfinite(weight, "The weight is infinite: " + weight);
        checkNotNaN(weight, "The weight is NaN.");
        
        Map<UndirectedGraphNode, Double> partialMap = map.get(from);
        
        if (partialMap == null) {
            // Once here, we are creating a new edge weight.
            partialMap = new HashMap<>();
            partialMap.put(to, weight);
            map.put(from, partialMap);
            
            // Another direction:
            partialMap = new HashMap<>();
            partialMap.put(from, weight);
            map.put(to, partialMap);
        } else {
            // Once here, we have to update an existing weight.
            partialMap.put(to, weight);
            
            // Another direction:
            Map<UndirectedGraphNode, Double> tmpmap = map.get(to);
            
            if (tmpmap == null) {
                tmpmap = new HashMap<>();
                tmpmap.put(from, weight);
                map.put(to, tmpmap);
                return;
            }
            
            // Secondary map exists and is referenced by tmpmap.
            tmpmap.put(from, weight);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @param from the tail node of the edge.
     * @param to the head node of the edge.
     * 
     * @return returns the weight of the edge <code>(from, to)</code>.
     */
    @Override
    public Double get(UndirectedGraphNode from, UndirectedGraphNode to) {
        checkNotNull(from, "The tail node (from) is null.");
        checkNotNull(to, "The head node (to) is null.");
        
        // try from -> to -> weight.
        Map<UndirectedGraphNode, Double> partialMap = map.get(from);
        
        if (partialMap == null) {
            throw new IllegalStateException("The weight not found.");
        }
        
        final Double weight = partialMap.get(to);
        
        if (weight == null) {
            throw new IllegalStateException("The weight not found.");
        }
        
        return weight;
    }

    /**
     * {@inheritDoc}
     * 
     * @param path the path whose weight to compute.
     * 
     * @return the weight of the path.
     */
    @Override
    public Double getPathWeight(Path<UndirectedGraphNode> path) {
        final Iterator<UndirectedGraphNode> iterator = path.iterator();
        
        if (iterator.hasNext() == false) {
            return 0.0;
        }
        
        UndirectedGraphNode u = iterator.next();
        
        if (iterator.hasNext() == false) {
            return 0.0;
        }
        
        UndirectedGraphNode v = iterator.next();
        
        double weight = get(u, v);
        
        while (iterator.hasNext()) {
            u = v;
            v = iterator.next();
            weight += get(u, v);
        }
        
        return weight;
    }
}
