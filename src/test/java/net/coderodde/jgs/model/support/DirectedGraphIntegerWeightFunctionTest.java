package net.coderodde.jgs.model.support;

import java.util.ArrayList;
import java.util.List;
import net.coderodde.jgs.model.Graph;
import net.coderodde.jgs.model.Path;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DirectedGraphIntegerWeightFunctionTest {
    
    private final DirectedGraphIntegerWeightFunction f;
    private final Graph<DirectedGraphNode> graph;
    private final DirectedGraphNode a;
    private final DirectedGraphNode b;
    private final DirectedGraphNode c;
    
    public DirectedGraphIntegerWeightFunctionTest() {
        graph = new Graph<>();
        f     = new DirectedGraphIntegerWeightFunction();
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
        f.put(a, b, 10);
        assertEquals((Integer) 10, f.get(a, b));
        f.put(b, c, 3);
        assertEquals((Integer) 3, f.get(b, c));
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
        
        f.put(a, b, 2);
        f.put(b, c, 4);
        
        assertEquals((Integer) 6, f.getPathWeight(path));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testThrowsOnAntisymmetricEdge() {
        f.put(a, b, 4);
        assertEquals(f.get(a, b), (Integer) 4);
        f.get(b, a);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPathThrowsOnDisconnectedEdge() {
        f.put(a, b, 5);
        assertEquals(f.get(a, b), (Integer) 5);
        
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        
        a.connectTo(b);
        c.connectTo(b); // the antiparallel edge of the required edge.
        
        f.put(c, b, 4);
        f.put(b, c, 3);
        
        final List<DirectedGraphNode> list = new ArrayList<>();
        
        list.add(a);
        list.add(b);
        list.add(c);
        
        f.getPathWeight(new Path<>(list));
    }
}
