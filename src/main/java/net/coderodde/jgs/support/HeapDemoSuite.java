package net.coderodde.jgs.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.coderodde.jgs.DemoSuite;
import static net.coderodde.jgs.Utilities.bar;
import static net.coderodde.jgs.Utilities.eq;
import static net.coderodde.jgs.Utilities.title1;
import static net.coderodde.jgs.Utilities.title2;
import net.coderodde.jgs.model.ds.MinPriorityQueue;
import net.coderodde.jgs.model.ds.support.BinomialHeap;
import net.coderodde.jgs.model.ds.support.DaryHeap;
import net.coderodde.jgs.model.ds.support.FibonacciHeap;
import net.coderodde.jgs.model.ds.support.PairingHeap;

/**
 * This class implements a demo suite for profiling various heap data 
 * structures.
 * 
 * The following heaps are demonstrated:
 * <ul>
 *   <li><tt>d</tt>-ary heap ({@link net.coderodde.jgs.model.ds.support.DaryHeap}) for <tt>d = 2, 3, 4, 5</tt>,
 *   <li>Binomial heap ({@link net.coderodde.jgs.model.ds.support.BinomialHeap}),
 *   <li>Fibonacci heap ({@link net.coderodde.jgs.model.ds.support.FibonacciHeap}).
 * </ul>
 * 
 * The profiled operations are
 * <ul>
 *   <li><code>add</code>,
 *   <li><code>extractMinimum</code>.
 * </ul>
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class HeapDemoSuite implements DemoSuite {

    /**
     * The number of elements to load into each heap.
     */
    private static final int N = 10000;
    
    /**
     * The seed for all the random number generators.
     */
    private final long seed;
    
    private final List<Integer> dary2Result;
    private final List<Integer> dary3Result;
    private final List<Integer> dary4Result;
    private final List<Integer> dary5Result;
    private final List<Integer> binomialResult;
    private final List<Integer> fibonacciResult;
    private final List<Integer> pairingResult;
    
    /**
     * Constructs this demo suite.
     */
    public HeapDemoSuite() {
        seed = System.currentTimeMillis();
        title1("HeapDemoSuite.java, seed: " + seed);
        
        dary2Result     = new ArrayList<>(N);
        dary3Result     = new ArrayList<>(N);
        dary4Result     = new ArrayList<>(N);
        dary5Result     = new ArrayList<>(N);
        binomialResult  = new ArrayList<>(N);
        fibonacciResult = new ArrayList<>(N);
        pairingResult   = new ArrayList<>(N);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        title1("PROFILING HEAPS");
        profileDaryHeaps(seed);
        profileBinomialHeap(seed);
        profileFibonacciHeap(seed);
        profilePairingHeap(seed);
        
        bar();
        System.out.println(
                "Output sequences are identical: " + eq(dary2Result,
                                                        dary3Result,
                                                        dary4Result,
                                                        dary5Result,
                                                        binomialResult,
                                                        fibonacciResult,
                                                        pairingResult));
        
        title1("END OF PROFILING HEAPS");
        System.out.println();
        System.out.println();
    }
    
    /**
     * Profile all the {@link net.coderodde.jgs.model.ds.support.DaryHeap}s, 
     * each with its own degree.
     * 
     * @param seed the seed for random number generators.
     */
    private void profileDaryHeaps(final long seed) {
        profileDaryHeap(new DaryHeap<Integer, Integer>(2), seed, dary2Result);
        profileDaryHeap(new DaryHeap<Integer, Integer>(3), seed, dary3Result);
        profileDaryHeap(new DaryHeap<Integer, Integer>(4), seed, dary4Result);
        profileDaryHeap(new DaryHeap<Integer, Integer>(5), seed, dary5Result);
    }
    
    /**
     * Profile the {@link net.coderodde.jgs.model.ds.support.BinomialHeap}.
     * 
     * @param seed the seed for random number generator.
     */
    private void profileBinomialHeap(final long seed) {
        final MinPriorityQueue<Integer, Integer> heap = new BinomialHeap<>();
        
        title2(heap.getClass().getSimpleName());
        
        final Random rnd = new Random(seed);
        
        long total = 0L;
        
        long ta = System.currentTimeMillis();
        
        for (int i = 0; i != N; ++i) {
            final Integer j = rnd.nextInt(N);
            heap.add(j, j);
        }
        
        long tb = System.currentTimeMillis();
        
        System.out.println("add() in " + (tb - ta) + " ms.");
        
        total += tb - ta;
        
        binomialResult.clear();
        
        ta = System.currentTimeMillis();
        
        while (heap.isEmpty() == false) {
            binomialResult.add(heap.extractMinimum());
        }
        
        tb = System.currentTimeMillis();
        
        System.out.println("extractMinimum() in " + (tb - ta) + " ms.");
        
        total += tb - ta;
        
        System.out.println("Total: " + total + " ms.");
    }
    
    /**
     * Profile the {@link net.coderodde.jgs.model.ds.support.FibonacciHeap}.
     * 
     * @param seed the seed for random number generator.
     */
    private void profileFibonacciHeap(final long seed) {
        final MinPriorityQueue<Integer, Integer> heap = new FibonacciHeap<>();
        
        title2(heap.getClass().getSimpleName());
        
        final Random rnd = new Random(seed);
        
        long total = 0L;
        
        long ta = System.currentTimeMillis();
        
        for (int i = 0; i != N; ++i) {
            final Integer j = rnd.nextInt(N);
            heap.add(j, j);
        }
        
        long tb = System.currentTimeMillis();
        
        System.out.println("add() in " + (tb - ta) + " ms.");
        
        total += tb - ta;
        
        fibonacciResult.clear();
        
        ta = System.currentTimeMillis();
        
        while (heap.isEmpty() == false) {
            fibonacciResult.add(heap.extractMinimum());
        }
        
        tb = System.currentTimeMillis();
        
        System.out.println("extractMinimum() in " + (tb - ta) + " ms.");
        
        total += tb - ta;
        
        System.out.println("Total: " + total + " ms.");
    }
    
    /**
     * Profile the {@link net.coderodde.jgs.model.ds.support.PairingHeap}.
     * 
     * @param seed the seed for random number generator.
     */
    private void profilePairingHeap(final long seed) {
        final MinPriorityQueue<Integer, Integer> heap = new PairingHeap<>();
        
        title2(heap.getClass().getSimpleName());
        
        final Random rnd = new Random(seed);
        
        long total = 0L;
        
        long ta = System.currentTimeMillis();
        
        for (int i = 0; i != N; ++i) {
            final Integer j = rnd.nextInt(N);
            heap.add(j, j);
        }
        
        long tb = System.currentTimeMillis();
        
        System.out.println("add() in " + (tb - ta) + " ms.");
        
        total += tb - ta;
        
        pairingResult.clear();
        
        ta = System.currentTimeMillis();
        
        while (heap.isEmpty() == false) {
            pairingResult.add(heap.extractMinimum());
        }
        
        tb = System.currentTimeMillis();
        
        System.out.println("extractMinimum() in " + (tb - ta) + " ms.");
        
        total += tb - ta;
        
        System.out.println("Total: " + total + " ms.");
    }
    
    /**
     * Implements an actual profiling routine for a given <tt>d</tt>-ary heap.
     * 
     * @param heap the heap to profile.
     * @param seed the seed for the random number generator.
     * @param result the list for storing output sequence.
     */
    private void profileDaryHeap(final DaryHeap<Integer, Integer> heap,
                         final long seed,
                         final List<Integer> result) {
        title2(heap.getClass().getSimpleName() + 
               ", degree " + heap.getDegree());
        
        final Random rnd = new Random(seed);
        
        long total = 0L;
        
        long ta = System.currentTimeMillis();
        
        for (int i = 0; i != N; ++i) {
            final Integer j = rnd.nextInt(N);
            heap.add(j, j);
        }
        
        long tb = System.currentTimeMillis();
        
        System.out.println("add() in " + (tb - ta) + " ms.");
        
        total += tb - ta;
        
        result.clear();
        
        ta = System.currentTimeMillis();
        
        while (heap.isEmpty() == false) {
            result.add(heap.extractMinimum());
        }
        
        tb = System.currentTimeMillis();
        
        System.out.println("extractMinimum() in " + (tb - ta) + " ms.");
        
        total += tb - ta;
        
        System.out.println("Total: " + total + " ms.");
    }
}
