package net.coderodde.jgs.support;

import java.util.Random;
import net.coderodde.jgs.DemoSuite;
import static net.coderodde.jgs.Utilities.bar;
import static net.coderodde.jgs.Utilities.createRandomDirectedUnweightedGraph;
import static net.coderodde.jgs.Utilities.title1;
import static net.coderodde.jgs.Utilities.title2;
import net.coderodde.jgs.model.Graph;
import net.coderodde.jgs.model.Path;
import net.coderodde.jgs.model.PathFinder;
import net.coderodde.jgs.model.support.BidirectionalBreadthFirstSearchPathFinder;
import net.coderodde.jgs.model.support.BreadthFirstSearchPathFinder;
import net.coderodde.jgs.model.support.DirectedGraphNode;

public class UnweightedGraphSearchSuite implements DemoSuite {

    private static final int SIZE = 1000;
    private static final float NODE_DEGREE = 25f;
    private final long seed;
    private DirectedGraphNode source;
    private DirectedGraphNode target;
    private final Graph<DirectedGraphNode> graph; 
    private Path<DirectedGraphNode> path1;
    private Path<DirectedGraphNode> path2;
    private final Random rnd;
    public UnweightedGraphSearchSuite() {
        this.seed = System.currentTimeMillis();
        title1("UnweightedGraphSearchSuite.java, seed: " + seed);
        this.rnd = new Random(seed);
        this.graph = 
                createRandomDirectedUnweightedGraph(SIZE, 
                                                    (int)(SIZE * NODE_DEGREE), 
                                                    rnd);
        this.source = graph.get(rnd.nextInt(graph.size()));
        this.target = graph.get(rnd.nextInt(graph.size()));
        
        System.out.println("Data constructed.");
    }
    
    @Override
    public void run() {
        int i = 0;
        do {
            profileBreadthFirstSearch();
            profileBidirectionalBreadthFirstSearch();
            bar();     
            if (++i > 500) {
                System.out.println("No luck.");
                break;
            }
            this.source = graph.get(rnd.nextInt(graph.size()));
            this.target = graph.get(rnd.nextInt(graph.size()));
        } while (!report());
//        report();
    }

    private void profileBreadthFirstSearch() {
        final PathFinder<DirectedGraphNode, ?> finder =
                new BreadthFirstSearchPathFinder<>();
        
        title2(finder.getClass().getSimpleName());
        
        final long ta = System.currentTimeMillis();
        
        path1 = new BreadthFirstSearchPathFinder()
                .search(source, target);
        
        final long tb = System.currentTimeMillis();
        
        System.out.println("Time: " + (tb - ta) + " ms.");
    }
    
    private void profileBidirectionalBreadthFirstSearch() {
        final PathFinder<DirectedGraphNode, ?> finder =
                new BidirectionalBreadthFirstSearchPathFinder<>();
        
        title2(finder.getClass().getSimpleName());
        
        final long ta = System.currentTimeMillis();
        
        path2 = new BreadthFirstSearchPathFinder()
                .search(source, target);
        
        final long tb = System.currentTimeMillis();
        
        System.out.println("Time: " + (tb - ta) + " ms."); 
    }
    
    private boolean report() {
        if (path1.isEmpty()) {
            if (path2.isEmpty()) {
                System.out.println(
                        "Paths O.K. " + 
                        "(Both finders did not find a path.)");
            } else {
                System.out.println(
                        "Failure: Unidirectional BFS did not find a path.");
            }
        } else {
            if (path2.isEmpty()) {
                System.out.println(
                        "Failure: Bidirectional BFS did not find a path.");
            } else {
                if (path1.size() == path2.size()) {
                    if ((path1.get(0).equals(path2.get(0))) 
                            && (path1.get(path1.size() - 1))
                               .equals(path2.get(path2.size() - 1))) {
                        System.out.println("Success! Paths are equal.");
                    } else {
                        System.out.println("Sources or target nodes mismatch.");
                    }
                } else {
                    System.out.println(
                        "Failure: paths have different lengths: " +
                        path1.size() + " vs. " + path2.size());
                    return true;
                }
            }
        }
        
        System.out.println();
        return false;
    }
}
