package net.coderodde.jgs.model.support;

import java.util.ArrayList;
import java.util.List;
import net.coderodde.jgs.model.Graph;
import net.coderodde.jgs.model.Path;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UndirectedGraphIntegerWeightFunctionTest {
    
    private static final double e = 0.0001;
    
    private final UndirectedGraphIntegerWeightFunction f;
    private final Graph<UndirectedGraphNode> graph;
    private final UndirectedGraphNode a;
    private final UndirectedGraphNode b;
    private final UndirectedGraphNode c;
    
    public UndirectedGraphIntegerWeightFunctionTest() {
        graph = new Graph<>();
        f     = new UndirectedGraphIntegerWeightFunction();
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
        f.put(a, b, 10);
        assertEquals(10, f.get(a, b), e);
        assertEquals(10, f.get(b, a), e);
        
        f.put(b, c, 3);
        assertEquals(3, f.get(b, c), e);
        assertEquals(3, f.get(c, b), e);
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
        
        f.put(a, b, 4);
        f.put(b, c, 6);
        
        assertEquals(10, f.getPathWeight(path), e);
        
        a.clear();
        b.clear();
        c.clear();
        
        b.connectTo(a);
        c.connectTo(b);
        
        path = new Path<>(list);
        
        assertEquals(10, f.getPathWeight(path), e);
    }
    
    @Test
    public void testEdgeIsBidirectional() {
        f.put(a, b, 5);
        assertEquals(5, f.get(a, b), e);
        assertEquals(5, f.get(b, a), e);
        
        f.put(b, a, 7);
        assertEquals(7, f.get(b, a), e);
        assertEquals(7, f.get(a, b), e);
        
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        
        a.connectTo(b);
        c.connectTo(b);
        
        f.put(c, b, 5);
        f.put(b, c, 9);
        
        final List<UndirectedGraphNode> list = new ArrayList<>();
        
        list.add(a);
        list.add(b);
        list.add(c);
        
        final Path<UndirectedGraphNode> path = new Path<>(list);
        
        assertEquals(16, f.getPathWeight(path), e);
    }
}
