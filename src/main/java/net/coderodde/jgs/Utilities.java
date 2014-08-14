package net.coderodde.jgs;

import net.coderodde.jgs.model.Node;

/**
 * This class holds various utility methods.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class Utilities {

    /**
     * Hide the constructors.
     */
    private Utilities() {
        
    }
    
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
    public static final <T extends Node<T>>
        void checkNodesBelongToSameGraph(final T a, final T b) {
        if (a.getOwnerGraph() != b.getOwnerGraph()) {
            throw new IllegalStateException(
            "Nodes " + a.toString() + " and " + b.toString() + " do not " +
            "belong to the same graph. a's graph: " + 
            a.getOwnerGraph().toString() + ", b's graph: " +
            b.getOwnerGraph().toString());
        }
    }
}
