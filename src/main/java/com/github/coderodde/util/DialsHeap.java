package com.github.coderodde.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This class implements a priority queue data structure called a Dial's heap.
 * 
 * @param <D> the type of the satellite data.
 * 
 * @version 1.0.0 (May 10, 2024)
 * @since 1.0.0
 */
public class DialsHeap<D> implements Iterable<D>, Cloneable {
    
    /**
     * This static inner class implements the node type for this heap.
     * 
     * @param <D> the satellite data type.
     */
    private static final class DialsHeapNode<D> {
        
        /**
         * The actual satellite datum.
         */
        final D datum;
        
        /**
         * The priority key of this node. Must be at least zero (0).
         */
        int priority;
        
        /**
         * The previous node in the collision chain or {@code null} if this node
         * is the head of the collision chain.
         */
        DialsHeapNode<D> prev;
        
        /**
         * The next node in the collision chain or {@code null} if this node is
         * the tail of the collision chain.
         */
        DialsHeapNode<D> next;
        
        /**
         * Constructs a new heap node.'
         * 
         * @param datum    the satellite datum.
         * @param priority the priority key.
         */
        DialsHeapNode(final D datum, final int priority) {
            this.datum = datum;
            this.priority = priority;
        }
    }
    
    /**
     * This inner class implements the iterator over all satellite data in this
     * heap in the ascending priority key order.
     */
    private final class DialsHeapIterator implements Iterator<D> {

        /**
         * Caches the number of nodes already iterated.
         */
        private int iterated = 0;
        
        /**
         * The current heap node.
         */
        private DialsHeapNode<D> currentDialsHeapNode;
        
        /**
         * Constructs a new iterator over the enclosing heap.
         */
        private DialsHeapIterator() {
            // Attempt to find the head node:
            for (final DialsHeapNode<D> headNode : table) {
                if (headNode != null) {
                    currentDialsHeapNode = headNode;
                    return;
                }
            }
            
            // Once here, the heap is empty, return null:
            currentDialsHeapNode = null;
        }
        
        /**
         * {@inheritDoc } 
         */
        @Override
        public boolean hasNext() {
            return iterated < size;
        }

        /**
         * {@inheritDoc} 
         */
        @Override
        public D next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Nothing to iterate left.");
            }
            
