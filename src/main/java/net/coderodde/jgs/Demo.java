package net.coderodde.jgs;

import java.util.Random;
import net.coderodde.jgs.model.ds.support.BinomialHeap;

public class Demo {

    public static void main(final String... args) {
        final long seed = System.currentTimeMillis();
        BinomialHeap<Integer, Integer> heap = new BinomialHeap<>();
        
        for (int i = 0; i < 10; ++i) {
            heap.add(i, i);
        }
        
        while (heap.size() > 0) {
            Integer i = heap.extractMinimum();
            System.out.println(heap.size() + " : " + i);
        }
//        final int sz = 100;
//        final Random rnd = new Random(seed);
//        
//        for (int i = 0; i < sz; ++i) {
//            Integer e = rnd.nextInt();
//            heap.add(e, e);
//        }
//        
//        Integer prev = null;
//        
//        while (!heap.isEmpty()) {
//            Integer current = heap.extractMinimum();
//            
//            if (prev != null && prev > current) {
//                System.out.println("No!");
//                System.exit(1);
//            }
//            
//            prev = current;
//        }
    }
}
