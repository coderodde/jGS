package net.coderodde.jgs.support;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Random;
import net.coderodde.jgs.DemoSuite;
import static net.coderodde.jgs.Utilities.bar;
import static net.coderodde.jgs.Utilities.stepAway;
import static net.coderodde.jgs.Utilities.title1;
import static net.coderodde.jgs.Utilities.title2;
import net.coderodde.jgs.model.Path;
import net.coderodde.jgs.model.PathFinder;
import net.coderodde.jgs.model.support.BreadthFirstSearchPathFinder;
import net.coderodde.jgs.model.support.PuzzleNode;

public class PuzzleGraphSearchSuite implements DemoSuite {

    private final long seed;
    
    private Path<PuzzleNode> pathBFSLinkedList;
    private Path<PuzzleNode> pathBFSArrayDeque;
    
    private final PuzzleNode source;
    private final PuzzleNode target;
    
    public PuzzleGraphSearchSuite() {
        this.seed = System.currentTimeMillis();
        this.source = new PuzzleNode(4);
        this.target = stepAway(this.source, 35, new Random(seed));
        title1("PuzzleGraphSearchSuite.java, seed: " + seed);
    }
    
    @Override
    public void run() {
        title1("PROFILING SEARCH IN UNWEIGHTED GRAPHS");
        profileBFSLinkedList();
        profileBFSArrayDeque();
        
        bar();
        
        System.out.println("Paths are of the same length: " + 
                (pathBFSArrayDeque.size() == pathBFSLinkedList.size()));
        
        title1("END OF PROFILING SEARCH IN UNWEIGHTED GRAPHS");
        
        System.out.println();
        System.out.println();
    }
    
    private void profileBFSLinkedList() {
        final PathFinder<PuzzleNode, Integer> pf = 
               new BreadthFirstSearchPathFinder<>(new LinkedList<PuzzleNode>());
        
        title2(pf.getClass().getSimpleName() + " on LinkedList");
        
        this.pathBFSLinkedList = profile(new BreadthFirstSearchPathFinder<>(
                                         new LinkedList<PuzzleNode>()));
    }
    
    private void profileBFSArrayDeque() {
        final PathFinder<PuzzleNode, Integer> pf = 
               new BreadthFirstSearchPathFinder<>(new ArrayDeque<PuzzleNode>());
        
        title2(pf.getClass().getSimpleName() + " on ArrayDeque");
        
        this.pathBFSArrayDeque = profile(new BreadthFirstSearchPathFinder<>(
                                         new ArrayDeque<PuzzleNode>()));
    }
    
    private Path<PuzzleNode> profile(
            final PathFinder<PuzzleNode, Integer> finder) {
        long ta = System.currentTimeMillis();
        final Path<PuzzleNode> path = finder.search(this.source, this.target);
        long tb = System.currentTimeMillis();
        
        System.out.println("Time searching: " + (tb - ta) + " ms.");
        
        return path;
    }
}
