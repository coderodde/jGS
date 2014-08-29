package net.coderodde.jgs.model.ds.support;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import net.coderodde.jgs.model.ds.MinPriorityQueue;


/**
 * This class implements Fibonacci heap.
 *
 * @author Rodion Efremov
 * @version 1.6
 * 
 * @param <E> the element type.
 * @param <P> the priority key type.
 */
public class FibonacciHeap<E, P extends Comparable<? super P>>
extends MinPriorityQueue<E, P> {

    /**
     * The default map capacity.
     */
    private static final int DEFAULT_MAP_CAPACITY = 1 << 10;

    /**
     * This class implements nodes of {@link FibonacciHeap}.
     * 
     * @param <E> the element type.
     * @param <P> the priority key type.
     */
    private static class Node<E, P extends Comparable<? super P>> {
        
        /**
         * The actual element.
         */
        private final E element;
        
        /**
         * The priority key of this node.
         */
        private P priority;
        
        /**
         * The reference to parent.
         */
        private Node<E, P> parent;
        
        /**
         * The left sibling.
         */
        private Node<E, P> left = this;
        
        /**
         * The right sibling.
         */
        private Node<E, P> right = this;

        /**
         * Reference to a leftmost child.
         */
        private Node<E, P> child;
        
        /**
         * The amount of children of this node.
         */
        private int degree;
        
        /**
         * Indicates whether this node has lost a child since the last time this
         * node was made the child of another node.
         */
        private boolean marked;

        Node(final E element, final P priority) {
            this.element = element;
            this.priority = priority;
        }
    }

    private static final double LOG_PHI = Math.log((1 + Math.sqrt(5)) / 2);

    /**
     * Maps every element in this heap to the node it belongs to.
     */
    private Map<E, Node<E, P>> map;
    
    /**
     * References the node with minimum priority.
     */
    private Node<E, P> minimumNode;
    
    /**
     * The amount of elements in this heap.
     */
    private int size;

    private Node<E, P>[] array;

    /**
     * Construct a new {@code FibonacciHeap} with default settings.
     */
    public FibonacciHeap() {
        this(DEFAULT_MAP_CAPACITY);
    }
    
    /**
     * Constructs a new {@code FibonacciHeap} with the given capacity for the 
     * node map.
     * 
     * @param mapCapacity the initial capacity of the underlying map.
     */
    public FibonacciHeap(final int mapCapacity) {
        map = new HashMap<>(mapCapacity);
        array = new Node[10];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final E element, final P priority) {
        if (map.containsKey(element)) {
            // element already in this heap, use decreaseKey instead.
            return;
        }
        
        Node<E, P> node = new Node<>(element, priority);

        if (minimumNode != null) {
            node.left = minimumNode;
            node.right = minimumNode.right;
            minimumNode.right = node;
            node.right.left = node;

            if (priority.compareTo(minimumNode.priority) < 0) {
                minimumNode = node;
            }
        } else {
            minimumNode = node;
        }

        map.put(element, node);
        ++size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreasePriority(final E element, final P newPriority) {
        if (!map.containsKey(element)) {
            // No element here.
            return;
        }
        
        Node<E, P> x = map.get(element);

        if (x.priority.compareTo(newPriority) <= 0) {
            // newPriority does not improve the current priority; do no more.
            return;
        }

        x.priority = newPriority;
        Node<E, P> y = x.parent;

        if (y != null && x.priority.compareTo(y.priority) < 0) {
            cut(x, y);
            cascadingCut(y);
        }

        if (x.priority.compareTo(minimumNode.priority) < 0) {
            minimumNode = x;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E extractMinimum() {
        if (size == 0) {
            throw new NoSuchElementException(
                    "Reading from an empty Fibonacci heap.");
        }

        Node<E, P> z = minimumNode;

        if (z != null) {
            int numKids = z.degree;
            Node<E, P> x = z.child;
            Node<E, P> tmpRight;

            while (numKids > 0) {
                tmpRight = x.right;

                x.left.right = x.right;
                x.right.left = x.left;

                x.left = minimumNode;
                x.right = minimumNode.right;
                minimumNode.right = x;
                x.right.left = x;

                x.parent = null;
                x = tmpRight;
                numKids--;
            }

            z.left.right = z.right;
            z.right.left = z.left;

            if (z == z.right) {
                minimumNode = null;
            } else {
                minimumNode = z.right;
                consolidate();
            }

            size--;
        }

        map.remove(z.element);
        return z.element;
    }
    
    @Override
    public E min() {
        if (size == 0) {
            throw new NoSuchElementException(
                    "Reading from an empty Fibonacci heap.");
        }
        
        return minimumNode.element;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
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
        minimumNode = null;
        map.clear();
        size = 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @return an empty Fibonacci heap. 
     */
    @Override
    public MinPriorityQueue<E, P> spawn() {
        return new FibonacciHeap<>();
    }
    
    private void consolidate() {
        final int arraySize = ((int) Math.floor(Math.log(size) / LOG_PHI)) + 1;
        checkArray(arraySize);
        
        for (int i = 0; i < arraySize; ++i) {
            array[i] = null;
        }

        int numberOfRoots = 0;
        Node<E, P> x = minimumNode;

        if (x != null) {
            ++numberOfRoots;
            x = x.right;

            while (x != minimumNode) {
                ++numberOfRoots;
                x = x.right;
            }
        }

        while (numberOfRoots > 0) {
            int degree = x.degree;
            Node<E, P> next = x.right;

            for (;;) {
                Node<E, P> y = array[degree];

                if (y == null) {
                    break;
                }

                if (x.priority.compareTo(y.priority) > 0) {
                    Node<E, P> tmp = y;
                    y = x;
                    x = tmp;
                }

                link(y, x);
                array[degree] = null;
                degree++;
            }

            array[degree] = x;
            x = next;
            numberOfRoots--;
        }

        minimumNode = null;

        for (Node<E, P> y : array) {
            if (y == null) {
                continue;
            }

            if (minimumNode != null) {
                y.left.right = y.right;
                y.right.left = y.left;

                y.left = minimumNode;
                y.right = minimumNode.right;
                minimumNode.right = y;
                y.right.left = y;

                if (y.priority.compareTo(minimumNode.priority) < 0) {
                    minimumNode = y;
                }
            } else {
                minimumNode = y;
            }
        }
    }

    private void link(Node<E, P> y, Node<E, P> x) {
        y.left.right = y.right;
        y.right.left = y.left;

        y.parent = x;

        if (x.child == null) {
            x.child = y;
            y.right = y;
            y.left = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right = y;
            y.right.left = y;
        }

        ++x.degree;

        y.marked = false;
    }

    private void cut(Node<E, P> x, Node<E, P> y) {
        x.left.right = x.right;
        x.right.left = x.left;
        y.degree--;

        if (y.child == x) {
            y.child = x.right;
        }

        if (y.degree == 0) {
            y.child = null;
        }

        x.left = minimumNode;
        x.right = minimumNode.right;
        minimumNode.right = x;
        x.right.left = x;

        x.parent = null;
        x.marked = false;
    }

    private void cascadingCut(Node<E, P> y) {
        Node<E, P> z = y.parent;

        if (z != null) {
            if (y.marked == false) {
                y.marked = true;
            } else {
                cut(y, z);
                cascadingCut(z);
            }
        }
    }
    
    private void checkArray(final int size) {
        if (array.length < size) {
            final Node[] newArray = new Node[size];
            array = newArray;
        }
    }
}