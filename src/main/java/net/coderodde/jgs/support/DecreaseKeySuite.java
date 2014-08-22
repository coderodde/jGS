package net.coderodde.jgs.support;

import java.util.ArrayList;
import java.util.List;
import net.coderodde.jgs.DemoSuite;
import static net.coderodde.jgs.Utilities.bar;
import static net.coderodde.jgs.Utilities.eq;
import static net.coderodde.jgs.Utilities.title1;
import static net.coderodde.jgs.Utilities.title2;
import net.coderodde.jgs.model.ds.MinPriorityQueue;
import net.coderodde.jgs.model.ds.support.BinomialHeap;
import net.coderodde.jgs.model.ds.support.DaryHeap;
import net.coderodde.jgs.model.ds.support.FibonacciHeap;

/**
 * This class implements a demo suite for profiling 
 * <code>decreasePriority</code> of each heap of the project.
 * 
 * The following heaps are demonstrated:
 * <ul>
 *   <li><tt>d</tt>-ary heap ({@code net.coderodde.jgs.model.ds.support.DaryHeap}) for <tt>d = 2, 3, 4, 5</tt>,</li>
 *   <li>Binomial heap ({@code net.coderodde.jgs.model.ds.support.BinomialHeap}),</li>
 *   <li>Fibonacci heap ({@code net.coderodde.jgs.model.ds.support.FibonacciHeap}).</li>
 * </ul>
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class DecreaseKeySuite implements DemoSuite {

    /**
     * The amount of elements to load into each heap.
     */
    private static final int N = 1000000;
    
    /**
     * The seed for random number generators.
     */
    private final long seed;
    
    private final List<Integer> dary2Result;
    private final List<Integer> dary3Result;
    private final List<Integer> dary4Result;
    private final List<Integer> dary5Result;
    private final List<Integer> binomialResult;
    private final List<Integer> fibonacciResult;
    
    /**
     * Constructs this demo suite.
     */
    public DecreaseKeySuite() {
        this.seed            = System.currentTimeMillis();
        this.dary2Result     = new ArrayList<>(N);
        this.dary3Result     = new ArrayList<>(N);
        this.dary4Result     = new ArrayList<>(N);
        this.dary5Result     = new ArrayList<>(N);
        this.binomialResult  = new ArrayList<>(N);
        this.fibonacciResult = new ArrayList<>(N);
        
        title1("DecreaseKeySuite.java, seed: " + seed);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        title1("PROFILING HEAP DECREASE KEY");
        profileDaryHeap(new DaryHeap<Integer, Integer>(2), dary2Result);
        profileDaryHeap(new DaryHeap<Integer, Integer>(3), dary3Result);
        profileDaryHeap(new DaryHeap<Integer, Integer>(4), dary4Result);
        profileDaryHeap(new DaryHeap<Integer, Integer>(5), dary5Result);
        profileBinomialHeap();
        profileFibonacciHeap();
        
        bar();
        
        System.out.println(
                "Output sequences are identical: " + eq(dary2Result,
                                                        dary3Result,
                                                        dary4Result,
                                                        dary5Result,
                                                        binomialResult,
                                                        fibonacciResult));
        
        title1("END OF PROFILING HEAP DECREASE KEY");
        
        System.out.println();
        System.out.println();
    }

    /**
     * Profiles a particular <tt>d</tt>-ary heap.
     * 
     * @param heap the <tt>d</tt>-ary heap to profile.
     * @param resultList the list for storing the output sequence.
     */
    private void profileDaryHeap(final DaryHeap<Integer, Integer> heap,
                                 final List<Integer> resultList) {
        title2(heap.getClass().getSimpleName() + 
               ", degree " + heap.getDegree());
        
        for (int i = 0; i != N; ++i) {
            heap.add(i, i);
        }
        
        long ta = System.currentTimeMillis();
        
        for (int i = 0; i < N; i += 2) {
            heap.decreasePriority(i, -i);
        }
        
        long tb = System.currentTimeMillis();
        
        System.out.println("decreaseKey() in " + (tb - ta) + " ms.");
        
        resultList.clear();
        
        while (heap.isEmpty() == false) {
            resultList.add(heap.extractMinimum());
        }
    }

    /**
     * Profiles a {@link net.coderodde.jgs.model.ds.support.BinomialHeap}.
     */
    private void profileBinomialHeap() {
        final MinPriorityQueue<Integer, Integer> heap = new BinomialHeap<>();
        
        title2(heap.getClass().getSimpleName());
        
        for (int i = 0; i != N; ++i) {
            heap.add(i, i);
        }
        
        long ta = System.currentTimeMillis();
        
        for (int i = 0; i < N; i += 2) {
            heap.decreasePriority(i, -i);
        }
        
        long tb = System.currentTimeMillis();
        
        System.out.println("decreaseKey() in " + (tb - ta) + " ms.");
        
        binomialResult.clear();
        
        while (heap.isEmpty() == false) {
            binomialResult.add(heap.extractMinimum());
        }
    }

    /**
     * Profiles a {@link net.coderodde.jgs.model.ds.support.FibonacciHeap}.
     */
    private void profileFibonacciHeap() {
        final MinPriorityQueue<Integer, Integer> heap = new FibonacciHeap<>();
        
        title2(heap.getClass().getSimpleName());
        
        for (int i = 0; i != N; ++i) {
            heap.add(i, i);
        }
        
        long ta = System.currentTimeMillis();
        
        for (int i = 0; i < N; i += 2) {
            heap.decreasePriority(i, -i);
        }
        
        long tb = System.currentTimeMillis();
        
        System.out.println("decreaseKey() in " + (tb - ta) + " ms.");
        
        fibonacciResult.clear();
        
        while (heap.isEmpty() == false) {
            fibonacciResult.add(heap.extractMinimum());
        }
    }
}
