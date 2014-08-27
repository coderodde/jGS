package net.coderodde.jgs.model;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class GraphNodeCoordinates<T extends AbstractNode<T>> {

    private final Map<T, Point2D.Double> map;
    
    public GraphNodeCoordinates() {
        map = new HashMap<>();
    }
    
    public void put(final T node, final Point2D.Double point) {
        map.put(node, point);
    }
    
    public Point2D.Double get(final T node) {
        return map.get(node);
    }
}
