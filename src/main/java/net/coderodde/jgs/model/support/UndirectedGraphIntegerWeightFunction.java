package net.coderodde.jgs.model.support;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static net.coderodde.jgs.Utilities.checkNotInfinite;
import static net.coderodde.jgs.Utilities.checkNotNaN;
import static net.coderodde.jgs.Utilities.checkNotNull;
import net.coderodde.jgs.model.AbstractWeightFunction;
import net.coderodde.jgs.model.Graph;
import net.coderodde.jgs.model.Path;

/**
 * This class implements an integer-valued weight function over an undirected 
 * graph.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class UndirectedGraphIntegerWeightFunction 
extends AbstractWeightFunction<UndirectedGraphNode, Integer>{

    /**
     * {@inheritDoc}
     * 
     * @param from the tail node of the edge.
     * @param to the head node of the edge.
     * @param weight the weight to associate with the edge
     * <code>(from, to)</code>.
     */
    @Override
    public void put(UndirectedGraphNode from, 
                    UndirectedGraphNode to, 
                    Integer weight) {
        checkNotNull(from, "The tail node is null.");
        checkNotNull(to, "The head node is null.");
        checkNotNull(weight, "The weight is null.");
        
        
        
        checkNotInfinite(weight, "The weight is infinite: " + weight);
        
        checkNotNaN(weight, "The weight is NaN.");
        
        Map<UndirectedGraphNode, Integer> partialMap = map.get(from);
        
        if (partialMap == null) {
            partialMap = new HashMap<>();
            partialMap.put(to, weight);
            map.put(from, partialMap);
        } else {
            partialMap.put(to, weight);
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
    public Integer get(UndirectedGraphNode from, UndirectedGraphNode to) {
        checkNotNull(from, "The tail node (from) is null.");
        checkNotNull(to, "The head node (to) is null.");
        
        // try from -> to -> weight.
        Map<UndirectedGraphNode, Integer> partialMap = map.get(from);
        final Integer weight;
        
        if (partialMap == null) {
            // Try to -> from -> weight.
            partialMap = map.get(to);
            
            if (partialMap == null) {
                throw new IllegalStateException("The weight not found.");
            }
            
            weight = partialMap.get(from);
            
            if (weight == null) {
                throw new IllegalStateException("The weight not found.");
            }
        } else {
            weight = partialMap.get(to);
            
            if (weight == null) {
                throw new IllegalStateException("The weight not found.");
            }
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
    public Integer getPathWeight(Path<UndirectedGraphNode> path) {
        final Iterator<UndirectedGraphNode> iterator = path.iterator();
        
        if (iterator.hasNext() == false) {
            return 0;
        }
        
        UndirectedGraphNode u = iterator.next();
        
        if (iterator.hasNext() == false) {
            return 0;
        }
        
        UndirectedGraphNode v = iterator.next();
        
        int weight = get(u, v);
        
        while (iterator.hasNext()) {
            u = v;
            v = iterator.next();
            weight += get(u, v);
        }
        
        return weight;
    }
}
