package net.coderodde.jgs.model.ds;

/**
 * This abstract class defines the API for the various minimum-priority queue
 * data structures.
 * 
 * @author Rodion Efremov
 * @param <E> the type of elements stored by the implementation.
 * @param <P> the type of the priority keys.
 * @version 1.6
 */
public abstract class MinPriorityQueue<E, P extends Comparable<? super P>> {

    /**
     * Adds <code>element</code> and assigns to it the priority 
     * <code>priority</code>.
     * 
     * @param element the element to store.
     * @param priority the priority of the element.
     */
    public abstract void add(final E element, final P priority);
    
    /**
     * Decreases the priority of the element <code>element</code> if it is 
     * present. If the element is not in this heap, or new priority does not
     * improve the current priority, does nothing.
     * 
     * @param element the element whose priority to decrease.
     * @param newPriority the new priority of <code>element</code>.
     */
    public abstract void decreasePriority(final E element, final P newPriority);
    
    /**
     * Extracts the element with the lowest priority.
     * 
     * @return the element with the lowest priority.
     * 
     * @throws java.util.NoSuchElementException if the heap is empty.
     */
    public abstract E extractMinimum();
    
    /**
     * Returns but does not remove the minimum element.
     * 
     * @return the minimum element. 
     */
    public abstract E min();
    
    /**
     * Returns the amount of elements in the heap.
     * 
     * @return the amount of elements in the heap. 
     */
    public abstract int size();
    
    /**
     * Returns <code>true</code> it this heap is empty. <code>false</code> 
     * otherwise.
     * 
     * @return <code>true</code> or <code>false</code>.
     */
    public abstract boolean isEmpty();
    
    /**
     * Removes all elements from this heap.
     */
    public abstract void clear();
    
    /**
     * Spawns another empty heap with the same implementation.
     * 
     * @return another empty heap.
     */
    public abstract MinPriorityQueue<E, P> spawn();
    
    /**
     * Returns a string indicating the actual implementation type.
     * 
     * @return a string indicating implementation type.
     */
    public abstract String toString();
}
