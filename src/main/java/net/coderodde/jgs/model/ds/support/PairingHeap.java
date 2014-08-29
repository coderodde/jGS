package net.coderodde.jgs.model.ds.support;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import net.coderodde.jgs.model.ds.MinPriorityQueue;

/**
 * This class implements pairing heaps.
 * 
 * @author Rodion Efremov
 * @param <E> the element type.
 * @param <P> the priority key type.
 */
public class PairingHeap<E, P extends Comparable<? super P>>
extends MinPriorityQueue<E, P> {

    /**
     * The default map capacity.
     */
    private static final int DEFAULT_MAP_CAPACITY = 1 << 10;
    
    /**
     * This class implements a tree within a pairing heap.
     * 
     * @param <E> the element type.
     * @param <P> the priority key type.
     */
    private static final class PairingHeapTree<E, P> {
        
        /**
         * The actual application-specific element.
         */
        E element;
        
        /**
         * The priority key of <code>element</code>.
         */
        P priority;
        
        /**
         * A reference to the parent node. Used as to speed up decreasing the 
         * priority keys.
         */
        PairingHeapTree<E, P> parent;
        
        /**
         * A reference to a sibling tree.
         */
        PairingHeapTree<E, P> next;
        
        /**
         * A reference to the leftmost child.
         */
        PairingHeapTree<E, P> child;
        
        /**
         * The amount of elements in this tree.
         */
        int size;
        
        /**
         * Constructs a new pairing heap tree with mandatory data.
         * 
         * @param element the element to set.
         * @param priority the priority key to set.
         */
        PairingHeapTree(final E element, final P priority) {
            this.element = element;
            this.priority = priority;
            this.size = 1;
        }
    }
    
    /**
     * The root pairing tree.
     */
    private PairingHeapTree<E, P> root;
    
    /**
     * The map mapping each element in the heap to its pairing tree as to speed
     * up the <code>decreasePriority</code>-operation.
     */
    private final Map<E, PairingHeapTree<E, P>> map;
    
    /**
     * The list of children of a removed root.
     */
    private final Deque<PairingHeapTree<E, P>> list;
    
    /**
     * Constructs a new {@code PairingHeap} with default settings.
     */
    public PairingHeap() {
        this(DEFAULT_MAP_CAPACITY);
    }
    
    /**
     * Constructs a new {@code PairingHeap} with the given map capacity.
     * 
     * @param mapCapacity the initial capacity of the underlying map.
     */
    public PairingHeap(final int mapCapacity) {
        this.map = new HashMap<>(mapCapacity);
        this.list = new ArrayDeque<>();
    }
    
    /**
     * A private constructor as to facilitate the insertion operation.
     * 
     * @param element the only element of this heap.
     * @param priority the priority key of the element.
     */
    private PairingHeap(final E element, final P priority) {
        this.root = new PairingHeapTree<>(element, priority);
        this.map = null;
        this.list = null;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @param element the element to add.
     * @param priority the priority of the element.
     */
    @Override
    public void add(final E element, final P priority) {
        if (map.containsKey(element)) {
            // element alread in this heap, use decreasePriority instead.
            return;
        }
        
        final PairingHeapTree<E, P> tree = new PairingHeapTree<>(element,
                                                                 priority);
        root = merge(root, tree);
        map.put(element, tree);
    }

    /**
     * {@inheritDoc}
     * 
     * @param element the element whose priority key to decrease.
     * @param newPriority the new priority.
     */
    @Override
    public void decreasePriority(final E element, final P newPriority) {
        final PairingHeapTree<E, P> tree = map.get(element);
        
        if (tree == null) {
            // element not in this heap, do no more.
            return;
        }
        
        if (tree.priority.compareTo(newPriority) <= 0) {
            // Nothing to improve.
            return;
        }
        
        if (tree != root) {
            // Go up.
            final PairingHeapTree<E, P> parentOfTree = tree.parent;
            
            // Go to leftmost child.
            final PairingHeapTree<E, P> leftmostChild = parentOfTree.child;
            
            if (leftmostChild == tree) {
                // Unlink the leftmost child.
                parentOfTree.child = leftmostChild.next;
                leftmostChild.parent = null;
                --parentOfTree.size;
                leftmostChild.priority = newPriority;
                root = merge(root, leftmostChild);
                return;
            }
            
            // Find the nodes u, v such that u.child = v and v = tree.
            PairingHeapTree<E, P> u = leftmostChild;
            PairingHeapTree<E, P> v = leftmostChild.next;
            
            while (v != tree) {
                u = v;
                v = v.next;
            }
            
            // Here v = tree and u is the immediate child to the right of v.
            u.next = v.next;
            --parentOfTree.size;
            
            // Set the new priority of v.
            v.priority = newPriority;
            
            // Update the root list.
            root = merge(root, v);
        } else {
            root.priority = newPriority;
        }
    }

    @Override
    public E extractMinimum() {
        if (root == null) {
            throw new NoSuchElementException(
                    "Reading from an empty pairing heap.");
        } else if (root.child == null) {
            final E ret = root.element;
            root = null;
            map.remove(ret);
            return ret;
        }
        
        final E ret = root.element;
        PairingHeapTree<E, P> tmp = root.child;
        PairingHeapTree<E, P> tmp2;
        
        while (tmp != null) {
            list.addLast(tmp);
            tmp.parent = null;
            tmp2 = tmp;
            tmp = tmp.next;
            tmp2.next = null;
        }
        
        root = mergePairs(list);
        map.remove(ret);
        return ret;
    }

    @Override
    public E min() {
        if (root == null) {
            throw new NoSuchElementException(
                    "Reading from an empty pairing heap.");
        }
        
        return root.element;
    }
    
    @Override
    public int size() {
        return root == null ? 0 : root.size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public void clear() {
        map.clear();
        root = null;
    }

    @Override
    public MinPriorityQueue<E, P> spawn() {
        return new PairingHeap<>();
    }
    
    /**
     * Merges two given trees into one.
     * 
     * @param tree1 the first tree.
     * @param tree2 the other tree.
     * 
     * @return a merged tree. 
     */
    private PairingHeapTree<E, P> merge(final PairingHeapTree<E, P> tree1,
                                        final PairingHeapTree<E, P> tree2) {
        if (tree1 == null) {
            return tree2;
        }
        
        if (tree2 == null) {
            return tree1;
        }
        
        if (tree1.priority.compareTo(tree2.priority) <= 0) {
            // tree1.priority <= tree2.priority; tree1 becomes the root.
            final PairingHeapTree<E, P> oldChild = tree1.child;
            tree1.child = tree2;
            tree2.next = oldChild;
            tree1.size += tree2.size;
            tree2.parent = tree1;
            return tree1;
        } else {
            // tree1.priority > tree2.priority, tree2 becomes the root.
            final PairingHeapTree<E, P> oldChild = tree2.child;
            tree2.child = tree1;
            tree1.next = oldChild;
            tree2.size += tree1.size;
            tree1.parent = tree2;
            return tree2;
        }
    }
    
    /**
     * Merges the root list of trees.
     * 
     * @param list the root list containing references to all the trees to 
     * merge.
     * 
     * @return the root node of all the merged trees.
     */
    private PairingHeapTree<E, P> 
        mergePairs(final Deque<PairingHeapTree<E, P>> list) {
        while (list.size() > 1) {
            final PairingHeapTree<E, P> left  = list.removeFirst();
            final PairingHeapTree<E, P> right = list.removeFirst();
            list.addLast(merge(left, right));
        }
        
        return list.removeFirst();
    }
}
