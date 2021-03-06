package net.coderodde.jgs.model;

/**
 * This class abstracts away an actual implementation of graph edge weights.
 * 
 * @author Rodion Efremov
 * @param <T> the actual weight type such as Integer, Double, and so on.
 */
public abstract class Weight<T> {

    /**
     * Returns the neutral element such as zero (0).
     * 
     * @return the neutral element. 
     */
    public abstract T identity();
    
    /**
     * Applies the operation to <code>a</code> and <code>b</code>. Basically,
     * in <tt>jGS</tt> it is addition (<tt>+</tt>).
     * 
     * @param a the first element.
     * @param b the other element.
     * 
     * @return the result of the operation.
     */
    public abstract T append(final T a, final T b);
    
    /**
     * Returns the largest value possible for <code>T</code>.
     * 
     * @return the largest value.
     */
    public abstract T largest();
    
    /**
     * This is a dirty hack.
     * 
     * @param what the object to convert.
     * 
     * @return the input value.
     */
    public T convert(final Object what) {
        return (T) what;
    }
}