            final D returnElement = currentDialsHeapNode.datum;
            iterated++;
            currentDialsHeapNode = computeNextDialsHeapNode();
            return returnElement;
        }
        
        /**
         * Returns the next heap node.
         * 
         * @return the next heap node in the iteration order.
         */
        private DialsHeapNode<D> computeNextDialsHeapNode() {
            if (iterated == size) {
                // Once here, iteration is complete.
                return null;
            }
                
            if (currentDialsHeapNode.next != null) {
                // currentDialsHeapNode has minimum priority key, move to its 
                // right sibling/neighbor in the collision chain:
                return currentDialsHeapNode.next;
            }
            
            // Search the next smallest priority node:
            for (int p = currentDialsHeapNode.priority + 1;
                     p < table.length; 
                     p++) {
                
                if (table[p] != null) {
                    // Found!
                    return table[p];
                }
            }
            
            // We should never ever get here.
            throw new IllegalStateException("Should not get here.");
        }
    }
    
    /**
     * The default table capacity.
     */
    private static final int DEFAULT_TABLE_CAPACITY = 8;
    
    /**
     * The table mapping each slot to the head of a collision chain.
     */
    private DialsHeapNode<D>[] table;
    
    /**
     * The map mapping the satellite datums to their respective heap nodes.
     */
    private final Map<D, DialsHeapNode<D>> nodeMap = new HashMap<>();
    
    /**
     * Caches the number of satellite datums in this heap.
     */
    private int size = 0;
    
    /**
     * Constructs a heap with {@code tableCapacity} as the capacity of the 
     * internal collision chain table.
     * 
     * @param tableCapacity the requested collision chain capacity.
     */
    public DialsHeap(final int tableCapacity) {
        this.table = new DialsHeapNode[tableCapacity];
    }
    
    /**
     * Constructs a heap0 with default collision chain table capacity.
     */
    public DialsHeap() {
        this(DEFAULT_TABLE_CAPACITY);
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Iterator<D> iterator() {
        return new DialsHeapIterator();
    }
    
    /**
     * Inserts a new datum {@code datum} to this heap with the given priority 
     * {@code priority}.
     * 
     * @param datum    the datum to insert.
     * @param priority the priority key to set.
     */
    public void insert(final D datum, final int priority) {
        
        if (mustExpand(priority)) {
            expand(priority);
        }
        
        final DialsHeapNode<D> newTreeHeapNode =
                new DialsHeapNode<>(datum, priority);
        
        nodeMap.put(datum, newTreeHeapNode);
        linkImpl(newTreeHeapNode, priority);
        size++;
    }
    
    /**
     * Updates the priority of the satellite datum {@code datum} to 
     * {@code priority}. This method can handle both increasing and decreasing 
     * of the priority key.
     * 
     * @param datum    the datum of which priority to update.
     * @param priority the new priority of {@code datum}. 
     */
    public void updatePriority(final D datum, final int priority) {
        
        if (mustExpand(priority)) {
            expand(priority);
        }
        
        final DialsHeapNode<D> node = nodeMap.get(datum);
        
        unlinkImpl(node);
        linkImpl(node, priority);
        node.priority = priority;
    }
    
    /**
     * Returns the minimal priority throughout the contents of this heap.
     * 
     * @return the minimal priority.
     */
    public int minimumPriority() {
        return accessMinimumPriorityNode().priority;
    }
    
    /**
     * Returns the datum with the lowest priority key, or {@code null} if this
     * heap is empty.
     * 
     * @return the datum with the lowest priority key, or {@code null} if this
     *         heap is empty.
     */
    public D minimumNode() {
        if (size == 0) {
            return null;
        }
        
        return accessMinimumPriorityNode().datum;
    }
    
    /**
     * Returns {@code true} if the {@code datum} is stored in this heap.
     * 
     * @param datum the query datum.
     * 
     * @return {@code true} if the {@code datum} is stored in this heap.
     */
    public boolean contains(final D datum) {
        return nodeMap.containsKey(datum);
    }
    
    /**
     * Returns the current priority of the input datum.
     * 
     * @param datum the datum to query.
     * @return the current priority of {@code datum}.
     */
    public int getPriority(final D datum) {
        return nodeMap.get(datum).priority;
    }
    
    /**
     * Removes and returns the datum with the lowest priority key, or 
     * {@code null} if this heap is empty.
     * 
     * @return the datum with the lowest priority key, or {@code null} if this 
     *         heap is empty.
     */
    public D extractMinimum() {
        if (size == 0) {
            return null;
        }
        
        final DialsHeapNode<D> treeNode = accessMinimumPriorityNode();
        
        unlinkImpl(treeNode);
        size--;
        return treeNode.datum;
    }
    
    /**
     * Removes the datum {@code datum} from this heap.
     * @param datum 
     */
    public void remove(final D datum) {
        unlinkImpl(nodeMap.get(datum));
        size--;
    }
    
    /**
     * Clears all the data from this heap.
     */
    public void clear() {
        size = 0;
        nodeMap.clear();
        Arrays.fill(table, null);
    }
    
    /**
     * Since the heap cannot contract the collision chain table, the remedy to 
     * do that is to clone it which will return another heap with the same 
     * content, but with the table as small as is necessary to accommodate also
     * the maximum priority nodes.
     * 
     * @return the clone of this heap.
     */
    @Override
    public Object clone() {
        final int maximumPriorityKey = getMaximumPriority();
        final int cloneCapacity = getNextCapacity(maximumPriorityKey);
        final DialsHeap<D> copy = new DialsHeap<>(cloneCapacity);
        
        for (final Map.Entry<D, DialsHeapNode<D>> entry : nodeMap.entrySet()) {
            copy.insert(entry.getValue().datum, entry.getValue().priority);
        }
        
        return copy;
    }
    
    /**
     * Returns the number of datums stored in this heap.
     * 
     * @return the number of datums stored in this heap.
     */
    public int size() {
        return size;
    }
    
    /**
     * Returns the head of the collision chain with the lowest priority key.
     * 
     * @return the head of the collision chain with the lowest priority key.
     */
    private DialsHeapNode<D> accessMinimumPriorityNode() {
        for (int p = 0; p != table.length; p++) {
            if (table[p] != null) {
                return table[p];
            }
        }
        
        throw new IllegalStateException("Should not get here.");
    }
    
    /**
     * Links the node {@code node} to the head of the collision chain with 
     * priority key {@code priority}.
     * 
     * @param node     the node to link.
     * @param priority the priority key to link with.
     */
    private void linkImpl(final DialsHeapNode<D> node, final int priority) {
        final DialsHeapNode<D> currentBucketHead = table[priority];
        
        if (currentBucketHead != null) {
            node.next = currentBucketHead;
            currentBucketHead.prev = node;
        } 
        
        table[priority] = node;
    }
    
    /**
     * Unlinks the node {@code node} from this heap.
     * 
     * @param node the node to unlink.
     */
    private void unlinkImpl(final DialsHeapNode<D> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
            node.prev = null;
            
            if (node.next != null) {
                node.next.prev = node.prev;
                node.next = null;
            }
        } else {
            // Once here, node.prev == null!
            if (node.next != null) {
                node.next.prev = null;
                node.next = null;
                table[node.priority] = node.next;
            } else {
                // Remove the last node in the collision chain:
                table[node.priority] = null;
            }
        }
    }
    
    /**
     * Returns {@code true} if this heap's table cannot accommodate a node with
     * priority {@code priority}.
     * 
     * @param priority the priority to query.
     * 
     * @return {@code true} if the table must be expanded.
     */
    private boolean mustExpand(final int priority) {
        return priority >= table.length;
    }
    
    /**
     * Expands the internal table {@code table} such that it can accommodate the
     * priority key {@code priority}, while being smallest such table.
     * 
     * @param priority the requested priority key.
     */
    private void expand(final int priority) {
        final int nextCapacity = getNextCapacity(priority);
        this.table = Arrays.copyOf(table, nextCapacity);
    }
    
    /**
     * Returns the capacity that is sufficiently large in order to accommodate
     * the heap nodes with priority {@code priority}.
     * 
     * @param priority the requested priority.
     * 
     * @return the next capacity to expand with. 
     */
    private int getNextCapacity(final int priority) {
        int nextCapacity = table.length;
        
        while (nextCapacity <= priority) {
            nextCapacity *= 2;
        }
        
        return nextCapacity;
    }
    
    /**
     * Returns the maximum priority key in this heap.
     * 
     * @return the maximum priority key in this heap.
     */
    private int getMaximumPriority() {
        for (int priority = table.length - 1; priority >= 0; priority--) {
            if (table[priority] != null) {
                return priority;
            }
        }
        
        return -1;
    }
}
 