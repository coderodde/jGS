package net.coderodde.jgs.support;

import java.util.Random;
import net.coderodde.jgs.DemoSuite;
import net.coderodde.jgs.Utilities.Triple;
import static net.coderodde.jgs.Utilities.bar;
import static net.coderodde.jgs.Utilities.createRandomDirectedGraphWithCoordinates;
import static net.coderodde.jgs.Utilities.title1;
import static net.coderodde.jgs.Utilities.title2;
import net.coderodde.jgs.model.Graph;
import net.coderodde.jgs.model.GraphNodeCoordinates;
import net.coderodde.jgs.model.Path;
import net.coderodde.jgs.model.PathFinder;
import net.coderodde.jgs.model.ds.MinPriorityQueue;
import net.coderodde.jgs.model.ds.support.BinomialHeap;
import net.coderodde.jgs.model.ds.support.DaryHeap;
import net.coderodde.jgs.model.ds.support.FibonacciHeap;
import net.coderodde.jgs.model.ds.support.PairingHeap;
import net.coderodde.jgs.model.support.AStarPathFinder;
import net.coderodde.jgs.model.support.BidirectionalDijkstraPathFinder;
import net.coderodde.jgs.model.support.DijkstraPathFinder;
import net.coderodde.jgs.model.support.DirectedGraphDoubleWeightFunction;
import net.coderodde.jgs.model.support.DirectedGraphNode;
import net.coderodde.jgs.model.support.DoubleWeight;
import net.coderodde.jgs.model.support.EuclidianDoubleHeuristicFunction;

public class DenseGraphShortestPathSuite implements DemoSuite {

    private static final int NODE_AMOUNT = 1000;
    private static final double EDGE_AMOUNT_FACTOR = 0.75;
    private static final double EDGE_LENGTH_FACTOR = 1.2;
    private static final double BOX_SIDE_LENGTH = 1000.0;
    
    private final long seed;
    private final DirectedGraphNode source;
    private final DirectedGraphNode target;
    private final DirectedGraphDoubleWeightFunction f;
    private final EuclidianDoubleHeuristicFunction hf;
    
    private Path<DirectedGraphNode> path;
    
    public DenseGraphShortestPathSuite() {
        this.seed = 1409665925756L; //System.currentTimeMillis();
        title1("DenseGraphShortestPathSuite.java, seed: " + seed);
        final Triple<Graph<DirectedGraphNode>,
                     DirectedGraphDoubleWeightFunction,
                     GraphNodeCoordinates> data =
                createRandomDirectedGraphWithCoordinates(
                        NODE_AMOUNT, 
                        (int)(EDGE_AMOUNT_FACTOR * NODE_AMOUNT * NODE_AMOUNT), 
                        BOX_SIDE_LENGTH,
                        BOX_SIDE_LENGTH,
                        EDGE_LENGTH_FACTOR,
                        "My graph",
                        new Random(seed));
        
        final Random r = new Random(seed);
        this.source = data.first.getByName("" + r.nextInt(data.first.size()));
        this.target = data.first.getByName("" + r.nextInt(data.first.size()));
        this.f = data.second;
        this.hf = new EuclidianDoubleHeuristicFunction(data.third);
        
        System.out.println("Data constructed.");
    }
    
    @Override
    public void run() {
        profileDijkstrasAlgorithm();
        profileBidirectionalDijkstrasAlgorithm();
        profileAStarAlgorithm();
        profileBidirectionalAStarAlgorithm();
        bar();
        System.out.println("Path length: " + f.getPathWeight(path));
        System.out.println();
    }
    
