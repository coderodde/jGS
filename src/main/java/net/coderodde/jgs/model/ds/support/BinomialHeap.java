package net.coderodde.jgs.model.ds.support;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import net.coderodde.jgs.model.ds.MinPriorityQueue;

/**
 * This class implements binomial heap.
 * 
 * @author Rodion Efremov
 * @version 1.6
 * @param <E> the element type.
 * @param <P> the type of priority keys.
 */
public class BinomialHeap<E, P extends Comparable<? super P>>
extends MinPriorityQueue<E, P> {

    /**
     * The default map capacity.
     */
    private static final int DEFAULT_MAP_CAPACITY = 1 << 10;
    
    /**
     * This class implements a binomial tree in a binomial heap.
     * 
     * @param <E> the element type.
     * @param <P> the type of priority keys.
     */
    private static final class BinomialTree<E, P> {
        
        /**
         * The actual element of this node.
         */
        E element;
        
        /**
         * The priority key of this node.
         */
        P priority;
        
        /**
         * The parent node.
         */
        BinomialTree<E, P> parent;
        
        /**
         * Immediate sibling of this node to the right.
         */
        BinomialTree<E, P> sibling;
        
        /**
         * The leftmost child of this node.
         */
        BinomialTree<E, P> child;
        
        /**
         * The amount of children of this node.
         */
        int degree;
        
        /**
         * Constructs a new node and initialize it with mandatory data.
         * 
         * @param element the element to store in this node.
         * @param priority the priority of the element stored.
         */
        BinomialTree(E element, P priority) {
            this.element = element;
            this.priority = priority;
        }
    }
    
    /**
     * Caches the amount of elements in this binomial heap.
     */
    private int size;
    
    /**
     * Points to the leftmost node in the root list of this heap.
     */
    private BinomialTree<E, P> head;
    
    /**
     * Maps each element in the heap to its respective node.
     */
    private final Map<E, BinomialTree<E, P>> map;
    
    /**
     * Constructs a new {@code BinomialHeap} with default settings.
     */
    public BinomialHeap() {
        this(DEFAULT_MAP_CAPACITY);
    }
    
    /**
     * Constructs a new {@code BinomialHeap} using <code>mapCapacity</code> as 
     * the initial capacity for the underlying map.
     * 
     * @param mapCapacity the initial map capacity.
     */
    public BinomialHeap(final int mapCapacity) {
        this.map = new HashMap<>(mapCapacity);
    }
    
    /**
     * Constructs a binomial heap with only one element. Used for the sake of 
     * <code>add</code>-operation, which simply unites the current heap with the
     * one created by this constructor.
     * 
     * @param element the application-specific satellite data.
     * @param priority the priority of <code>element</code>.
     */
    private BinomialHeap(final E element, final P priority) {
        BinomialTree<E, P> tree = new BinomialTree<>(element, priority);
        head = tree;
        size = 1;
        map = null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void add(E element, P priority) {
        if (map.containsKey(element)) {
            // element already in this heap, use decreaseKey instead.
            return;
        }
        
        BinomialHeap<E, P> h = new BinomialHeap<>(element, priority);
        
        if (size == 0) {
            this.head = h.head;
            this.map.put(element, this.head);
            this.size = 1;
        } else {
            heapUnion(h.head);
            this.map.put(element, h.head);
            this.size++;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @param element the element whose priority key to decrease.
     * @param newPriority the new priority key.
     */
    @Override
    public void decreasePriority(E element, P newPriority) {
        if (!map.containsKey(element)) {
            // No element here.
            return;
        } 
        
        BinomialTree<E, P> target = map.get(element);
        
        if (target.priority.compareTo(newPriority) <= 0) {
            // The priority key of element won't improve.
            return;
        }
        
        target.priority = newPriority;
        
        BinomialTree<E, P> z = target.parent;
        BinomialTree<E, P> y = target;
        
        while (z != null && y.priority.compareTo(z.priority) < 0) {
            // Exchange priority keys.
            P tmp = y.priority;
            y.priority = z.priority;
            z.priority = tmp;
            
            // Exchange satellite data elements.
            E tmp2 = y.element;
            y.element = z.element;
            z.element = tmp2;
            
            // Move one level up.
            y = z;
            z = z.parent;
        }
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
                    "Reading from an empty binomial heap.");
        }
        
        BinomialTree<E, P> x = head;
        BinomialTree<E, P> prevx = null;
        BinomialTree<E, P> best = x;
        BinomialTree<E, P> bestprev = null;
        P minPriorityKey = x.priority;
        
        // Find the tree T with the least priority element and the tree 
        // preceding T.
        while (x != null) {
            if (minPriorityKey.compareTo(x.priority) > 0) {
                minPriorityKey = x.priority;
                best = x;
                bestprev = prevx;
            }
            
            prevx = x;
            x = x.sibling;
        }
        
        // Remove from root list the tree with the least priority root.
        if (bestprev == null) {
            head = best.sibling;
        } else {
            bestprev.sibling = best.sibling;
        }
        
        // Unite this heap with the reversed list of children of the tree whose
        // root contained the extracted element.
        heapUnion(reverseRootList(best.child));
        --size;
        return best.element;
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
     * @return <code>true</code> if this heap is empty, <code>false</code>
     * otherwise.
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
        this.head = null;
        this.map.clear();
        this.size = 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @return an empty binomial heap.
     */
    @Override
    public MinPriorityQueue<E, P> spawn() {
       return new BinomialHeap<>();
    }

    /**
     * Makes <code>y</code> a leftmost child of <code>z</code>.
     * 
     * @param y the node to become a child of <code>z</code>.
     * @param z the node to become a parent of <code>y</code>.
     */
    private void link(final BinomialTree<E, P> child, 
                      final BinomialTree<E, P> parent) {
        child.parent = parent;
        child.sibling = parent.child;
        parent.child = child;
        parent.degree++;
    }
    
    /**
     * Merges the root lists of this heap and <code>other</code>.
     * 
     * @param other another binomial heap whose root list to merge.
     * 
     * @return the head of the merged root list. 
     */
    private BinomialTree<E, P> mergeRoots(final BinomialTree<E, P> other) {
        BinomialTree<E, P> a = head;
        BinomialTree<E, P> b = other;
        
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        }
        
        BinomialTree<E, P> rootListHead;
        BinomialTree<E, P> rootListTail;
        
        // Initialize rootListHead and rootListTail.
        if (a.degree < b.degree) {
            rootListHead = a;
            rootListTail = a;
            a = a.sibling;
        } else {
            rootListHead = b;
            rootListTail = b;
            b = b.sibling;
        }
        
        while (a != null && b != null) {
            if (a.degree < b.degree) {
                rootListTail.sibling = a;
                rootListTail = a;
                a = a.sibling;
            } else {
                rootListTail.sibling = b;
                rootListTail = b;
                b = b.sibling;
            }
        }
        
        if (a != null) {
            // Just append the rest.
            rootListTail.sibling = a;
        } else {
            // Just append the rest.
            rootListTail.sibling = b;
        }
        
        return rootListHead;
    }
    
    /**
     * Reverses the root list as to facilitate the <code>extractMinimum</code>.
     * Sets the parent references to <code>null</code> also.
     * 
     * @param first the head node of the root list to reverse.
     * 
     * @return the reversed root list. 
     */
    private BinomialTree<E, P> reverseRootList(final BinomialTree<E, P> first) {
        BinomialTree<E, P> tmp = first; // This is the cursor over the list.
        BinomialTree<E, P> tmpnext;
        BinomialTree<E, P> newHead = null;
     
        while (tmp != null) {
            tmpnext = tmp.sibling;
            tmp.sibling = newHead;
            newHead = tmp;
            tmp = tmpnext;
        }
        
        return newHead;
    }
    
    /**
     * Unites this heap with <code>other</code>. This subroutine is used in both
     * <code>add</code> and <code>extractMinimum</code>.
     * 
     * @param other the heap to unite with this heap. 
     */
    private void heapUnion(final BinomialTree<E, P> other) {
        if (other == null) {
            return;
        }
        
        BinomialTree<E, P> t = mergeRoots(other);
        BinomialTree<E, P> prev = null;
        BinomialTree<E, P> x = t;
        BinomialTree<E, P> next = x.sibling;
        
        while (next != null) {
            if ((x.degree != next.degree)
                    || (next.sibling != null 
                    && next.sibling.degree == x.degree)) {
                prev = x;
                x = next;
            } else if (x.priority.compareTo(next.priority) <= 0) {
                x.sibling = next.sibling;
                link(next, x);
            } else {
                if (prev == null) {
                    t = next;
                } else {
                    prev.sibling = next;
                }
                
                link(x, next);
                x = next;
            }
            
            next = x.sibling;
        }
        
        this.head = t;
    }
}
