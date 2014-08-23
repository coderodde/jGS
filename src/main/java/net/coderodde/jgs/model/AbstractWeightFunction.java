package net.coderodde.jgs.model;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWeightFunction<T extends AbstractNode<T>, 
                                             W extends Comparable<? super W>> {

    /**
     * The actual map.
     */
    protected final Map<T, Map<T, W>> map;
    
    /**
     * Initializes the map.
     */
    protected AbstractWeightFunction() {
        this.map = new HashMap<>();
    }
    
    /**
     * Associates the weight <code>weight</code> with the edge
     * <code>(from, to)</code>.
     * 
     * @param from the tail of the edge.
     * @param to the head of the edge.
     * @param weight the weight to associates.
     */
    public abstract void put(final T from, final T to, final W weight);
    
    /**
     * Returns the weight associated with the edge <code>(from, to)</code>, or
     * throws {@link java.lang.IllegalStateException} if there is no weight 
     * associated with that edge.
     * 
     * @param from the tail node.
     * @param to the head node.
     * 
     * @return the weight associated with edge <code>(from, to)</code>.
     * 
     * @throws java.lang.IllegalStateException in case there is no weight 
     * associated.
     */
    public abstract W get(final T from, final T to);
    
    /**
     * Computes and returns the weight of a path.
     * 
     * @param path the path whose weight to compute.
     * 
     * @return the weight of the specified path.
     */
    public abstract W getPathWeight(final Path<T> path);
}
