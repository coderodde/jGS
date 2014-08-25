package net.coderodde.jgs.model.support;

import java.util.Iterator;
import net.coderodde.jgs.model.Graph;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests <code>net.coderodde.jgs.model.DirectedGraphNode</code>.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class DirectedGraphNodeTest {
    
    private final DirectedGraphNode a;
    private final DirectedGraphNode b;
    private final DirectedGraphNode c;
    private final Graph<DirectedGraphNode> G1;
    private final Graph<DirectedGraphNode> G2;
    
    public DirectedGraphNodeTest() {
        a = new DirectedGraphNode("A");
        b = new DirectedGraphNode("B");
        c = new DirectedGraphNode("C");
        
        G1 = new Graph<>("G1");
        G2 = new Graph<>("G2");
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
        assertEquals(0, a.parentsListSize());
        
        assertEquals(0, b.childrenListSize());
        assertEquals(0, b.parentsListSize());
        
        assertEquals(0, c.childrenListSize());
        assertEquals(1, c.parentsListSize());
        
        a.connectTo(b);
        
        assertEquals(2, a.childrenListSize());
        assertEquals(0, a.parentsListSize());
        
        assertEquals(0, b.childrenListSize());
        assertEquals(1, b.parentsListSize());
        
        assertEquals(0, c.childrenListSize());
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
        assertEquals(0, a.parentsListSize());
        
        assertTrue(a.isConnectedTo(b));
        assertFalse(b.isConnectedTo(a));
        
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
        assertFalse(b.isConnectedTo(a));
        
        b.disconnectFrom(a);
        
        assertTrue(a.isConnectedTo(b));
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
        
        assertFalse(a.isConnectedTo(c));
        assertTrue(c.isConnectedTo(a));
        
        assertFalse(a.isConnectedTo(b));
        assertFalse(b.isConnectedTo(a));
        
        a.disconnectFrom(c);
        
        assertFalse(a.isConnectedTo(b));
        assertFalse(b.isConnectedTo(a));
        
        assertFalse(a.isConnectedTo(a));
        assertTrue(c.isConnectedTo(a));
        
        c.disconnectFrom(a);
        
        assertFalse(a.isConnectedTo(c));
        assertFalse(c.isConnectedTo(a));
    }

    @Test
    public void testParents() {
        G1.addNode(a);
        G1.addNode(b);
        G1.addNode(c);
        
        Iterator<DirectedGraphNode> iterator = a.parents().iterator();
        
        assertFalse(iterator.hasNext());
        
        a.connectTo(b);
        
        iterator = a.parents().iterator();
        
        assertFalse(iterator.hasNext());
        
        c.connectTo(b);
        
        iterator = b.parents().iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), a);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), c);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator() {
        G1.addNode(a);
        G1.addNode(b);
        G1.addNode(c);
        
        Iterator<DirectedGraphNode> iterator = a.iterator();
        
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
        
        assertEquals(0, a.childrenListSize());
        assertEquals(0, b.childrenListSize());
        assertEquals(0, c.childrenListSize());
        
        a.connectTo(b);
        
        assertEquals(1, a.childrenListSize());
        
        a.connectTo(c);
        
        assertEquals(2, a.childrenListSize());
        
        a.connectTo(a);
        
        assertEquals(3, a.childrenListSize());
        
        a.disconnectFrom(b);
        
        assertEquals(2, a.childrenListSize());
        
        a.disconnectFrom(b);
        
        assertEquals(2, a.childrenListSize());
        
        a.disconnectFrom(c);
        
        assertEquals(1, a.childrenListSize());
        
        a.disconnectFrom(a);
        
        assertEquals(0, a.childrenListSize());
    }
    
    @Test
    public void testParentsListSize() {
        assertEquals(a.parentsListSize(), 0);
        
        G1.addNode(a);
        G1.addNode(b);
        G1.addNode(c);
        
        assertEquals(0, a.parentsListSize());
        
        a.connectTo(b);
        
        assertEquals(0, a.parentsListSize());
        
        b.connectTo(a);
        
        assertEquals(1, a.parentsListSize());
        
        c.connectTo(a);
        
        assertEquals(2, a.parentsListSize());
        
        a.disconnectFrom(b);
        
        assertEquals(2, a.parentsListSize());
        
        b.disconnectFrom(a);
        
        assertEquals(1, a.parentsListSize());
        
        c.disconnectFrom(a);
        
        assertEquals(0, a.parentsListSize());
    }

    @Test
    public void testToString() {
        assertEquals(a.toString(), "[DirectedGraphNode A]");
        assertEquals(b.toString(), "[DirectedGraphNode B]");
        assertEquals(c.toString(), "[DirectedGraphNode C]");
        
        assertEquals(new DirectedGraphNode("Funkeeh!").toString(),
                     "[DirectedGraphNode Funkeeh!]");
    }
    
    /**
     * Tests that constructing an DirectedGraphNode with null name throws
     * NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void nullNameThrows() {
        new DirectedGraphNode(null);
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
        a.connectTo(new DirectedGraphNode("tmp"));
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
