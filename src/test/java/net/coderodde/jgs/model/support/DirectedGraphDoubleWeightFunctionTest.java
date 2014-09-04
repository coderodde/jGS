package net.coderodde.jgs.model.support;

import java.util.ArrayList;
import java.util.List;
import net.coderodde.jgs.model.Graph;
import net.coderodde.jgs.model.Path;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DirectedGraphDoubleWeightFunctionTest {
    
    private static final double e = 0.0001;
    
    private final DirectedGraphDoubleWeightFunction f;
    private final Graph<DirectedGraphNode> graph;
    private final DirectedGraphNode a;
    private final DirectedGraphNode b;
    private final DirectedGraphNode c;
    
    public DirectedGraphDoubleWeightFunctionTest() {
        graph = new Graph<>();
        f     = new DirectedGraphDoubleWeightFunction();
        a     = new DirectedGraphNode();
        b     = new DirectedGraphNode();
        c     = new DirectedGraphNode();
    }
    
    @Before
    public void setUp() {
        f.clear();
        graph.clear();
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
    }

    @Test
    public void testPutAndGet() {
        f.put(a, b, 10.5);
        assertEquals(10.5, f.get(a, b), e);
        f.put(b, c, 3.42);
        assertEquals(3.42, f.get(b, c), e);
    }

    @Test
    public void testGetPathWeight() {
        final List<DirectedGraphNode> list = new ArrayList<>();
        
        list.add(a);
        list.add(b);
        list.add(c);
        
        a.connectTo(b);
        b.connectTo(c);
        
        final Path<DirectedGraphNode> path = new Path<>(list);
        
        f.put(a, b, 2.4);
        f.put(b, c, 4.6);
        
        assertEquals(7.0, f.getPathWeight(path), e);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testThrowsOnAntisymmetricEdge() {
        f.put(a, b, 4.0);
        assertEquals(f.get(a, b), 4.0, e);
        f.get(b, a);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPathThrowsOnDisconnectedEdge() {
        f.put(a, b, 3.5);
        assertEquals(f.get(a, b), 3.5, e);
        
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        
        a.connectTo(b);
        c.connectTo(b); // the antiparallel edge of the required edge.
        
        f.put(c, b, 4.5);
        f.put(b, c, 3.6);
        
        final List<DirectedGraphNode> list = new ArrayList<>();
        
        list.add(a);
        list.add(b);
        list.add(c);
        
        f.getPathWeight(new Path<>(list));
    }
}
