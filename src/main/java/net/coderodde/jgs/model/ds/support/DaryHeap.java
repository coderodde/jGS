package net.coderodde.jgs.model.ds.support;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import net.coderodde.jgs.model.ds.MinPriorityQueue;

/**
 * This class implements <tt>d</tt>-ary heap. <tt>d</tt> is the "degree" of the
 * heap, or namely the maximum amount of children of a node. 
 * 
 * @author Rodion Efremov
 * @version 1.6
 * @param <E> the element type.
 * @param <P> the priority key type.
 */
public class DaryHeap<E, P extends Comparable<? super P>> 
extends MinPriorityQueue<E, P> {
    
    /**
     * The minimum storage capacity.
     */
    private static final int MINIMUM_CAPACITY = 128;
    
    /**
     * The default storage capacity.
     */
    private static final int DEFAULT_CAPACITY = 1024;
    
    /**
     * The minimum degree of the heaps. (Signifies the binary heap.)
     */
    private static final int MINIMUM_DEGREE = 2;
    
    /**
     * The default degree of the heaps.
     */
    private static final int DEFAULT_DEGREE = 2;

    /**
     * Stores an element, its priority, and its index in the storage array.
     * 
     * @param <E> the element type.
     * @param <P> the priority key type.
     */
    private static class Node<E, P extends Comparable<? super P>> {
        
        Node(final E element, final P priority, final int index) {
            this.element = element;
            this.priority = priority;
            this.index = index;
        }
        
        /**
         * The actual element.
         */
        E element;
        
        /**
         * The priority of the element.
         */
        P priority;
        
        /**
         * The index of this node in the storage array.
         */
        int index;
    }
    
    /**
     * The actual degree of this heap.
     */
    private final int degree;
    
    /**
     * The actual storage array.
     */
    private Node[] storage;
    
    /**
     * The map mapping elements to their respective storage nodes.
     */
    private Map<E, Node<E, P>> map;
    
    /**
     * Holds the array of indices as to avoid creating index arrays
     * every time we are doing something.
     */
    private int[] indices;
    
    /**
     * Caches the amount of elements in this heap.
     */
    private int size;
    
    /**
     * Constructs a new <tt>d</tt>-ary heap with given degree and capacity.
     * 
     * @param degree the degree of this heap.
     * @param capacity the capacity of this heap.
     */
    public DaryHeap(final int degree, final int capacity) {
        checkDegree(degree);
        checkCapacity(capacity);
        this.degree = degree;
        this.storage = new Node[capacity];
        this.indices = new int[degree];
        this.map = new HashMap<>(capacity);
    }
    
    /**
     * Constructs a new heap with degree <code>degree</code> and default 
     * capacity.
     * 
     * @param degree the degree of this heap.
     */
    public DaryHeap(final int degree) {
        this(degree, DEFAULT_CAPACITY);
    }
    
    /**
     * Construct a new heap with default parameters.
     */
    public DaryHeap() {
        this(DEFAULT_DEGREE, DEFAULT_CAPACITY);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @param element the element to add. Must not be already in the heap.
     * @param priority the priority of <code>element</code>.
     */
    @Override
    public void add(final E element, final P priority) {
        if (map.containsKey(element)) {
            return;
        }
        
        checkAndExpand();
        Node<E, P> node = new Node<>(element, priority, size);
        storage[size] = node;
        map.put(element, node);
        siftUp(size);
        ++size;
    }

    /**
     * {@inheritDoc}
     * 
     * @param element the element whose priority key to decrease.
     * @param newPriority the new priority of the element.
     */
    @Override
    public void decreasePriority(E element, P newPriority) {
        Node<E, P> node = map.get(element);
        
        if (node == null 
                || node.index == 0
                || node.priority.compareTo(newPriority) <= 0) {
            return;
        }
        
        node.priority = newPriority;
        siftUp(node.index);
    }

    /**
     * {@inheritDoc}
     * 
     * @return the element with the least priority key. 
     */
    @Override
    public E extractMinimum() {
        if (size == 0) {
            throw new NoSuchElementException(
            "Reading from an empty d-ary heap.");
        }
        
        E ret = ((Node<E, P>) storage[0]).element;
        map.remove(ret);
        Node<E, P> node = (Node<E, P>) storage[--size];
        storage[size] = null; // For the sake of garbage collection.
        
        if (size != 0) {
            storage[0] = node;
            node.index = 0;
            siftDown(0);
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @return the element with the least priority key.
     */
    @Override
    public E min() {
        if (size == 0) {
            throw new NoSuchElementException("Reading from an empty queue.");
        }
        
        return ((Node<E, P>) storage[0]).element;
    }

    /**
     * {@inheritDoc}
     * 
     * @return the amount of elements in this heap.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     * 
     * @return <code>true</code> if this heap is empty; 
     * <code>false</code> otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        // Nullify the storage array, so that the garbage collector can collect.
        for (int i = 0; i != size; ++i) {
            storage[i] = null;
        }
        
        map.clear();
        size = 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @return an empty binomial heap.
     */
    @Override
    public MinPriorityQueue<E, P> spawn() {
        return new DaryHeap<>(this.degree);
    }

    /**
     * Returns the degree of this <tt>d</tt>-ary heap.
     * 
     * @return the degree of this heap.
     */
    public int getDegree() {
        return degree;
    }

    /**
     * {@inheritDoc}
     * 
     * @return the string indicating the implementation type.
     */
    @Override
    public String toString() {
        return "DaryHeap with degree " + degree;
    }
    
    /**
     * Checks that the degree is not less than the minimum degree, which is 2.
     * 
     * @param degree the degree to check.
     */
    private void checkDegree(final int degree) {
        if (degree < MINIMUM_DEGREE) {
            throw new IllegalArgumentException(
            "Degree must be at least " + MINIMUM_DEGREE +
            ", received " + degree + ".");
        }
    }
    
    /**
     * Checks the capacity is not less than minimum capacity.
     * 
     * @param capacity the capacity to check.
     */
    private void checkCapacity(final int capacity) {
        if (capacity < MINIMUM_CAPACITY) {
            throw new IllegalArgumentException(
            "Capacity must be at least " + MINIMUM_CAPACITY +
            ", received " + capacity + ".");
        }
    }
    
    /**
     * If the storage array is full, expands it and copies the old data into it.
     */
    private void checkAndExpand() {
        if (size == storage.length) {
            Node[] arr = new Node[3* size / 2];
            System.arraycopy(storage, 0, arr, 0, size);
            storage = arr;
        }
    }
    
    /**
     * Loads the array <code>indices</code> with the indices of children nodes 
     * of the node at index <code>index</code>.
     * 
     * @param index the index of the node whose child indices to load. 
     */
    private final void computeChildrenIndices(final int index) {
        for (int i = 0; i != degree; ++i) {
            indices[i] = degree * index + i + 1;
            
            if (indices[i] >= size) {
                indices[i] = -1;
                break;
            }
        }
    }
    
    /**
     * Computes and returns the index of the parent of the node at index
     * <code>index</code>.
     * 
     * @param index the index whose parent's index to compute.
     * @return return the parent index.
     */
    private int getParentIndex(final int index) {
        return (index - 1) / degree;
    }
    
    /**
     * Sifts the node at index <code>index</code> until <tt>d</tt>-ary heap 
     * invariant is fixed.
     * 
     * @param index the index of the node to sift up.
     */
    private void siftUp(int index) {
        if (index == 0) {
            return;
        }
        
        int parentIndex = getParentIndex(index);
        Node<E, P> target = (Node<E, P>) storage[index];
        
        for (;;) {
            Node<E, P> parent = (Node<E, P>) storage[parentIndex];
            
            if (parent.priority.compareTo(target.priority) > 0) {
                storage[index] = parent;
                parent.index = index;
                
                index = parentIndex;
                parentIndex = getParentIndex(index);
            } else {
                break;
            }
            
            if (index == 0) {
                break;
            }
        }
        
        storage[index] = target;
        target.index = index;
    }
    
    /**
     * Sifts a node at index <code>index</code> down until <tt>d</tt>-ary heap
     * invariant is fixed.
     * 
     * @param index the index of the node to sift down.
     */
    private void siftDown(int index) {
        final Node<E, P> target = (Node<E, P>) storage[index];
        final P priority = target.priority;
        
        for (;;) {
            P minChildPriority = priority;
            int minChildIndex = -1;
            computeChildrenIndices(index);
            
            for (int i : indices) {
                if (i == -1) {
                    break;
                }
                
                final P tentative = ((Node<E, P>) storage[i]).priority;
                
                if (minChildPriority.compareTo(tentative) > 0) {
                    minChildPriority = tentative;
                    minChildIndex = i;
                }
            }
            
            if (minChildIndex == -1) {
                storage[index] = target;
                target.index = index;
                return;
            }
            
            storage[index] = storage[minChildIndex];
            storage[index].index = index;
            
            // Go for the next iteration.
            index = minChildIndex;
        }
    }
}
