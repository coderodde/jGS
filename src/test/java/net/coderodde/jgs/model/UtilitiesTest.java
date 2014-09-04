package net.coderodde.jgs.model;

import net.coderodde.jgs.Utilities;
import net.coderodde.jgs.model.support.UndirectedGraphNode;
import org.junit.Test;

/**
 *
 * @author Rodion Efremov
 * @version 1.6
 */
public class UtilitiesTest {
 
    /**
     * Test of checkNotNull method, of class Utilities.
     */
    @Test(expected = NullPointerException.class)
    public void testCheckNotNull() {
        Utilities.checkNotNull(new Object(), "Message.");
        Utilities.checkNotNull(null, "Whoops!");
    }

    /**
     * Tests that non-null reference does not throw an exception.
     */
    @Test
    public void testCheckNotNull2() {
        Utilities.checkNotNull(new Object(), "Message.");
    }
    /**
     * Test of checkNodesBelongToSameGraph method, of class Utilities.
     */
    @Test(expected = IllegalStateException.class)
    public void testCheckNodesBelongToSameGraph() {
        UndirectedGraphNode a = new UndirectedGraphNode();
        UndirectedGraphNode b = new UndirectedGraphNode();
        
        Graph<UndirectedGraphNode> g1 = new Graph<>();
        Graph<UndirectedGraphNode> g2 = new Graph<>();
        
        g2.addNode(a);
        g1.addNode(b);
        
        Utilities.checkNodesBelongToSameGraph(a, b);
    }
    
    /**
     * Checks that the nodes in the same graph do not throw anything.
     */
    @Test
    public void testCheckNodesBelongToSameGraph2() {
        UndirectedGraphNode a = new UndirectedGraphNode();
        UndirectedGraphNode b = new UndirectedGraphNode();
        
        Graph<UndirectedGraphNode> g1 = new Graph<>();
        Graph<UndirectedGraphNode> g2 = new Graph<>();
        
        g2.addNode(a);
        g2.addNode(b);
        
        Utilities.checkNodesBelongToSameGraph(a, b);
    }
}
