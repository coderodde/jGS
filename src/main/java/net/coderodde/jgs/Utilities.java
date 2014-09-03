package net.coderodde.jgs;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.coderodde.jgs.model.AbstractNode;
import net.coderodde.jgs.model.Graph;
import net.coderodde.jgs.model.GraphNodeCoordinates;
import net.coderodde.jgs.model.support.DirectedGraphDoubleWeightFunction;
import net.coderodde.jgs.model.support.DirectedGraphNode;

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
     * @param graphName the graph name.
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
                                                     final String graphName,
                                                     final Random rnd) {
        final Graph<DirectedGraphNode> graph = new Graph<>(graphName);
        final GraphNodeCoordinates coords = new GraphNodeCoordinates();
        final List<DirectedGraphNode> list = new ArrayList<>(size);
        final DirectedGraphDoubleWeightFunction f = 
          new DirectedGraphDoubleWeightFunction();
        
        for (int i = 0; i < size; ++i) {
            final DirectedGraphNode node = new DirectedGraphNode("" + i);
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
