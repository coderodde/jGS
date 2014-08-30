package net.coderodde.jgs.model;

import net.coderodde.jgs.model.support.DirectedGraphNode;
import net.coderodde.jgs.model.support.UndirectedGraphNode;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GraphTest {
    
    // Undirected graph stuff.
    private final UndirectedGraphNode a;
    private final UndirectedGraphNode b;
    private final UndirectedGraphNode c;
    
    private final Graph<UndirectedGraphNode> g1;
    private final Graph<UndirectedGraphNode> g2;
    
    // Directed graph stuff.
    private final DirectedGraphNode aa;
    private final DirectedGraphNode bb;
    private final DirectedGraphNode cc;
    
    private final Graph<DirectedGraphNode> gg1;
    private final Graph<DirectedGraphNode> gg2;
    
    public GraphTest() {
        this.a = new UndirectedGraphNode("A");
        this.b = new UndirectedGraphNode("B");
        this.c = new UndirectedGraphNode("C");
        
        this.g1 = new Graph<>("G1");
        this.g2 = new Graph<>("G2");
        
        this.aa = new DirectedGraphNode("AA");
        this.bb = new DirectedGraphNode("BB");
        this.cc = new DirectedGraphNode("CC");
        
        this.gg1 = new Graph<>("GG1");
        this.gg2 = new Graph<>("GG2");
    }
    
    @Before
    public void setUp() {
        g1.clear();
        g2.clear();
        
        gg1.clear();
        gg2.clear();
    }
    
    @Test
    public void testAddNodeUndirected() {
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
        assertEquals(0, g2.edgeCount());
    }
    
    @Test
    public void testAddNodeDirected() {
        assertEquals(0, gg1.size());
        assertEquals(0, gg1.edgeCount());
        
        gg1.addNode(aa);
        
        assertEquals(1, gg1.size());
        assertEquals(0, gg1.edgeCount());
        
        gg1.addNode(bb);
        
        assertEquals(2, gg1.size());
        assertEquals(0, gg1.edgeCount());
        
        aa.connectTo(bb); // 1 edge in the graph.
        
        assertEquals(2, gg1.size());
        assertEquals(1, gg1.edgeCount());
        
        gg1.addNode(cc);
        
        assertEquals(3, gg1.size());
        assertEquals(1, gg1.edgeCount());
        
        bb.connectTo(cc); // 2 edges.
        
        assertEquals(3, gg1.size());
        assertEquals(2, gg1.edgeCount());
        
        aa.connectTo(cc); // 3 edges.
        
        assertEquals(3, gg1.size());
        assertEquals(3, gg1.edgeCount());
        
        gg2.addNode(bb); // lost 2 edges in gg1.
        
        assertEquals(2, gg1.size());
        assertEquals(1, gg2.size());
        assertEquals(1, gg1.edgeCount());
        assertEquals(0, gg2.edgeCount());
        
        bb.connectTo(bb);  // 2 edges.
       
        assertEquals(2, gg1.size());
        assertEquals(1, gg2.size());
        assertEquals(1, gg1.edgeCount());
        assertEquals(1, gg2.edgeCount());
        
        gg1.clear();
        
        gg1.addNode(aa);
        
        assertEquals(1, gg1.size());
        assertEquals(0, gg1.edgeCount());
        
        aa.connectTo(aa);
        
        assertEquals(1, gg1.size());
        assertEquals(1, gg1.edgeCount());
        
        gg1.addNode(cc);
        
        assertEquals(2, gg1.size());
        assertEquals(1, gg1.edgeCount());
        
        cc.connectTo(cc);
        
        assertEquals(2, gg1.size());
        assertEquals(2, gg1.edgeCount());
        
        cc.connectTo(aa);
        
        assertEquals(2, gg1.size());
        assertEquals(3, gg1.edgeCount());
        
        aa.connectTo(cc);
        
        assertEquals(2, gg1.size());
        assertEquals(4, gg1.edgeCount());
    }

    @Test
    public void testRemoveNodeUndirected() {
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
        assertEquals(0, g2.edgeCount());
    }

    @Test
    public void testRemoveNodeDirected() {
        gg1.addNode(aa);
        gg1.addNode(bb);
        gg1.addNode(cc);
        
        assertEquals(3, gg1.size());
        assertEquals(0, gg1.edgeCount());
        
        aa.connectTo(bb);
        bb.connectTo(cc);
        cc.connectTo(aa);
        
        assertEquals(3, gg1.size());
        assertEquals(3, gg1.edgeCount());
        
        gg1.removeNode(aa);
        
        assertEquals(2, gg1.size());
        assertEquals(1, gg1.edgeCount());
        
        gg1.addNode(aa);
        
        aa.connectTo(bb);
        bb.connectTo(cc);
        cc.connectTo(aa);
        
        assertEquals(3, gg1.size());
        assertEquals(3, gg1.edgeCount());
        
        gg2.addNode(bb);
        
        assertEquals(2, gg1.size());
        assertEquals(1, gg1.edgeCount());
        
        assertEquals(1, gg2.size());
        assertEquals(0, gg2.edgeCount());
        
        bb.connectTo(bb);
        
        assertEquals(2, gg1.size());
        assertEquals(1, gg1.edgeCount());
        
        assertEquals(1, gg2.size());
        assertEquals(1, gg2.edgeCount());
    }

    @Test
    public void testClearUndirected() {
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

    @Test
    public void testClearDirected() {
        assertEquals(0, gg1.size());
        assertEquals(0, gg1.edgeCount());
        
        gg1.addNode(aa);
        gg1.addNode(bb);
        
        assertEquals(2, gg1.size());
        assertEquals(0, gg1.edgeCount());
        
        bb.connectTo(aa);
        
        assertEquals(2, gg1.size());
        assertEquals(1, gg1.edgeCount());
        
        gg1.clear();
        
        assertEquals(0, gg1.size());
        assertEquals(0, gg1.size());
        
        gg1.clear();
        
        assertEquals(0, gg1.size());
        assertEquals(0, gg1.size());
    }

    @Test
    public void testSizeUndirected() {
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

    @Test
    public void testSizeDirected() {
        assertEquals(0, gg1.size());
        assertEquals(0, gg1.size());

        // {a}
        gg1.addNode(aa);
        
        assertEquals(1, gg1.size());
        
        // {a, b}
        gg1.addNode(bb);
        
        assertEquals(2, gg1.size());
        
        // {a, b, c}
        gg1.addNode(cc);
        
        assertEquals(3, gg1.size());
        
        // {a, c}
        gg1.removeNode(bb);
        
        assertEquals(2, gg1.size());
        
        // {c}
        gg1.removeNode(aa);
        
        assertEquals(1, gg1.size());
        
        // {c}
        gg1.removeNode(aa); // Remove the node that is no longer there.
        
        assertEquals(1, gg1.size()); 
        
        // {}
        gg1.removeNode(cc);
        
        assertEquals(0, gg1.size());
        
        // {}
        gg1.removeNode(bb);
        
        assertEquals(0, gg1.size());
    }

    @Test
    public void testEdgeCountUndirected() {
        assertEquals(0, g1.edgeCount());
        
        // {c} {empty edge set}
        g1.addNode(c);
        
        assertEquals(0, g1.edgeCount());
        
        // {c} {} self-loop not allowed.
        c.connectTo(c);
        
        assertEquals(0, g1.edgeCount());
        
        // {c, b} {}
        g1.addNode(b);
        
        assertEquals(0, g1.edgeCount());

        // {c, b} {(b, c)}
        b.connectTo(c);
        
        assertEquals(1, g1.edgeCount());
        
        // {c, b} {(b, c)}
        b.connectTo(b);
        
        assertEquals(1, g1.edgeCount());
        
        // {c, b, a} {(b, c)}
        g1.addNode(a);
        
        assertEquals(1, g1.edgeCount());
        
        // {c, b, a} {(b, c), (a, b)}
        a.connectTo(b);
        
        assertEquals(2, g1.edgeCount());
        
        // {c, b, a} {(b, c), (a, b), (a, c)}
        a.connectTo(c);
        
        assertEquals(3, g1.edgeCount());
        
        // {c, b, a} {(b, c), (a, b), (a, c)}
        a.connectTo(a);
        
        assertEquals(3, g1.edgeCount());
        
        // {c, a} {(a, c)}
        g1.removeNode(b);
        
        assertEquals(1, g1.edgeCount());
        
        // {c, a} {(a, c)}
        a.disconnectFrom(a);
        
        assertEquals(1, g1.edgeCount());
        
        // {c} {(c, c)}
        g1.removeNode(a);
        
        assertEquals(0, g1.edgeCount());
        
        // {} {}
        g1.clear();
        
        assertEquals(0, g1.edgeCount());
        
        g1.clear();
        
        assertEquals(0, g1.edgeCount());
    }

    @Test
    public void testEdgeCountDirected() {
        assertEquals(0, gg1.edgeCount());
        
        // {c} {empty edge set}
        gg1.addNode(cc);
        
        assertEquals(0, gg1.edgeCount());
        
        // {c} {(c, c)}
        cc.connectTo(cc);
        
        assertEquals(1, gg1.edgeCount());
        
        // {c, b} {(c, c)}
        gg1.addNode(bb);
        
        assertEquals(1, gg1.edgeCount());

        // {c, b} {(c, c), (b, c)}
        bb.connectTo(cc);
        
        assertEquals(2, gg1.edgeCount());
        
        // {c, b} {(c, c), (b, c), (b, b)}
        bb.connectTo(bb);
        
        assertEquals(3, gg1.edgeCount());
        
        // {c, b, a} {(c, c), (b, c), (b, b)}
        gg1.addNode(aa);
        
        assertEquals(3, gg1.edgeCount());
        
        // {c, b, a} {(c, c), (b, c), (b, b), (a, b)}
        aa.connectTo(bb);
        
        assertEquals(4, gg1.edgeCount());
        
        // {c, b, a} {(c, c), (b, c), (b, b), (a, b), (a, c)}
        aa.connectTo(cc);
        
        assertEquals(5, gg1.edgeCount());
        
        // {c, b, a} {(c, c), (b, c), (b, b), (a, b), (a, c), (a, a)}
        aa.connectTo(aa);
        
        assertEquals(6, gg1.edgeCount());
        
        // {c, a} {(c, c), (a, c), (a, a)}
        gg1.removeNode(bb);
        
        assertEquals(3, gg1.edgeCount());
        
        // {c, a} {(c, c), (a, c)}
        aa.disconnectFrom(aa);
        
        assertEquals(2, gg1.edgeCount());
        
        // {c} {(c, c)}
        gg1.removeNode(aa);
        
        assertEquals(1, gg1.edgeCount());
        assertEquals(1, gg1.size());
        
        // {} {}
        gg1.clear();
        
        assertEquals(0, gg1.size());
        assertEquals(0, gg1.edgeCount());
        
        gg1.clear();
        
        assertEquals(0, gg1.size());
        assertEquals(0, gg1.edgeCount());
    }
    
    @Test(expected = NullPointerException.class) 
    public void graphThrowsNullExceptionOnNullName() {
        new Graph<DirectedGraphNode>(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void graphThrowsNullExceptionOnAddingNullNode() {
        new Graph<UndirectedGraphNode>("graph").addNode(null);
    }

    @Test(expected = NullPointerException.class)
    public void graphThrowsNullExceptionOnRemovingNullNode() {
        new Graph<UndirectedGraphNode>("graph").removeNode(null);
    }
}
