package net.coderodde.jgs.model.support;

import java.util.ArrayList;
import java.util.List;
import net.coderodde.jgs.model.Graph;
import net.coderodde.jgs.model.Path;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UndirectedGraphDoubleWeightFunctionTest {
    
    private static final double e = 0.0001;
    
    private final UndirectedGraphDoubleWeightFunction f;
    private final Graph<UndirectedGraphNode> graph;
    private final UndirectedGraphNode a;
    private final UndirectedGraphNode b;
    private final UndirectedGraphNode c;
    
    public UndirectedGraphDoubleWeightFunctionTest() {
        graph = new Graph<>();
        f     = new UndirectedGraphDoubleWeightFunction();
        a     = new UndirectedGraphNode();
        b     = new UndirectedGraphNode();
        c     = new UndirectedGraphNode();
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
        assertEquals(10.5, f.get(b, a), e);
        
        f.put(b, c, 3.42);
        assertEquals(3.42, f.get(b, c), e);
        assertEquals(3.42, f.get(c, b), e);
    }

    @Test
    public void testGetPathWeight() {
        final List<UndirectedGraphNode> list = new ArrayList<>();
        
        list.add(a);
        list.add(b);
        list.add(c);
        
        a.connectTo(b);
        b.connectTo(c);
        
        Path<UndirectedGraphNode> path = new Path<>(list);
        
        f.put(a, b, 2.4);
        f.put(b, c, 4.6);
        
        assertEquals(7.0, f.getPathWeight(path), e);
        
        a.clear();
        b.clear();
        c.clear();
        
        b.connectTo(a);
        c.connectTo(b);
        
        path = new Path<>(list);
        
        assertEquals(7.0, f.getPathWeight(path), e);
    }
    
    @Test
    public void testEdgeIsBidirectional() {
        f.put(a, b, 3.5);
        assertEquals(3.5, f.get(a, b), e);
        assertEquals(3.5, f.get(b, a), e);
        
        f.put(b, a, 1.7);
        assertEquals(1.7, f.get(b, a), e);
        assertEquals(1.7, f.get(a, b), e);
        
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        
        a.connectTo(b);
        c.connectTo(b);
        
        f.put(c, b, 4.5);
        f.put(b, c, 3.6);
        
        final List<UndirectedGraphNode> list = new ArrayList<>();
        
        list.add(a);
        list.add(b);
        list.add(c);
        
        final Path<UndirectedGraphNode> path = new Path<>(list);
        
        assertEquals(5.3, f.getPathWeight(path), e);
    }
}
