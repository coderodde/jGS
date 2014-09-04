package net.coderodde.jgs.model.support;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PuzzleNodeTest {
    
    @Before
    public void setUp() {
    }

    @Test
    public void testIterator() {
        PuzzleNode v = new PuzzleNode(new byte[][]{
            new byte[]{1, 2, 3},
            new byte[]{4, 0, 5},
            new byte[]{6, 7, 8}
        });
        
        PuzzleNode up = new PuzzleNode(new byte[][]{
            new byte[]{1, 0, 3},
            new byte[]{4, 2, 5},
            new byte[]{6, 7, 8}
        });
        
        PuzzleNode left = new PuzzleNode(new byte[][]{
            new byte[]{1, 2, 3},
            new byte[]{0, 4, 5},
            new byte[]{6, 7, 8}
        });
        
        PuzzleNode down = new PuzzleNode(new byte[][]{
            new byte[]{1, 2, 3},
            new byte[]{4, 7, 5},
            new byte[]{6, 0, 8}
        });
        
        PuzzleNode right = new PuzzleNode(new byte[][]{
            new byte[]{1, 2, 3},
            new byte[]{4, 5, 0},
            new byte[]{6, 7, 8}
        });
        
        assertEquals(up, v.moveUp());
        assertEquals(left, v.moveLeft());
        assertEquals(down, v.moveDown());
        assertEquals(right, v.moveRight());
        
        v = new PuzzleNode(new byte[][]{
            new byte[]{1, 0, 2},
            new byte[]{3, 4, 5},
            new byte[]{6, 7, 8}
        });
        
        up = new PuzzleNode(new byte[][]{
            new byte[]{1, 0, 2},
            new byte[]{3, 4, 5},
            new byte[]{6, 7, 8}
        });
        
        right = new PuzzleNode(new byte[][]{
            new byte[]{1, 0, 2},
            new byte[]{3, 4, 5},
            new byte[]{6, 7, 8}
        });
        
        down = new PuzzleNode(new byte[][]{
            new byte[]{1, 0, 2},
            new byte[]{3, 4, 5},
            new byte[]{6, 7, 8}
        });
        
        left = new PuzzleNode(new byte[][]{
            new byte[]{1, 0, 2},
            new byte[]{3, 4, 5},
            new byte[]{6, 7, 8}
        });
        
        
        
        v = new PuzzleNode(new byte[][]{
            new byte[]{1, 2, 3},
            new byte[]{4, 5, 6},
            new byte[]{7, 8, 0},
        });
    }

    @Test
    public void testEquals() {
        
    }

    @Test
    public void testGet() {
        
    }
    
    @Test
    public void testMoveUp() {
        
    }

    @Test
    public void testMoveDown() {
        
    }

    @Test
    public void testMoveLeft() {
        
    }

    @Test
    public void testMoveRight() {
        
    }

    @Test
    public void testGetDimension() {
        
    }

    @Test
    public void testToString() {
        
    }
    
    @Test
    public void testIsConnectedTo() {
        
    }

    @Test
    public void testParents() {
        
    }

    @Test
    public void testChildrenListSize() {
        
    }

    @Test
    public void testParentsListSize() {
        
    }
}
