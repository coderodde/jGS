package net.coderodde.jgs.model;

import net.coderodde.jgs.model.support.UndirectedGraphNode;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests <code>net.coderodde.jgs.model.Graph</code>.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class GraphTest {
    
    private final UndirectedGraphNode a;
    private final UndirectedGraphNode b;
    private final UndirectedGraphNode c;
    
    private final Graph<UndirectedGraphNode> g1;
    private final Graph<UndirectedGraphNode> g2;
    
    public GraphTest() {
        this.a = new UndirectedGraphNode("A");
        this.b = new UndirectedGraphNode("B");
        this.c = new UndirectedGraphNode("C");
        
        this.g1 = new Graph<>("G1");
        this.g2 = new Graph<>("G2");
    }
    
    @Before
    public void setUp() {
        g1.clear();
        g2.clear();
    }
    
    /**
     * Test of addNode method, of class Graph.
     */
    @Test
    public void testAddNode() {
        assertEquals(0, g1.size());
        assertEquals(0, g1.edgeCount());
        
        g1.addNode(a);
        
        assertEquals(1, g1.size());
        assertEquals(0, g1.edgeCount());
        
        g1.addNode(b);
        
        assertEquals(2, g1.size());
        assertEquals(0, g1.edgeCount());
        
        a.connectTo(b);
        
        assertEquals(2, g1.size());
        assertEquals(1, g1.edgeCount());
        
        g1.addNode(c);
        
        assertEquals(3, g1.size());
        assertEquals(1, g1.edgeCount());
        
        b.connectTo(c);
        
        assertEquals(3, g1.size());
        assertEquals(2, g1.edgeCount());
        
        a.connectTo(c);
        
        assertEquals(3, g1.size());
        assertEquals(3, g1.edgeCount());
        
        g2.addNode(b);
        
        assertEquals(2, g1.size());
        assertEquals(1, g2.size());
        assertEquals(1, g1.edgeCount());
        assertEquals(0, g2.edgeCount());
        
        b.connectTo(b);
       
        assertEquals(2, g1.size());
        assertEquals(1, g2.size());
        assertEquals(1, g1.edgeCount());
        assertEquals(1, g2.edgeCount());
    }

    /**
     * Test of removeNode method, of class Graph.
     */
    @Test
    public void testRemoveNode() {
        g1.addNode(a);
        g1.addNode(b);
        g1.addNode(c);
        
        assertEquals(3, g1.size());
        assertEquals(0, g1.edgeCount());
        
        a.connectTo(b);
        b.connectTo(c);
        c.connectTo(a);
        
        assertEquals(3, g1.size());
        assertEquals(3, g1.edgeCount());
        
        g1.removeNode(a);
        
        assertEquals(2, g1.size());
        assertEquals(1, g1.edgeCount());
        
        g1.addNode(a);
        
        a.connectTo(b);
        b.connectTo(c);
        c.connectTo(a);
        
        assertEquals(3, g1.size());
        assertEquals(3, g1.edgeCount());
        
        g2.addNode(b);
        
        assertEquals(2, g1.size());
        assertEquals(1, g1.edgeCount());
        
        assertEquals(1, g2.size());
        assertEquals(0, g2.edgeCount());
        
        b.connectTo(b);
        
        assertEquals(2, g1.size());
        assertEquals(1, g1.edgeCount());
        
        assertEquals(1, g2.size());
        assertEquals(1, g2.edgeCount());
    }

    /**
     * Test of clear method, of class Graph.
     */
    @Test
    public void testClear() {
        assertEquals(0, g1.size());
        assertEquals(0, g1.edgeCount());
        
        g1.addNode(a);
        g1.addNode(b);
        
        assertEquals(2, g1.size());
        assertEquals(0, g1.edgeCount());
        
        b.connectTo(a);
        
        assertEquals(2, g1.size());
        assertEquals(1, g1.edgeCount());
        
        g1.clear();
        
        assertEquals(0, g1.size());
        assertEquals(0, g1.size());
        
        g1.clear();
        
        assertEquals(0, g1.size());
        assertEquals(0, g1.size());
    }

    /**
     * Test of size method, of class Graph.
     */
    @Test
    public void testSize() {
        assertEquals(0, g1.size());
        assertEquals(0, g1.size());

        // {a}
        g1.addNode(a);
        
        assertEquals(1, g1.size());
        
        // {a, b}
        g1.addNode(b);
        
        assertEquals(2, g1.size());
        
        // {a, b, c}
        g1.addNode(c);
        
        assertEquals(3, g1.size());
        
        // {a, c}
        g1.removeNode(b);
        
        assertEquals(2, g1.size());
        
        // {c}
        g1.removeNode(a);
        
        assertEquals(1, g1.size());
        
        // {c}
        g1.removeNode(a); // Remove the node that is no longer there.
        
        assertEquals(1, g1.size()); 
        
        // {}
        g1.removeNode(c);
        
        assertEquals(0, g1.size());
        
        // {}
        g1.removeNode(b);
        
        assertEquals(0, g1.size());
    }

    /**
     * Test of edgeCount method, of class Graph.
     */
    @Test
    public void testEdgeCount() {
        assertEquals(0, g1.edgeCount());
        
        // {c} {empty edge set}
        g1.addNode(c);
        
        assertEquals(0, g1.edgeCount());
        
        // {c} {(c, c)}
        c.connectTo(c);
        
        assertEquals(1, g1.edgeCount());
        
        // {c, b} {(c, c)}
        g1.addNode(b);
        
        assertEquals(1, g1.edgeCount());

        // {c, b} {(c, c), (b, c)}
        b.connectTo(c);
        
        assertEquals(2, g1.edgeCount());
        
        // {c, b} {(c, c), (b, c), (b, b)}
        b.connectTo(b);
        
        assertEquals(3, g1.edgeCount());
        
        // {c, b, a} {(c, c), (b, c), (b, b)}
        g1.addNode(a);
        
        assertEquals(3, g1.edgeCount());
        
        // {c, b, a} {(c, c), (b, c), (b, b), (a, b)}
        a.connectTo(b);
        
        assertEquals(4, g1.edgeCount());
        
        // {c, b, a} {(c, c), (b, c), (b, b), (a, b), (a, c)}
        a.connectTo(c);
        
        assertEquals(5, g1.edgeCount());
        
        // {c, b, a} {(c, c), (b, c), (b, b), (a, b), (a, c), (a, a)}
        a.connectTo(a);
        
        assertEquals(6, g1.edgeCount());
        
        // {c, a} {(c, c), (a, c), (a, a)}
        g1.removeNode(b);
        
        assertEquals(3, g1.edgeCount());
        
        // {c, a} {(c, c), (a, c)}
        a.disconnectFrom(a);
        
        assertEquals(2, g1.edgeCount());
        
        // {c} {(c, c)}
        g1.removeNode(a);
        
        assertEquals(1, g1.edgeCount());
        
        // {} {}
        g1.clear();
        
        assertEquals(0, g1.edgeCount());
        
        g1.clear();
        
        assertEquals(0, g1.edgeCount());
    }
}