    private void profileDijkstrasAlgorithm() {
        title2("Profiling Dijkstra's algorithm");
        profileDijkstrasAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(2));
        profileDijkstrasAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(3));
        profileDijkstrasAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(4));
        profileDijkstrasAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(5));
        profileDijkstrasAlgorithmOn(new BinomialHeap<DirectedGraphNode, Double>());
        profileDijkstrasAlgorithmOn(new FibonacciHeap<DirectedGraphNode, Double>());
        profileDijkstrasAlgorithmOn(new PairingHeap<DirectedGraphNode, Double>()); 
    }
    
    private void profileBidirectionalDijkstrasAlgorithm() {
        title2("Profiling bidirectional Dijkstra's algorithm");
        profileBidirectionalDijkstrasAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(2));
        profileBidirectionalDijkstrasAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(3));
        profileBidirectionalDijkstrasAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(4));
        profileBidirectionalDijkstrasAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(5));
        profileBidirectionalDijkstrasAlgorithmOn(new BinomialHeap<DirectedGraphNode, Double>());
        profileBidirectionalDijkstrasAlgorithmOn(new FibonacciHeap<DirectedGraphNode, Double>());
        profileBidirectionalDijkstrasAlgorithmOn(new PairingHeap<DirectedGraphNode, Double>()); 
    }
    
    private void profileAStarAlgorithm() {
        title2("Profiling A* algorithm");
        profileAStarAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(2));
        profileAStarAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(3));
        profileAStarAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(4));
        profileAStarAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(5));
        profileAStarAlgorithmOn(new BinomialHeap<DirectedGraphNode, Double>());
        profileAStarAlgorithmOn(new FibonacciHeap<DirectedGraphNode, Double>());
        profileAStarAlgorithmOn(new PairingHeap<DirectedGraphNode, Double>()); 
    }
    
    private void profileBidirectionalAStarAlgorithm() {
        title2("Profiling bidirectional A* algorithm");
        profileBidirectionalAStarAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(2));
        profileBidirectionalAStarAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(3));
        profileBidirectionalAStarAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(4));
        profileBidirectionalAStarAlgorithmOn(new DaryHeap<DirectedGraphNode, Double>(5));
        profileBidirectionalAStarAlgorithmOn(new BinomialHeap<DirectedGraphNode, Double>());
        profileBidirectionalAStarAlgorithmOn(new FibonacciHeap<DirectedGraphNode, Double>());
        profileBidirectionalAStarAlgorithmOn(new PairingHeap<DirectedGraphNode, Double>()); 
    }
    
    private void profileDijkstrasAlgorithmOn(
            final MinPriorityQueue<DirectedGraphNode, Double> queue) {
        if (queue instanceof DaryHeap) {
            System.out.print(queue.getClass().getSimpleName() + ", degree " +
                             ((DaryHeap) queue).getDegree() + ": ");
        } else {
            System.out.print(queue.getClass().getSimpleName() + ": ");
        }
        
        long ta = System.currentTimeMillis();
        
        PathFinder<DirectedGraphNode, Double> pf = 
                new DijkstraPathFinder<>(f, new DoubleWeight(), queue.spawn());
        
        final Path<DirectedGraphNode> p = pf.search(source, target);
        
        long tb = System.currentTimeMillis();
        
        System.out.println("" + (tb - ta) + " ms.");
        
        if (path == null) {
            path = p;
        } else if (!path.equals(p)) {
            System.out.println("Algorithms disagreed. Latest: " + 
                               f.getPathWeight(path) +
                               ", current: " + f.getPathWeight(p));
        }
    }
    
    private void profileBidirectionalDijkstrasAlgorithmOn(
            final MinPriorityQueue<DirectedGraphNode, Double> queue) {
        if (queue instanceof DaryHeap) {
            System.out.print(queue.getClass().getSimpleName() + ", degree " +
                             ((DaryHeap) queue).getDegree() + ": ");
        } else {
            System.out.print(queue.getClass().getSimpleName() + ": ");
        }
        
        long ta = System.currentTimeMillis();
        
        PathFinder<DirectedGraphNode, Double> pf = 
                new BidirectionalDijkstraPathFinder<>(f, 
                                      new DoubleWeight(), 
                                      queue.spawn());
        
        final Path<DirectedGraphNode> p = pf.search(source, target);
        
        long tb = System.currentTimeMillis();
        
        System.out.println("" + (tb - ta) + " ms.");
        
        if (path == null) {
            path = p;
        } else if (!path.equals(p)) {
            System.out.println("Algorithms disagreed. Latest: " + 
                               f.getPathWeight(path) +
                               ", current: " + f.getPathWeight(p));
        }
    }
    
    private void profileAStarAlgorithmOn(
            final MinPriorityQueue<DirectedGraphNode, Double> queue) {
        if (queue instanceof DaryHeap) {
            System.out.print(queue.getClass().getSimpleName() + ", degree " +
                             ((DaryHeap) queue).getDegree() + ": ");
        } else {
            System.out.print(queue.getClass().getSimpleName() + ": ");
        }
        
        long ta = System.currentTimeMillis();
        
        PathFinder<DirectedGraphNode, Double> pf = 
                new AStarPathFinder<>(f, 
                                      hf, 
                                      new DoubleWeight(), 
                                      queue.spawn());
        
        final Path<DirectedGraphNode> p = pf.search(source, target);
        
        long tb = System.currentTimeMillis();
        
        System.out.println("" + (tb - ta) + " ms.");
        
        if (path == null) {
            path = p;
        } else if (!path.equals(p)) {
            System.out.println("Algorithms disagreed. Latest: " + 
                               f.getPathWeight(path) +
                               ", current: " + f.getPathWeight(p));
        }
    }
    
    private void profileBidirectionalAStarAlgorithmOn(
            final MinPriorityQueue<DirectedGraphNode, Double> queue) {
        
    }
}
