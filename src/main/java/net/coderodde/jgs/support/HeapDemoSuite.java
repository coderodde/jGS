package net.coderodde.jgs.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.coderodde.jgs.DemoSuite;
import static net.coderodde.jgs.Utilities.bar;
import static net.coderodde.jgs.Utilities.title1;
import static net.coderodde.jgs.Utilities.title2;
import net.coderodde.jgs.model.ds.MinPriorityQueue;
import net.coderodde.jgs.model.ds.support.BinomialHeap;
import net.coderodde.jgs.model.ds.support.DaryHeap;
import net.coderodde.jgs.model.ds.support.FibonacciHeap;

/**
 * This class implements a demo suite for profiling various heap data 
 * structures.
 * 
 * The following heaps are demonstrated:
 * <ul>
 *   <li><tt>d</tt>-ary heap ({@code net.coderodde.jgs.model.ds.support.DaryHeap}) for <tt>d = 2, 3, 4, 5</tt>,</li>
 *   <li>Binomial heap</li> ({@code net.coderodde.jgs.model.ds.support.BinomialHeap}),
 *   <li>Fibonacci heap ({@code net.coderodde.jgs.model.ds.support.FibonacciHeap}).</li>
 * </ul>
 * 
 * The profiled operations are
 * <ul>
 *   <li><code>add</code></li>,
 *   <li><code>decreaseKey</code></li>,
 *   <li><code>extractMinimum</code></li>.
 * </ul>
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class HeapDemoSuite implements DemoSuite {

    private static final int N = 500000;
    
    private final long seed;
    
    private final List<Integer> dary2Result;
    private final List<Integer> dary3Result;
    private final List<Integer> dary4Result;
    private final List<Integer> dary5Result;
    private final List<Integer> binomialResult;
    private final List<Integer> fibonacciResult;
    
    public HeapDemoSuite() {
        seed = System.currentTimeMillis();
        title1("HeapDemoSuite.java, seed: " + seed);
        
        dary2Result     = new ArrayList<>(N);
        dary3Result     = new ArrayList<>(N);
        dary4Result     = new ArrayList<>(N);
        dary5Result     = new ArrayList<>(N);
        binomialResult  = new ArrayList<>(N);
        fibonacciResult = new ArrayList<>(N);
    }
    
    @Override
    public void run() {
        title1("PROFILING HEAPS");
        profileDaryHeaps(seed);
        profileBinomialHeap(seed);
        profileFibonacciHeap(seed);
        title2("Equal sequences: " + eq(dary2Result,
                                        dary3Result,
                                        dary4Result,
                                        dary5Result,
                                        binomialResult,
                                        fibonacciResult));
        bar();
    }
    
    private void profileDaryHeaps(final long seed) {
        profile(new DaryHeap<Integer, Integer>(2), seed, dary2Result);
        profile(new DaryHeap<Integer, Integer>(3), seed, dary3Result);
        profile(new DaryHeap<Integer, Integer>(4), seed, dary4Result);
        profile(new DaryHeap<Integer, Integer>(5), seed, dary5Result);
    }
    
    private void profileBinomialHeap(final long seed) {
        final MinPriorityQueue<Integer, Integer> heap = new BinomialHeap<>();
        
        title2(heap.getClass().getSimpleName());
        
        final Random rnd = new Random(seed);
        
        long ta = System.currentTimeMillis();
        
        for (int i = 0; i != N; ++i) {
            final Integer j = rnd.nextInt(N);
            heap.add(j, j);
        }
        
        long tb = System.currentTimeMillis();
        
        System.out.println("add() in " + (tb - ta) + " ms.");
        
        ta = System.currentTimeMillis();
        
        for (int i = 0; i != N / 2; ++i) {
            final Integer t = rnd.nextInt(N);
            final Integer p = t / 2;
            
            heap.decreasePriority(t, p);
        }
        
        tb = System.currentTimeMillis();
        
        System.out.println("decreasePriority() in " + (tb - ta) + " ms.");
        
        binomialResult.clear();
        
        ta = System.currentTimeMillis();
        
        while (heap.isEmpty() == false) {
            binomialResult.add(heap.extractMinimum());
        }
        
        tb = System.currentTimeMillis();
        
        System.out.println("extractMinimum() in " + (tb - ta) + " ms.");
    }
    
    private void profileFibonacciHeap(final long seed) {
        final MinPriorityQueue<Integer, Integer> heap = new FibonacciHeap<>();
        
        title2(heap.getClass().getSimpleName());
        
        final Random rnd = new Random(seed);
        
        long ta = System.currentTimeMillis();
        
        for (int i = 0; i != N; ++i) {
            final Integer j = rnd.nextInt(N);
            heap.add(j, j);
        }
        
        long tb = System.currentTimeMillis();
        
        System.out.println("add() in " + (tb - ta) + " ms.");
        
        ta = System.currentTimeMillis();
        
        for (int i = 0; i != N / 2; ++i) {
            final Integer t = rnd.nextInt(N);
            final Integer p = t / 2;
            
            heap.decreasePriority(t, p);
        }
        
        tb = System.currentTimeMillis();
        
        System.out.println("decreasePriority() in " + (tb - ta) + " ms.");
        
        fibonacciResult.clear();
        
        ta = System.currentTimeMillis();
        
        while (heap.isEmpty() == false) {
            fibonacciResult.add(heap.extractMinimum());
        }
        
        tb = System.currentTimeMillis();
        
        System.out.println("extractMinimum() in " + (tb - ta) + " ms.");
    }
    
    private void profile(final DaryHeap<Integer, Integer> heap,
                         final long seed,
                         final List<Integer> result) {
        title2(heap.getClass().getSimpleName() + 
               ", degree " + heap.getDegree());
        
        final Random rnd = new Random(seed);
        
        long ta = System.currentTimeMillis();
        
        for (int i = 0; i != N; ++i) {
            final Integer j = rnd.nextInt(N);
            heap.add(j, j);
        }
        
        long tb = System.currentTimeMillis();
        
        System.out.println("add() in " + (tb - ta) + " ms.");
        
        ta = System.currentTimeMillis();
        
        for (int i = 0; i != N / 2; ++i) {
            final Integer t = rnd.nextInt(N);
            final Integer p = t / 2;
            
            heap.decreasePriority(t, p);
        }
        
        tb = System.currentTimeMillis();
        
        System.out.println("decreasePriority() in " + (tb - ta) + " ms.");
        
        result.clear();
        
        ta = System.currentTimeMillis();
        
        while (heap.isEmpty() == false) {
            result.add(heap.extractMinimum());
        }
        
        tb = System.currentTimeMillis();
        
        System.out.println("extractMinimum() in " + (tb - ta) + " ms.");
    }
    
    private static final boolean eq(final List<Integer>... lists) {
        if (lists.length < 2) {
            throw new IllegalArgumentException("Two few integer lists.");
        }
        
        for (int i = 0; i < lists.length - 1; ++i) {
            if (lists[i].size() != lists[i + 1].size()) {
                return false;
            }
        }
        
        for (int i = 0; i < lists[0].size(); ++i) {
            for (int j = 0; j < lists.length - 1; ++j) {
                if (lists[j].get(i) != lists[j + 1].get(i)) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
