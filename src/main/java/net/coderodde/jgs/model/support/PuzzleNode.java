package net.coderodde.jgs.model.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import static net.coderodde.jgs.Utilities.checkNotNull;
import net.coderodde.jgs.model.AbstractNode;
import net.coderodde.jgs.model.Graph;

/**
 * This class models a node of a <tt>(n^2 - 1)</tt>-puzzle game. Basically,
 * <code>PuzzleNode</code> models an <b>implicit</b> graph.
 * 
 * @author Rodion Efremov
 * @version 1.6
 */
public class PuzzleNode extends AbstractNode<PuzzleNode> {
    
    /**
     * The minimum width/height of this puzzle node.
     */
    private static final int MINIMUM_DIMENSION = 2;
    
    /**
     * The maximum width/height of this puzzle node.
     */
    private static final int MAXIMUM_DIMENSION = 11;
   
    /**
     * Maps puzzle dimensions to their respective graphs.
     */
    private static Graph<PuzzleNode>[] graphMap = new Graph[MAXIMUM_DIMENSION];
    
    /**
     * The matrix of tiles in this node..
     */
    private final byte[][] m;
    
    /**
     * The x-coordinate of the zero tile.
     */
    private byte x;
    
    /**
     * The y-coordinate of the zero tile.
     */
    private byte y;
    
    /**
     * Constructs a puzzle from given matrix.
     * 
     * @param matrix the input matrix.
     */
    public PuzzleNode(final byte[][] matrix) {       
        if (graphMap[matrix.length] == null) {
            graphMap[matrix.length] = new Graph<>();
            this.ownerGraph = graphMap[matrix.length];
        }
        
        this.m = new byte[matrix.length][matrix.length];
        
        // Filter set.
        final Set<Byte> set = new HashSet<>();
        
        for (int y = 0; y < matrix.length; ++y) {
            if (matrix[y].length != matrix.length) {
                throw new IllegalArgumentException("Bad matrix.");
            }
            
            for (int x = 0; x < matrix.length; ++x) {
                this.m[y][x] = matrix[y][x];
                set.add(matrix[y][x]);
                
                if (matrix[y][x] == 0) {
                    this.x = (byte) x;
                    this.y = (byte) y;
                }
            }
        }
        
        if (set.size() != matrix.length * matrix.length) {
            throw new IllegalArgumentException("Duplicate tile detected.");
        }
        
        if (!set.contains((byte) 0)) {
            throw new IllegalArgumentException();
        }
        
        Byte[] array = new Byte[set.size()];
        Byte badOne = null;
        
        try {
            for (final Byte b : set) {
                badOne = b;
                array[b] = b;
            }
        } catch (final ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException(
                    "Bad entry (" + badOne + ") detected");
        }
    }
    
    /**
     * Creates an initial puzzle node.
     * 
     * @param n the dimension of this puzzle node.
     */
    public PuzzleNode(final int n) {
        if (graphMap[n] == null) {
            graphMap[n] = new Graph<>();
            this.ownerGraph = graphMap[n];
        }
        
        checkDimension(n);
        m = new byte[n][n];
        
        byte index = 1;
        for (int y = 0; y != n; ++y) {
            for (int x = 0; x != n; ++x, ++index) {
                if (index != n * n) {
                    m[y][x] = index;
                }
            }
        }
        
        x = (byte)(n - 1);
        y = (byte)(n - 1);
    }
    
    /**
     * Copy-constructs a new node from <code>node</code>.
     * 
     * @param node the node to copy.
     */
    public PuzzleNode(final PuzzleNode node) {
        this.ownerGraph = node.getOwnerGraph();
        final int n = node.getDimension();
        m = new byte[n][n];
        
        for (int y = 0; y != n; ++y) {
            for (int x = 0; x != n; ++x) {
                m[y][x] = node.m[y][x];
            }
        }
        
        x = node.x;
        y = node.y;
    }
    
    /**
     * Returns the iterator over this node's neighbor nodes.
     * 
     * @return the iterator over this node's neighbor nodes. 
     */
    @Override
    public Iterator<PuzzleNode> iterator() {
        return new PuzzleNodeIterator();
    }
    
