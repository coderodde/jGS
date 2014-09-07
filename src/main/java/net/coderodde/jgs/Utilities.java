package net.coderodde.jgs;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.coderodde.jgs.model.AbstractNode;
import net.coderodde.jgs.model.Graph;
import net.coderodde.jgs.model.GraphNodeCoordinates;
import net.coderodde.jgs.model.support.DirectedGraphDoubleWeightFunction;
import net.coderodde.jgs.model.support.DirectedGraphNode;
import net.coderodde.jgs.model.support.PuzzleNode;
import net.coderodde.jgs.model.support.UndirectedGraphDoubleWeightFunction;
import net.coderodde.jgs.model.support.UndirectedGraphNode;

/**
 * This class holds various utility methods.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class Utilities {

    /**
     * The length of the bar and titles.
     */
    private static final int BAR_LENGTH = 80;
    
    /**
     * The separator bar.
     */
    private static final String bar;
    
    static {
        final StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < BAR_LENGTH; ++i) {
            sb.append('-');
        }
        
        bar = sb.toString();
    }
    
    /**
     * Hide the constructors.
     */
    private Utilities() {}
    
    /**
     * Throws an <code>NullPointerException</code> with message
     * <code>errorMessage</code> if <code>reference</code> is <code>null</code>.
     * 
     * @param reference the reference to check.
     * @param errorMessage the error message to pass to 
     * <code>NullPointerException</code>.
     * 
     * @throws NullPointerException if <code>reference</code> if
     * <code>null</code>.
     */
    public static final void checkNotNull(final Object reference,
                                          final String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(errorMessage);
        }
    }

    /**
     * Throws an <code>IllegalStateException</code> if <code>a</code> and
     * <code>b</code> do not belong to the same graph.
     * 
     * @param <T> the actual node type extending 
     * <code>net.coderodde.jgs.model.Node</code>.
     * @param a a node.
     * @param b another node.
     */
    public static final <T extends AbstractNode<T>>
        void checkNodesBelongToSameGraph(final T a, final T b) {
        if (a.getOwnerGraph() != b.getOwnerGraph()) {
            throw new IllegalStateException(
            "Nodes " + a.toString() + " and " + b.toString() + " do not " +
            "belong to the same graph. a's graph: " + 
            a.getOwnerGraph().toString() + ", b's graph: " +
            b.getOwnerGraph().toString());
        }
    }
       
    public static final <T extends AbstractNode<T>> 
        void checkBelongsToGraph(final T node, final Graph<T> graph) {
            checkNotNull(graph, "The graph is null.");
            
            if (node.getOwnerGraph() == null) {
                throw new IllegalStateException(
                "The node " + node + " belongs to no graph.");
            } else if (node.getOwnerGraph() != graph) {
                throw new IllegalStateException(
                "The node " + node + " does not belong to the given graph.");
            }
        }
        
    /**
     * Checks that <code>d</code> is not infinite and if it is, throws an 
     * exceptions with error message <code>errorMessage</code>.
     * 
     * @param d the double to check.
     * @param errorMessage the error message to pass to the exception.
     */
    public static final void checkNotInfinite(final double d, 
                                              final String errorMessage) {
        if (Double.isInfinite(d)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Checks that <code>d</code> is not NaN and if it is, throws an exceptions
     * with error message <code>errorMessage</code>.
     * 
     * @param d the double to check.
     * @param errorMessage the error message to pass to the exception.
     */
    public static final void checkNotNaN(final double d,
                                         final String errorMessage) {
        if (Double.isNaN(d)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    /**
     * Checks that all the arrays are of equal length, and all contain "equal"
     * elements as specified by <code>equals</code>.
     * 
     * @param <T> the actual element type of lists.
     * @param lists the actual lists.
     * 
     * @return a boolean denoting equality. 
     */
    public static final <T> boolean eq(final List<T>... lists) {
        if (lists.length < 2) {
            throw new IllegalArgumentException("Too few integer lists.");
        }
        
        for (int i = 0; i < lists.length - 1; ++i) {
            if (lists[i].size() != lists[i + 1].size()) {
                return false;
            }
        }
        
        for (int i = 0; i < lists[0].size(); ++i) {
            for (int j = 0; j < lists.length - 1; ++j) {
                if (!lists[j].get(i).equals(lists[j + 1].get(i))) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Prints a first-level title.
     * 
     * @param title the title to print. 
     */
    public static void title1(final String title) {
        barImpl(title, '*');
    }
    
    /**
     * Prints a second-level title.
     * 
     * @param title the title to print. 
     */
    public static void title2(final String title) {
        barImpl(title, '-');
    }
        
    /**
     * Prints a separator bar.
     */
    public static void bar() {
        System.out.println(bar);
    }
    
    /**
     * This class defines a two-tuple.
     * 
     * @param <F> the type of the first component.
     * @param <S> the type of the second component.
     */
    public static final class Pair<F, S> {
        public F first;
        public S second;
        
        /**
         * Constructs a new pair with the given components.
         * 
         * @param first the first component.
         * @param second the second component.
         */
        public Pair(final F first, final S second) {
            this.first = first;
            this.second = second;
        }
    }
    
    /**
     * Defines a triple.
     * 
     * @param <F> the type of the first component.
     * @param <S> the type of the second component.
     * @param <T> the type of the third component.
     */
    public static final class Triple<F, S, T> {
        public F first;
        public S second;
        public T third;
        
        /**
         * Constructs a triple.
         * 
         * @param first the first component.
         * @param second the second component.
         * @param third the third component.
         */
        public Triple(final F first, final S second, final T third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }
    
    /**
     * Checks that the integer <code>what</code> is not below 
     * <code>minimum</code>.
     * 
     * @param what the integer to check.
     * @param minimum the lower bound.
     * @param message the message upon exception.
     */
    public static void checkNotBelow(final int what,
                                     final int minimum,
                                     final String message) {
        if (what < minimum) {
            throw new IllegalArgumentException(message);
        }
    }
    
    /**
     * Creates a directed graph, its weight function and the coordinate map to 
     * be passed to the heuristic function.
     * 
     * @param size the amount of nodes in the graph.
     * @param edges the amount of edges in the graph.
     * @param width the width of the bounding box.
     * @param height the height of the bounding box.
     * @param edgeFactor the factor by which the actual distance between two 
     * end nodes will be multiplied as to make the heuristic function
     * <tt>optimistic</tt>; must be above 1.0.
     * @param rnd the random number generator.
     * 
     * @return a triple containing the graph, weight function and the map
     * mapping the nodes to their respective coordinates.
     */
    public static final Triple<Graph<DirectedGraphNode>, 
                               DirectedGraphDoubleWeightFunction,
                               GraphNodeCoordinates>
            createRandomDirectedGraphWithCoordinates(final int size,
                                                     final int edges,
                                                     final double width,
                                                     final double height,
                                                     final double edgeFactor,
                                                     final Random rnd) {
        final Graph<DirectedGraphNode> graph = new Graph<>();
        final GraphNodeCoordinates coords = new GraphNodeCoordinates();
        final List<DirectedGraphNode> list = new ArrayList<>(size);
        final DirectedGraphDoubleWeightFunction f = 
          new DirectedGraphDoubleWeightFunction();
        
        for (int i = 0; i < size; ++i) {
            final DirectedGraphNode node = new DirectedGraphNode();
            graph.addNode(node);
            list.add(node);
            
            final double x = width * rnd.nextDouble();
            final double y = height * rnd.nextDouble();
            
            coords.put(node, new Point.Double(x, y));
        }
        
        int e = edges;
        final int maxEdges = (int)(0.8 * size * size);
        
        if (e > maxEdges) {
            e = maxEdges;
        }
        
        while (e > 0) {
            final DirectedGraphNode tail = list.get(rnd.nextInt(size));
            final DirectedGraphNode head = list.get(rnd.nextInt(size));
            tail.connectTo(head);
            f.put(tail, 
                  head, 
                  edgeFactor * coords.get(tail).distance(coords.get(head)));
            --e;
        }
         
        return new Triple<>(graph, f, coords);
    }
    
    public static final PuzzleNode stepAway(final PuzzleNode from,
                                            int steps,
                                            final Random r) {
        PuzzleNode current = from;
        
        while (steps > 0) {
            PuzzleNode next = null;
            
            switch (r.nextInt(4)) {
                case 0:
                    // Move up.
                    next = current.moveUp();
                    break;
                    
                case 1:
                    // Move right.
                    next = current.moveRight();
                    break;
                    
                case 2:
                    // Move down.
                    next = current.moveDown();
                    break;
                    
                case 3:
                    // Move left.
                    next = current.moveLeft();
                    break;
            }
            
            if (next != null) {
                current = next;
                --steps;
            }
        }
        
        return current;
    }
            
    /**
     * 
     * @param width
     * @param height
     * @param obstacleFactor
     * @param rnd
     * @return 
     */
    public static final Triple<Graph<UndirectedGraphNode>,
                               UndirectedGraphDoubleWeightFunction,
                               Set<UndirectedGraphNode>> 
            createGridGraphWithObstacles(final int width,
                                         final int height,
                                         float obstacleFactor,
                                         final Random rnd) {
        final Graph<UndirectedGraphNode> grid = new Graph<>();
        
        final UndirectedGraphDoubleWeightFunction f =
                new UndirectedGraphDoubleWeightFunction();
        
        final UndirectedGraphNode[][] nodeMatrix = 
                new UndirectedGraphNode[height][width];
        
        final Double SQRT2 = Math.sqrt(2.0);
        
        // Create all the nodes.
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                final UndirectedGraphNode node = 
                        new UndirectedGraphNode();
                nodeMatrix[y][x] = node;
            }
        }
        
        // Create horizontal edges.
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width - 1; ++x) {
                final UndirectedGraphNode a = nodeMatrix[y][x];
                final UndirectedGraphNode b = nodeMatrix[y][x + 1];
                
                a.connectTo(b);
                
                f.put(a, b, 1.0);
            }
        }
        
        // Create vertical edges;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height - 1; ++y) {
                final UndirectedGraphNode a = nodeMatrix[y][x];
                final UndirectedGraphNode b = nodeMatrix[y + 1][x];
                
                a.connectTo(b);
                
                f.put(a, b, 1.0);
            }
        }
        
        // Create /-diagonal edges.
        for (int y = 0; y < height - 1; ++y) {
            for (int x = 1; x < width; ++x) {
                final UndirectedGraphNode a = nodeMatrix[y][x];
                final UndirectedGraphNode b = nodeMatrix[y + 1][x - 1];
                
                a.connectTo(b);
                
                f.put(a, b, SQRT2);
            }
        }
        
        // Create \-diagonal edges. 
        for (int y = 0; y < height - 1; ++y) {
            for (int x = 0; x < width - 1; ++x) {
                final UndirectedGraphNode a = nodeMatrix[y][x];
                final UndirectedGraphNode b = nodeMatrix[y + 1][x + 1];
                
                a.connectTo(b);
                
                f.put(a, b, SQRT2);
            }
        }
        
        if (obstacleFactor > 0.9f) {
            obstacleFactor = 0.9f;
        }
        
        int obstacleCount = (int)(obstacleFactor * width * height);
        
        Set<UndirectedGraphNode> obstacleNodes = new HashSet<>();
        
        while (obstacleNodes.size() < obstacleCount) {
            obstacleNodes.add(nodeMatrix[rnd.nextInt(height)]
                                        [rnd.nextInt(width)]);
        }
        
        return new Triple<>(grid, f, obstacleNodes);
    }
            
    /**
     * The actual implementation of title-printing methods.
     * 
     * @param title the title to print.
     * @param ch the character to draw the bars.
     */
    private static void barImpl(String title, final char ch) {
        title = title.trim();
        final StringBuilder sb = new StringBuilder(80);
       
        final int messageLength = title.length();
        final int precedingChars = (80 - messageLength - 2) >>> 1;
        final int followingChars = 80 - messageLength - 2 - precedingChars;
        
        for (int i = 0; i < precedingChars; ++i) {
            sb.append(ch);
        }
        
        sb.append(' ').append(title.trim()).append(' ');
        
        for (int i = 0; i < followingChars; ++i) {
            sb.append(ch);
        }
        
        System.out.println(sb.toString());
    }
}
