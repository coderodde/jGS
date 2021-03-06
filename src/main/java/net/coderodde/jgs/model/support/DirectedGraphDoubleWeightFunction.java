package net.coderodde.jgs.model.support;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static net.coderodde.jgs.Utilities.checkBelongsToGraph;
import static net.coderodde.jgs.Utilities.checkNotInfinite;
import static net.coderodde.jgs.Utilities.checkNotNaN;
import static net.coderodde.jgs.Utilities.checkNotNull;
import net.coderodde.jgs.model.AbstractWeightFunction;
import net.coderodde.jgs.model.Graph;
import net.coderodde.jgs.model.Path;

/**
 * This class implements a real-valued weight function over a directed graph.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class DirectedGraphDoubleWeightFunction 
extends AbstractWeightFunction<DirectedGraphNode, Double> {

    /**
     * {@inheritDoc}
     * 
     * @param from the tail node of the edge.
     * @param to the head node of the edge.
     * @param weight the weight to associate with the edge
     * <code>(from, to)</code>.
     */
    @Override
    public void put(DirectedGraphNode from, 
                    DirectedGraphNode to, 
                    Double weight) {
        checkNotNull(from, "The tail node is null.");
        checkNotNull(to, "The head node is null.");
        checkNotNull(weight, "The weight is null.");
        
        checkNotInfinite(weight, "The weight is infinite: " + weight);
        
        checkNotNaN(weight, "The weight is NaN.");
        
        Map<DirectedGraphNode, Double> partialMap = map.get(from);
        
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
    public Double get(DirectedGraphNode from, DirectedGraphNode to) {
        checkNotNull(from, "The tail node (from) is null.");
        checkNotNull(to, "The head node (to) is null.");
        
        Map<DirectedGraphNode, Double> partialMap = map.get(from);
        
        if (partialMap == null) {
            throw new IllegalStateException(
                    from + " is not mapped to a partial map.");
        }
        
        final Double weight = partialMap.get(to);
        
        if (weight == null) {
            throw new IllegalStateException(to + " is not mapped to a weight.");
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
    public Double getPathWeight(Path<DirectedGraphNode> path) {
        final Iterator<DirectedGraphNode> iterator = path.iterator();
        
        if (iterator.hasNext() == false) {
            return 0.0;
        }
        
        DirectedGraphNode u = iterator.next();
        
        if (iterator.hasNext() == false) {
            return 0.0;
        }
        
        DirectedGraphNode v = iterator.next();
        
        double weight = get(u, v);
        
        while (iterator.hasNext()) {
            u = v;
            v = iterator.next();
            weight += get(u, v);
        }
        
        return weight;
    }
}
