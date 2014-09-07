package net.coderodde.jgs.model.support;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

public class PuzzleNodeTest {

    @Test
    public void testEquals() {
        PuzzleNode u = new PuzzleNode(new byte[][]{
            {1, 2, 3},
            {4, 0, 5},
            {6, 7, 8}
        });
        
        assertEquals(u, new PuzzleNode(new byte[][]{
            {1, 2, 3},
            {4, 0, 5},
            {6, 7, 8}
        }));
        
        assertFalse(u.equals(u.moveDown()));
        assertTrue(u != u.moveUp());
        assertTrue(u != u.moveRight());
        assertTrue(u != u.moveDown());
        assertTrue(u != u.moveLeft());
    }

    @Test
    public void testGet() {
        PuzzleNode u = new PuzzleNode(new byte[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 0, 8}
        });
        
        assertEquals(1, u.get(0, 0));
        assertEquals(2, u.get(1, 0));
        assertEquals(3, u.get(2, 0));
        assertEquals(4, u.get(0, 1));
        assertEquals(5, u.get(1, 1));
        assertEquals(6, u.get(2, 1));
        assertEquals(7, u.get(0, 2));
        assertEquals(0, u.get(1, 2));
        assertEquals(8, u.get(2, 2));
    }
    
    @Test
    public void testInitialNode() {
        PuzzleNode u = new PuzzleNode(3);
        
        assertEquals(1, u.get(0, 0));
        assertEquals(2, u.get(1, 0));
        assertEquals(3, u.get(2, 0));
        assertEquals(4, u.get(0, 1));
        assertEquals(5, u.get(1, 1));
        assertEquals(6, u.get(2, 1));
        assertEquals(7, u.get(0, 2));
        assertEquals(8, u.get(1, 2));
        assertEquals(0, u.get(2, 2));
        
        u = new PuzzleNode(4);
        
        assertEquals(1, u.get(0, 0));
        assertEquals(2, u.get(1, 0));
        assertEquals(3, u.get(2, 0));
        assertEquals(4, u.get(3, 0));
        assertEquals(5, u.get(0, 1));
        assertEquals(6, u.get(1, 1));
        assertEquals(7, u.get(2, 1));
        assertEquals(8, u.get(3, 1));
        assertEquals(9, u.get(0, 2));
        assertEquals(10, u.get(1, 2));
        assertEquals(11, u.get(2, 2));
        assertEquals(12, u.get(3, 2));
        assertEquals(13, u.get(0, 3));
        assertEquals(14, u.get(1, 3));
        assertEquals(15, u.get(2, 3));
        assertEquals(0, u.get(3, 3));
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testThrowsOnDuplicateEntries() {
        new PuzzleNode(new byte[][]{
            {1, 2, 3},
            {4, 6, 5},
            {6, 7, 4}
        });
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsOnNoZeroEntry() {
        new PuzzleNode(new byte[][]{
            {1, 2, 3}, 
            {4, 5, 6}, 
            {7, 8, 9} 
        });
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testThrowsOnGap() {
        new PuzzleNode(new byte[][]{
            {0, 1, 2}, 
            {3, 4, 5}, 
            {6, 7, 9}, 
        });
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testThrowOnNegative() {
        new PuzzleNode(new byte[][]{
            {0, 1, 2}, 
            {3, 4, 5}, 
            {6, 7, -1}, 
        });
    }
    
    @Test
    public void testMoveUp() {
        PuzzleNode u = new PuzzleNode(new byte[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        });
        
        PuzzleNode v = u.moveUp();
        
        assertEquals(v, new PuzzleNode(new byte[][]{
            {1, 2, 3},
            {4, 5, 0},
            {7, 8, 6}
        }));
        
        u = new PuzzleNode(new byte[][]{
            {1, 2, 0}, 
            {3, 4, 5}, 
            {6, 7, 8} 
        });
        
        assertNull(u.moveUp());
    }

    @Test
    public void testMoveDown() {
        PuzzleNode u = new PuzzleNode(new byte[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        });
        
        PuzzleNode v = u.moveDown();
        
        assertNull(v);
        
        u = new PuzzleNode(new byte[][]{
            {1, 2, 0}, 
            {3, 4, 5}, 
            {6, 7, 8} 
        });
        
        assertEquals(new PuzzleNode(new byte[][]{
            {1, 2, 5},
            {3, 4, 0},
            {6, 7, 8},
        }), u.moveDown());
    }

    @Test
    public void testMoveLeft() {
        PuzzleNode u = new PuzzleNode(new byte[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        });
        
        PuzzleNode v = u.moveLeft();
        
        assertEquals(new PuzzleNode(new byte[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 0, 8}
        }), v);
        
        u = new PuzzleNode(new byte[][]{
            {0, 1, 2}, 
            {3, 4, 5}, 
            {6, 7, 8} 
        });
        
        assertNull(u.moveLeft());
    }

    @Test
    public void testMoveRight() {
        PuzzleNode u = new PuzzleNode(new byte[][]{
            {1, 2, 3},
            {4, 0, 5},
            {6, 7, 8}
        });
        
        PuzzleNode v = u.moveRight();
        
        assertEquals(new PuzzleNode(new byte[][]{
            {1, 2, 3},
            {4, 5, 0},
            {6, 7, 8},
        }), v);
        
        u = new PuzzleNode(new byte[][]{
            {1, 2, 0}, 
            {3, 4, 5}, 
            {6, 7, 8} 
        });
        
        assertNull(u.moveRight());
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, new PuzzleNode(3).getDimension());
        assertEquals(4, new PuzzleNode(4).getDimension());
        assertEquals(5, new PuzzleNode(5).getDimension());
        assertEquals(2, new PuzzleNode(new byte[][]{
            {1, 2},
            {0, 3}
        }).getDimension());
    }

    @Test
    public void testToString() {
        PuzzleNode u = new PuzzleNode(new byte[][]{
            {3, 2},
            {1, 0}
        });
        
        String s = "+-------+-------+\n" +
                   "|       |       |\n" +
                   "|   3   |   2   |\n" +
                   "|       |       |\n" +
                   "+-------+-------+\n" +
                   "|       |       |\n" +
                   "|   1   |   0   |\n" +
                   "|       |       |\n" +
                   "+-------+-------+\n";
        
        assertEquals(s, u.toString());
    }
    
    @Test
    public void testIsConnectedTo() {
        PuzzleNode origin = new PuzzleNode(new byte[][]{
            {3, 1, 8},
            {4, 0, 7},
            {2, 5, 6}
        });
        
        PuzzleNode u = new PuzzleNode(new byte[][]{
            {3, 0, 8}, 
            {4, 1, 7}, 
            {2, 5, 6} 
        });
        
        PuzzleNode r = new PuzzleNode(new byte[][]{
            {3, 1, 8}, 
            {4, 7, 0}, 
            {2, 5, 6} 
        });
        
        PuzzleNode d = new PuzzleNode(new byte[][]{
            {3, 1, 8}, 
            {4, 5, 7}, 
            {2, 0, 6} 
        });
        
        PuzzleNode l = new PuzzleNode(new byte[][]{
            {3, 1, 8}, 
            {0, 4, 7}, 
            {2, 5, 6} 
        });
        
        assertTrue(origin.isConnectedTo(u));
        assertTrue(origin.isConnectedTo(r));
        assertTrue(origin.isConnectedTo(d));
        assertTrue(origin.isConnectedTo(l));
        
        assertTrue(u.isConnectedTo(origin));
        assertTrue(r.isConnectedTo(origin));
        assertTrue(d.isConnectedTo(origin));
        assertTrue(l.isConnectedTo(origin));
        
        PuzzleNode n = new PuzzleNode(new byte[][]{
            {3, 1, 8},
            {4, 2, 7},
            {0, 5, 6} 
        });
        
        assertFalse(n.isConnectedTo(origin));
        assertFalse(origin.isConnectedTo(n));
    }

    @Test
    public void testIterator() {
        PuzzleNode u = new PuzzleNode(new byte[][]{
            {1, 2, 0}, 
            {3, 4, 5}, 
            {6, 7, 8} 
        });
        
        final Set<PuzzleNode> set = new HashSet<>();
        
        for (final PuzzleNode parent : u) {
            set.add(parent);
        }
        
        PuzzleNode a = new PuzzleNode(new byte[][]{
            {1, 0, 2},
            {3, 4, 5},
            {6, 7, 8}
        });
        
        PuzzleNode b = new PuzzleNode(new byte[][]{
            {1, 2, 5},
            {3, 4, 0},
            {6, 7, 8}
        });
        
        assertEquals(2, set.size());
        
        assertTrue(u.isConnectedTo(a));
        assertTrue(u.isConnectedTo(b));
        
        assertTrue(a.isConnectedTo(u));
        assertTrue(b.isConnectedTo(u));
        
        assertTrue(set.contains(a));
        assertTrue(set.contains(b));
    }
    
    @Test
    public void testParents() {
        PuzzleNode u = new PuzzleNode(new byte[][]{
            {1, 2, 0}, 
            {3, 4, 5}, 
            {6, 7, 8} 
        });
        
        final Set<PuzzleNode> set = new HashSet<>();
        
        for (final PuzzleNode parent : u.parents()) {
            set.add(parent);
        }
        
        PuzzleNode a = new PuzzleNode(new byte[][]{
            {1, 0, 2},
            {3, 4, 5},
            {6, 7, 8}
        });
        
        PuzzleNode b = new PuzzleNode(new byte[][]{
            {1, 2, 5},
            {3, 4, 0},
            {6, 7, 8}
        });
        
        assertEquals(2, set.size());
        
        assertTrue(u.isConnectedTo(a));
        assertTrue(u.isConnectedTo(b));
        
        assertTrue(a.isConnectedTo(u));
        assertTrue(b.isConnectedTo(u));
        
        assertTrue(set.contains(a));
        assertTrue(set.contains(b));
    }

    @Test
    public void testChildrenAndParentListSize() {
        PuzzleNode u = new PuzzleNode(new byte[][]{
            {1, 2, 3},
            {4, 0, 5},
            {6, 7, 8}
        });
        
        assertEquals(4, u.childrenListSize());
        assertEquals(4, u.parentsListSize());
        
        u = new PuzzleNode(new byte[][]{
            {0, 1, 2}, 
            {3, 4, 5}, 
            {6, 7, 8} 
        });
        
        assertEquals(2, u.childrenListSize());
        assertEquals(2, u.parentsListSize());
        
        u = new PuzzleNode(new byte[][]{
            {1, 2, 3}, 
            {0, 4, 5}, 
            {6, 7, 8} 
        });
        
        assertEquals(3, u.childrenListSize());
        assertEquals(3, u.parentsListSize());
    }
}
