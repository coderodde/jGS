package net.coderodde.jgs;

import net.coderodde.jgs.model.ds.support.PairingHeap;
import net.coderodde.jgs.support.DecreaseKeySuite;
import net.coderodde.jgs.support.HeapDemoSuite;

public class Demo {

    public static void main(final String... args) {
        //new HeapDemoSuite().run();
        //new DecreaseKeySuite().run();
        
        PairingHeap<Integer, Integer> heap = new PairingHeap<>();
        
        for (int i = 0; i < 1000; ++i) {
            heap.add(i, i);
        }
        
        int i = 1;
        
        while (heap.size() > 0) {
            System.out.println(i + ": " + heap.extractMinimum());
            i++;
        }
    }
}
