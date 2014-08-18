package net.coderodde.jgs.model.ds.support;

import java.util.Random;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests 
 * <code>net.coderodde.jgs.model.ds.support.BinomialHeap</code>.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class BinomialHeapTest {
    
    private static final long seed = 1408192902229L;//System.currentTimeMillis();
    
    private final BinomialHeap<Integer, Integer> heap;
    
    public BinomialHeapTest() {
        this.heap = new BinomialHeap<>();
    }
    
    @BeforeClass
    public static void initClass() {
        System.out.println("BinomialHeapTest.java, seed: " + seed);
    }
    
    /**
     * Clears the heap before any test.
     */
    @Before
    public void init() {
        heap.clear();
    }
    
    /**
     * Test of add method, of class DaryHeap.
     */
    @Test
    public void testAdd() {
        final int sz = 100000;
        final Random rnd = new Random(seed);
        
        for (int i = 0; i != sz; ++i) {
            final Float f = rnd.nextFloat();
        }
        
        Integer prev = null;
        
        while (!heap.isEmpty()) {
            Integer current = heap.extractMinimum();
            
            if (prev != null && prev > current) {
                fail("The sequence was not monotonically increasing. " +
                     "Previous: " + prev + ", current: " + current + ".");
            }
            
            prev = current;
        }
    }
    
    /**
     * Test of decreasePriority method, of class DaryHeap.
     */
    @Test
    public void testDecreasePriority() {
        for (int i = 10; i != 0; --i) {
            heap.add(i, i);
        }
        
        heap.decreasePriority(10, -1);
        
        assertEquals((Integer) 10, heap.extractMinimum());
        
        int i = 1;
        while (!heap.isEmpty()) {
            assertEquals((Integer) i, heap.extractMinimum());
            i++;
        }
    }
    
    /**
     * Test of extractMinimum method, of class DaryHeap.
     */
    @Test
    public void testExtractMinimum() {
        final int sz = 10000;
        final Random rnd = new Random(seed);
        
        for (int i = 0; i < sz; ++i) {
            Integer e = rnd.nextInt();
            heap.add(e, e);
        }
        
        Integer prev = null;
        
        while (!heap.isEmpty()) {
            Integer current = heap.extractMinimum();
            
            if (prev != null && prev > current) {
                fail("The sequence was not monotonically increasing. " +
                     "Previous: " + prev + ", current: " + current + ".");
            }
            
            prev = current;
        }
    }
    
    /**
     * Test of size method, of class DaryHeap.
     */
    @Test
    public void testSize() {
        assertTrue(heap.isEmpty());
        
        final long sz = 10000;
        
        for (int i = 0; i < sz; ++i) {
            assertEquals(i, heap.size());
            heap.add(i, i);
        }
        
        assertEquals(sz, heap.size());
        assertFalse(heap.isEmpty());
    }
    
    /**
     * Test of isEmpty method, of class DaryHeap.
     */
    @Test
    public void testIsEmpty() {
        assertTrue(heap.isEmpty());
        
        heap.add(0, 0);
        
        assertFalse(heap.isEmpty());
        
        heap.add(1, -1);
        
        assertFalse(heap.isEmpty());
        
        heap.extractMinimum();
        
        assertFalse(heap.isEmpty());
        
        heap.extractMinimum();
        
        assertTrue(heap.isEmpty());
        
        heap.add(100, 10);
        heap.add(10, 1);
        
        assertFalse(heap.isEmpty());
        
        heap.clear();
        
        assertTrue(heap.isEmpty());
    }
    
    /**
     * Test of clear method, of class DaryHeap.
     */
    @Test
    public void testClear() {
        assertTrue(heap.isEmpty());
        
        final int sz = 10000;
        
        for (int i = 0; i < sz; ++i) {
            heap.add(i, i);
            assertFalse(heap.isEmpty());
        }
        
        heap.clear();
        
        assertTrue(heap.isEmpty());
    }
    
    /**
     * Test of spawn method, of class DaryHeap.
     */
    @Test
    public void testSpawn() {
        heap.add(1, 2);
        
        BinomialHeap<Integer, Integer> heap2 = 
                (BinomialHeap<Integer, Integer>) heap.spawn();
        
        assertTrue(heap2 instanceof BinomialHeap);
        assertFalse(heap.isEmpty());
        assertTrue(heap2.isEmpty());
    }
}
