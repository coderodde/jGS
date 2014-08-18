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
        
    public static void title1(final String title) {
        barImpl(title, '*');
    }
    
    public static void title2(final String title) {
        barImpl(title, '-');
    }
        
    public static void bar() {
        System.out.println(bar);
    }
    
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
