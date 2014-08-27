package net.coderodde.jgs.model.support;

import net.coderodde.jgs.model.AbstractHeuristicFunction;
import net.coderodde.jgs.model.AbstractNode;
import net.coderodde.jgs.model.GraphNodeCoordinates;

public class EuclidianDoubleHeuristicFunction<T extends AbstractNode<T>> 
extends AbstractHeuristicFunction<T, Double> {

    private final GraphNodeCoordinates<T> coordinateMap;
    
    public EuclidianDoubleHeuristicFunction(final GraphNodeCoordinates<T> cm) {
        this.coordinateMap = cm;
    }
    
    @Override
    public Double evaluate(T tail, T head) {
        return coordinateMap.get(tail).distance(coordinateMap.get(head));
    }
}
