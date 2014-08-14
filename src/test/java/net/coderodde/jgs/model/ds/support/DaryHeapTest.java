package net.coderodde.jgs.model.ds.support;

import java.util.Random;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests <code>net.coderodde.jgs.model.ds.support.DaryHeap</code>.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class DaryHeapTest {
    
    private static final long seed = System.currentTimeMillis();
    
    @BeforeClass
    public static void initClass() {
        System.out.println("DaryHeapTest.java, seed: " + seed);
    }
    
    /**
     * Test of add method, of class DaryHeap.
     */
    @Test
    public void testAdd() {
        testAddOn(new DaryHeap<Float, Float>(), seed);
        testAddOn(new DaryHeap<Float, Float>(3), seed);
        testAddOn(new DaryHeap<Float, Float>(4), seed);
        testAddOn(new DaryHeap<Float, Float>(5), seed);
    }

    /**
     * Test of decreasePriority method, of class DaryHeap.
     */
    @Test
    public void testDecreasePriority() {
        testDecreasePriorityOn(new DaryHeap<Integer, Integer>());
        testDecreasePriorityOn(new DaryHeap<Integer, Integer>(3));
        testDecreasePriorityOn(new DaryHeap<Integer, Integer>(4));
        testDecreasePriorityOn(new DaryHeap<Integer, Integer>(5));
    }

    /**
     * Test of extractMinimum method, of class DaryHeap.
     */
    @Test
    public void testExtractMinimum() {
        testExtractMinimumOn(new DaryHeap<Integer, Integer>());
        testExtractMinimumOn(new DaryHeap<Integer, Integer>(3));
        testExtractMinimumOn(new DaryHeap<Integer, Integer>(4));
        testExtractMinimumOn(new DaryHeap<Integer, Integer>(5));
    }

    /**
     * Test of size method, of class DaryHeap.
     */
    @Test
    public void testSize() {
        testSizeOn(new DaryHeap<Integer, Integer>());
        testSizeOn(new DaryHeap<Integer, Integer>(3));
        testSizeOn(new DaryHeap<Integer, Integer>(4));
        testSizeOn(new DaryHeap<Integer, Integer>(5));
    }
    

    /**
     * Test of isEmpty method, of class DaryHeap.
     */
    @Test
    public void testIsEmpty() {
        testIsEmptyOn(new DaryHeap<Integer, Integer>());
        testIsEmptyOn(new DaryHeap<Integer, Integer>(3));
        testIsEmptyOn(new DaryHeap<Integer, Integer>(4));
        testIsEmptyOn(new DaryHeap<Integer, Integer>(5));
    }

    /**
     * Test of clear method, of class DaryHeap.
     */
    @Test
    public void testClear() {
        testClearOn(new DaryHeap<Integer, Integer>());
        testClearOn(new DaryHeap<Integer, Integer>(3));
        testClearOn(new DaryHeap<Integer, Integer>(4));
        testClearOn(new DaryHeap<Integer, Integer>(5));
    }

    /**
     * Test of spawn method, of class DaryHeap.
     */
    @Test
    public void testSpawn() {
        testSpawnOn(new DaryHeap<Integer, Integer>());
        testSpawnOn(new DaryHeap<Integer, Integer>(3));
        testSpawnOn(new DaryHeap<Integer, Integer>(4));
        testSpawnOn(new DaryHeap<Integer, Integer>(5));
    }
    
    private void testAddOn(final DaryHeap<Float, Float> heap, final long seed) {
        final int sz = 100000;
        final Random rnd = new Random(seed);
        
        for (int i = 0; i != sz; ++i) {
            final Float f = rnd.nextFloat();
            heap.add(Float.NaN, Float.NaN);
        }
        
        Float prev = null;
        
        while (!heap.isEmpty()) {
            Float current = heap.extractMinimum();
            
            if (prev != null && prev > current) {
                fail("The sequence was not monotonically increasing. " +
                     "Previous: " + prev + ", current: " + current + ".");
            }
            
            prev = current;
        }
    }
    
    private void testDecreasePriorityOn(final DaryHeap<Integer, Integer> heap) {
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
    
    private void testExtractMinimumOn(final DaryHeap<Integer, Integer> heap) {
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
    
    private void testSizeOn(final DaryHeap<Integer, Integer> heap) {
        assertTrue(heap.isEmpty());
        
        final long sz = 10000;
        
        for (int i = 0; i < sz; ++i) {
            assertEquals(i, heap.size());
            heap.add(i, i);
        }
        
        assertEquals(sz, heap.size());
        assertFalse(heap.isEmpty());
    }
    
    private void testIsEmptyOn(final DaryHeap<Integer, Integer> heap) {
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
    
    private void testClearOn(final DaryHeap<Integer, Integer> heap) {
        assertTrue(heap.isEmpty());
        
        final int sz = 10000;
        
        for (int i = 0; i < sz; ++i) {
            heap.add(i, i);
            assertFalse(heap.isEmpty());
        }
        
        heap.clear();
        
        assertTrue(heap.isEmpty());
    }
    
    private void testSpawnOn(final DaryHeap<Integer, Integer> heap) {
        heap.add(1, 2);
        
        DaryHeap<Integer, Integer> heap2 = 
                (DaryHeap<Integer, Integer>) heap.spawn();
        
        assertEquals(heap.getDegree(), heap2.getDegree());
        assertFalse(heap.isEmpty());
        assertTrue(heap2.isEmpty());
    }
}
