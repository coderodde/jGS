package net.coderodde.jgs;

import net.coderodde.jgs.support.DecreaseKeySuite;
import net.coderodde.jgs.support.DenseGraphShortestPathSuite;
import net.coderodde.jgs.support.HeapDemoSuite;
import net.coderodde.jgs.support.PuzzleGraphSearchSuite;
import net.coderodde.jgs.support.SparseGraphShortestPathSuite;
import net.coderodde.jgs.support.UnweightedGraphSearchSuite;

public class Demo {

    public static void main(final String... args) {
//        new HeapDemoSuite().run();
//        new DecreaseKeySuite().run();
//        new DenseGraphShortestPathSuite().run();
//        new SparseGraphShortestPathSuite().run();
//        new PuzzleGraphSearchSuite().run();
        new UnweightedGraphSearchSuite().run();
    }
}