    /**
     * Checks whether <code>o</code> represents the same node as this.
     * 
     * @param o the object to test.
     * 
     * @return <code>true</code> if <code>o</code> is a puzzle node and decodes
     * the same state as this.
     */
    @Override
    public boolean equals(Object o) {
        if ((o instanceof PuzzleNode) == false) {
            return false;
        }
        
        final PuzzleNode other = (PuzzleNode) o;
        
        if (other.getDimension() != this.getDimension()) {
            return false;
        }
        
        final int n = this.getDimension();
        
        for (int y = 0; y != n; ++y) {
            for (int x = 0; x != n; ++x) {
                if (this.m[y][x] != other.m[y][x]) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Computes the hash of this puzzle node.
     * 
     * @return the hash of this puzzle node.
     */
    @Override
    public int hashCode() {
        final int n = m.length;
        int i = 1;
        int hash = 0;
        
        for (int y = 0; y != n; ++y) {
            for (int x = 0; x != n; ++x, ++i) {
                hash += m[y][x] * i;
            }
        }
        
        return hash;
    }
    
    /**
     * Reads a particular tile.
     * 
     * @param x the x-coordinate of the tile.
     * @param y the y-coordinate of the tile.
     * 
     * @return the tile at position <tt>(x, y)</tt>. 
     */
    public byte get(final int x, final int y) {
        return m[y][x];
    }
    
    /**
     * Returns a puzzle node generated by sliding the empty tile upwards.
     * 
     * @return a puzzle node or <code>null</code> if the empty tile slides away
     * from the matrix.
     */
    public PuzzleNode moveUp() {
        if (y == 0) {
            return null;
        }
        
        final PuzzleNode node = new PuzzleNode(this);
        final byte tmp = node.m[y][x];
        node.m[y][x] = node.m[y - 1][x];
        node.m[y - 1][x] = tmp;
        node.y--;
        return node;
    }
    
    /**
     * Returns a puzzle node generated by sliding the empty tile downwards.
     * 
     * @return a puzzle node or <code>null</code> if the empty tile slides away
     * from the matrix.
     */
    public PuzzleNode moveDown() {
        if (y == m.length - 1) {
            return null;
        }
        
        final PuzzleNode node = new PuzzleNode(this);
        final byte tmp = node.m[y][x];
        node.m[y][x] = node.m[y + 1][x];
        node.m[y + 1][x] = tmp;
        node.y++;
        return node;
    }
    
    /**
     * Returns a puzzle node generated by sliding the empty tile to the left.
     * 
     * @return a puzzle node or <code>null</code> if the empty tile slides away
     * from the matrix.
     */
    public PuzzleNode moveLeft() {
        if (x == 0) {
            return null;
        }
        
        final PuzzleNode node = new PuzzleNode(this);
        final byte tmp = node.m[y][x];
        node.m[y][x] = node.m[y][x - 1];
        node.m[y][x - 1] = tmp;
        node.x--;
        return node;
    }
    
    /**
     * Returns a puzzle node generated by sliding the empty tile to the right.
     * 
     * @return a puzzle node or <code>null</code> if the empty tile slides away
     * from the matrix.
     */
    public PuzzleNode moveRight() {
        if (x == m.length - 1) {
            return null;
        }
        
        final PuzzleNode node = new PuzzleNode(this);
        final byte tmp = node.m[y][x];
        node.m[y][x] = node.m[y][x + 1];
        node.m[y][x + 1] = tmp;
        node.x++;
        return node;
    }
    
    /**
     * Returns the dimension of this puzzle node.
     * 
     * @return the dimension of this puzzle node.
     */
    public final int getDimension() {
        return m.length;
    }
    
    /**
     * Produces printable String representing this puzzle node.
     * 
     * @return a text-UI representation of this puzzle node.
     */
    @Override
    public String toString() {
        final int maxNumber = m.length * m.length - 1;
        final int fieldLength = 
                Math.max(5, (int)(Math.floor(Math.log10(maxNumber))) + 1);
        
        final StringBuilder sb = new StringBuilder();
        final StringBuilder all = new StringBuilder(8192);
        
        String smallBar = "+-";
        String filler = "| ";
        
        for (int i = 0; i != fieldLength + 1; ++i) {
            smallBar += '-';
            filler += ' ';
        }
        
        for (int i = 0; i != m.length; ++i) {
            sb.append(smallBar);
        }
        
        sb.append('+')
          .append('\n');
        
        final String horizontalBar = sb.toString();
        
        for (int yy = 0; yy != m.length; ++yy) {
            all.append(horizontalBar);;
            
            for (int xx = 0; xx != m.length; ++xx) {
                all.append(filler);
            }
            
            all.append("|\n");
            
            for (int xx = 0; xx != m.length; ++xx) {
                int fl;
                
                if (m[yy][xx] == 0) {
                    fl = 1;
                } else {
                    fl = (int)(Math.floor(Math.log10(m[yy][xx]))) + 1; 
                }
                
                int tmp = fieldLength - fl;
                int after = tmp / 2;
                int before = tmp - after;
                String skip = "";
                String skip2 = "";
                
                for (int i = 0; i != before; ++i) {
                    skip += ' ';
                }
                
                for (int i = 0; i != after; ++i) {
                    skip2 += ' ';
                }
                
                all.append("| ")
                   .append(String.format(skip + "%d" + skip2 + " ", m[yy][xx]));
            }
            
            all.append("|\n");
            
            for (int xx = 0; xx != m.length; ++xx) {
                all.append(filler);
            }
            
            all.append("|\n");
        }
        
        return all.append(horizontalBar).toString();
    }

    /**
     * Does nothing.
     */
    @Override
    public void clear() {}

    /**
     * Does nothing.
     * 
     * @param child ignored.
     */
    @Override
    public void connectTo(PuzzleNode child) {}

    /**
     * {@inheritDoc}
     * 
     * @param childCandidate the node to test against.
     * 
     * @return <code>true</code> if the distance between this node and
     * <code>childCandidate</code> is exactly 1; <code>false</code> otherwise.
     */
    @Override
    public boolean isConnectedTo(PuzzleNode childCandidate) {
        checkNotNull(childCandidate, "The child candidate is null.");
        
        if (this.getDimension() != childCandidate.getDimension()) {
            return false;
        }
        
        for (final PuzzleNode actualChild : this) {
            if (actualChild.equals(childCandidate)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Does nothing.
     * 
     * @param child ignored.
     */
    @Override
    public void disconnectFrom(PuzzleNode child) {}

    /**
     * Returns an <code>Iterable</code> over this node's parents.
     * 
     * @return an <code>Iterable</code> over this node's parents.
     */
    @Override
    public Iterable<PuzzleNode> parents() {
        return new Iterable<PuzzleNode>() {

            @Override
            public Iterator<PuzzleNode> iterator() {
                return new PuzzleNodeIterator();
            }
        };
    }

    /**
     * Returns the amount of neighbors of this node.
     * 
     * @return the amount of neighbors of this node.
     */
    @Override
    public int childrenListSize() {
        return new PuzzleNodeIterator().getSize();
    }

    /**
     * Returns the amount of neighbors of this node.
     * 
     * @return the amount of neighbors of this node.
     */
    @Override
    public int parentsListSize() {
        return new PuzzleNodeIterator().getSize();
    }
    
    /**
     * This inner class implements the puzzle node neighbor iterator.
     */
    private class PuzzleNodeIterator implements Iterator<PuzzleNode> {

        /**
         * The list of neighbor nodes.
         */
        private final List<PuzzleNode> neighbourList;
        
        /**
         * The iterator of the neighbor list.
         */
        private final Iterator<PuzzleNode> iterator;
        
        /**
         * Constructs an iterator over the enclosing nodes neighbors.
         */
        PuzzleNodeIterator() {
            final PuzzleNode node = PuzzleNode.this;
            
            neighbourList = new ArrayList<>(4);
            
            PuzzleNode tmp = node.moveUp();
            
            if (tmp != null) {
                neighbourList.add(tmp);
            }
            
            tmp = node.moveRight();
            
            if (tmp != null) {
                neighbourList.add(tmp);
            }
            
            tmp = node.moveDown();
            
            if (tmp != null) {
                neighbourList.add(tmp);
            }
            
            tmp = node.moveLeft();
            
            if (tmp != null) {
                neighbourList.add(tmp);
            }
            
            iterator = neighbourList.iterator();
        }
        
        /**
         * Returns <code>true</code> if this iterator has more nodes to iterate,
         * <code>false</code> otherwise.
         * 
         * @return <code>true</code> if this iterator has more nodes to iterate,
         * <code>false</code> otherwise.
         */
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
         * Returns the next neighbor puzzle node, or throws 
         * <code>NoSuchElementException</code> if there is no more left.
         * 
         * @return the next puzzle node.
         * 
         * @throws NoSuchElementException if there is no more nodes to iterate.
         */
        @Override
        public PuzzleNode next() {
            return iterator.next();
        }

        /**
         * Not implemented.
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                        "Cannot modify an implicit puzzle graph.");
        }
        
        /**
         * Returns the amount of nodes this iterator is iterating over.
         * 
         * @return the amount of nodes in this iterator.
         */
        int getSize() {
            return neighbourList.size();
        }
    }
    
    /**
     * Checks the dimension.
     * 
     * @param n the dimension to check.
     * 
     * @throws IllegalArgumentException if dimension is too small or too large.
     */
    private final void checkDimension(final int n) {
        if (n < MINIMUM_DIMENSION) {
            throw new IllegalArgumentException(
                    "Dimension is too small: " + n + ". Must be at least " +
                            MINIMUM_DIMENSION + ".");
        }
        
        if (n > MAXIMUM_DIMENSION) {
            throw new IllegalArgumentException(
                    "Dimension is too large: " + n + ". Must be at most " + 
                            MAXIMUM_DIMENSION + ".");
        }
    }
}
