package net.coderodde.jgs.model.support;

import java.util.Iterator;
import net.coderodde.jgs.model.Graph;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests <code>net.coderodde.jgs.model.UndirectedGraphNode</code>.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class UndirectedGraphNodeTest {
    
    private final UndirectedGraphNode a;
    private final UndirectedGraphNode b;
    private final UndirectedGraphNode c;
    private final Graph<UndirectedGraphNode> G1;
    private final Graph<UndirectedGraphNode> G2;
    
    public UndirectedGraphNodeTest() {
        a = new UndirectedGraphNode();
        b = new UndirectedGraphNode();
        c = new UndirectedGraphNode();
        
        G1 = new Graph<>();
        G2 = new Graph<>();
    }
    
    @Before
    public void setUp() {
        a.clear();
        b.clear();
        c.clear();
        
        G1.clear();
        G2.clear();
    }
    
    @Test
    public void testClear() {
        G1.addNode(a);
        G1.addNode(b);
        G1.addNode(c);
        
        assertEquals(0, a.childrenListSize());
        assertEquals(0, a.parentsListSize());
        
        assertEquals(0, b.childrenListSize());
        assertEquals(0, b.parentsListSize());
        
        assertEquals(0, c.childrenListSize());
        assertEquals(0, c.parentsListSize());
        
        a.connectTo(c);
        
        assertEquals(1, a.childrenListSize());
        assertEquals(1, a.parentsListSize());
        
        assertEquals(0, b.childrenListSize());
        assertEquals(0, b.parentsListSize());
        
        assertEquals(1, c.childrenListSize());
        assertEquals(1, c.parentsListSize());
        
        a.connectTo(b);
        
        assertEquals(2, a.childrenListSize());
        assertEquals(2, a.parentsListSize());
        
        assertEquals(1, b.childrenListSize());
        assertEquals(1, b.parentsListSize());
        
        assertEquals(1, c.childrenListSize());
        assertEquals(1, c.parentsListSize());
        
        a.clear();
        
        assertEquals(0, a.childrenListSize());
        assertEquals(0, a.parentsListSize());
    }

    @Test
    public void testConnectTo() {
        G1.addNode(a);
        G1.addNode(b);
        G1.addNode(c);
        
        assertEquals(0, a.childrenListSize());
        assertEquals(0, a.parentsListSize());
        
        a.connectTo(b);
        
        assertEquals(1, a.childrenListSize());
        assertEquals(1, a.parentsListSize());
        
        assertTrue(a.isConnectedTo(b));
        assertTrue(b.isConnectedTo(a));
        
        assertFalse(a.isConnectedTo(c));
        assertFalse(c.isConnectedTo(a));
        
        assertFalse(b.isConnectedTo(c));
        assertFalse(c.isConnectedTo(b));
    }

    @Test
    public void testIsConnectedTo() {
        G1.addNode(a);
        G1.addNode(b);
        G1.addNode(c);
        
        assertFalse(a.isConnectedTo(b));
        assertFalse(b.isConnectedTo(a));
        
        a.connectTo(b);
        
        assertTrue(a.isConnectedTo(b));
        assertTrue(b.isConnectedTo(a));
        
        b.disconnectFrom(a);
        
        assertFalse(a.isConnectedTo(b));
        assertFalse(b.isConnectedTo(a));
    }

    @Test
    public void testDisconnectFrom() {
        G1.addNode(a);
        G1.addNode(b);
        G1.addNode(c);
        
        assertFalse(a.isConnectedTo(c));
        assertFalse(c.isConnectedTo(a));
        
        c.connectTo(a);
        
        assertTrue(a.isConnectedTo(c));
        assertTrue(c.isConnectedTo(a));
        
        assertFalse(a.isConnectedTo(b));
        assertFalse(b.isConnectedTo(a));
        
        a.disconnectFrom(c);
        
        assertFalse(a.isConnectedTo(b));
        assertFalse(b.isConnectedTo(a));
        
        assertFalse(a.isConnectedTo(a));
        assertFalse(c.isConnectedTo(a));
    }

    @Test
    public void testParents() {
        G1.addNode(a);
        G1.addNode(b);
        G1.addNode(c);
        
        Iterator<UndirectedGraphNode> iterator = a.parents().iterator();
        
        assertFalse(iterator.hasNext());
        
        a.connectTo(b);
        
        iterator = a.parents().iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), b);
        assertFalse(iterator.hasNext());
        
        a.connectTo(c);
        
        iterator = a.parents().iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), b);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), c);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator() {
        G1.addNode(a);
        G1.addNode(b);
        G1.addNode(c);
        
        Iterator<UndirectedGraphNode> iterator = a.iterator();
        
        assertFalse(iterator.hasNext());
        
        a.connectTo(b);
        
        iterator = a.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), b);
        assertFalse(iterator.hasNext());
        
        a.connectTo(c);
        
        iterator = a.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), b);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), c);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testChildrenListSize() {
        assertEquals(a.childrenListSize(), 0);
        
        G1.addNode(a);
        G1.addNode(b);
        G1.addNode(c);
        
        assertEquals(a.childrenListSize(), 0);
        
        b.connectTo(a);
        
        assertEquals(a.childrenListSize(), 1);
        
        a.connectTo(c);
        
        assertEquals(a.childrenListSize(), 2);
        
        a.disconnectFrom(b);
        
        assertEquals(a.childrenListSize(), 1);
        
        c.disconnectFrom(a);
        
        assertEquals(a.childrenListSize(), 0);
    }
    
    @Test
    public void testParentsListSize() {
        assertEquals(a.parentsListSize(), 0);
        
        G1.addNode(a);
        G1.addNode(b);
        G1.addNode(c);
        
        assertEquals(a.parentsListSize(), 0);
        
        b.connectTo(a);
        
        assertEquals(a.parentsListSize(), 1);
        
        a.connectTo(c);
        
        assertEquals(a.parentsListSize(), 2);
        
        a.disconnectFrom(b);
        
        assertEquals(a.parentsListSize(), 1);
        
        c.disconnectFrom(a);
        
        assertEquals(a.parentsListSize(), 0);
    }
    
    @Test
    public void testToString() {
        assertTrue(a.toString().startsWith("[UndirectedGraphNode "));
        assertTrue(b.toString().startsWith("[UndirectedGraphNode "));
        assertTrue(c.toString().startsWith("[UndirectedGraphNode "));
        assertTrue(new UndirectedGraphNode()
                   .toString().startsWith("[UndirectedGraphNode "));
    }
    
    /**
     * Tests that a node not belonging to any graph cannot be connected to any
     * other node.
     */
    @Test(expected = NullPointerException.class) 
    public void connectingToNodeWhileHaveNoOwnerGraphThrows() {
        a.connectTo(b);
    }
    
    /**
     * Tests that a node belonging to a graph cannot be connected to null.
     */
    @Test(expected = NullPointerException.class)
    public void connectingToNullThrows() {
        G1.addNode(a);
        a.connectTo(null);
    }
    
    /**
     * Tests that a node belonging to a graph cannot be connected to
     * another node belonging to no graph.
     */
    @Test(expected = NullPointerException.class) 
    public void connectingToNonNullNodeHavingNoOwnerGraphThrows() {
        G1.addNode(a);
        a.connectTo(new UndirectedGraphNode());
    }
    
    /**
     * Tests that a node belonging to a graph cannot be connected to another 
     * node belonging to another graph.
     */
    @Test(expected = IllegalStateException.class) 
    public void connectingToNodeFromAnotherGraphThrows() {
        G1.addNode(a);
        G2.addNode(b);
        b.connectTo(a);
    }
    
    /**
     * Tests that connectTo throws NullPointerException whenever the argument
     * is null.
     */
    @Test(expected = NullPointerException.class)
    public void onIsConnectedToWithNullChildThrows() {
        G1.addNode(a);
        a.connectTo(null);
    }
    
    /**
     * Tests that disconnectFrom throws NullPointerException whenever the
     * argument is null.
     */
    @Test(expected = NullPointerException.class) 
    public void onDisconnectFromNullThrows() {
        G1.addNode(a);
        a.disconnectFrom(null);
    }
}
