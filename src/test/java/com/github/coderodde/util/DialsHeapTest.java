package com.github.coderodde.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public final class DialsHeapTest {
    
    private DialsHeap<String> heap; 
    private Iterator<String> iterator;
    
    @Before 
    public void before() {
        heap = new DialsHeap<String>();
        iterator = null;
    }
    
    @Test
    public void testIterator() {
        iterator = heap.iterator();
        
        assertFalse(iterator.hasNext());
        
        try {
            iterator.next();
            fail("Should throw on empty iterator.");
        } catch (final NoSuchElementException ex) {
            
        }
        
        heap.insert("A1", 1);
        heap.insert("A2", 1);
        heap.insert("A3", 1);
        
        heap.insert("C1", 3);
        
        heap.insert("B1", 2);
        heap.insert("B2", 2);
        
        iterator = heap.iterator();
        
        assertEquals("A3", iterator.next());
        assertEquals("A2", iterator.next());
        assertEquals("A1", iterator.next());
        assertEquals("B2", iterator.next());
        assertEquals("B1", iterator.next());
        assertEquals("C1", iterator.next());
        
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testInsert() {
        heap.insert("A", 1);
        heap.insert("C", 3);
        heap.insert("B", 2);
        
        assertEquals("A", heap.minimumNode());
        assertEquals(1, heap.minimumPriority());
        assertEquals("A", heap.extractMinimum());
        
        assertEquals("B", heap.minimumNode());
        assertEquals(2, heap.minimumPriority());
        assertEquals("B", heap.extractMinimum());
        
        assertEquals("C", heap.minimumNode());
        assertEquals(3, heap.minimumPriority());
        assertEquals("C", heap.extractMinimum());
        
        assertEquals(0, heap.size());
    }

    @Test
    public void testUpdatePriority() {
        heap.insert("B", 2);
        heap.insert("A", 3);
        heap.insert("C", 1);
        
        heap.updatePriority("A", 1);
        heap.updatePriority("B", 2);
        heap.updatePriority("C", 3);
        
        assertEquals("A", heap.extractMinimum());
        assertEquals("B", heap.extractMinimum());
        assertEquals("C", heap.extractMinimum());
    }

    @Test
    public void testMinimumPriority() {
        heap.insert("A", 1);
        heap.insert("B", 2);
        heap.insert("C", 3);
        
        assertEquals(1, heap.minimumPriority());
        assertEquals("A", heap.extractMinimum());
        
        assertEquals(2, heap.minimumPriority());
        assertEquals("B", heap.extractMinimum());
        
        assertEquals(3, heap.minimumPriority());
        assertEquals("C", heap.extractMinimum());
    }

    @Test
    public void testMinimumNode() {
        heap.insert("A", 1);
        heap.insert("B", 2);
        heap.insert("C", 3);
        
        assertEquals("A", heap.minimumNode());
        assertEquals("A", heap.extractMinimum());
        
        assertEquals("B", heap.minimumNode());
        assertEquals("B", heap.extractMinimum());
        
        assertEquals("C", heap.minimumNode());
        assertEquals("C", heap.extractMinimum());
    }

    @Test
    public void testGetPriority() {
        heap.insert("X", 10);
        
        assertEquals(10, heap.getPriority("X"));
        
        heap.updatePriority("X", 9);
        
        assertEquals(9, heap.getPriority("X"));
        
        heap.updatePriority("X", 26);
        
        assertEquals(26, heap.getPriority("X"));
    }

    @Test
    public void testExtractMinimum() {
        for (int i = 10; i >= 0; i--) {
            heap.insert(Integer.toString(i), i);
        }
        
        for (int i = 0; i <= 10; i++) {
            assertEquals(Integer.toString(i), heap.extractMinimum());
        }
    }
    
    @Test
    public void testRemove() {
        for (int i = 10; i >= 0; i--) {
            heap.insert(Integer.toString(i), i);
        }
        
        // Remove odd numbers:
        for (int i = 1; i <= 10; i += 2) {
            heap.remove(Integer.toString(i));
        }
        
        // Make sure all the even numbers are still in the heap:
        for (int i = 0; i <= 10; i += 2) {
            heap.containsDatum(Integer.toString(i));
        }
    }

    @Test
    public void testClone() {
        for (int i = 2; i < 10; i++) {
            heap.insert(Integer.toString(i), i);
        }
        
        final DialsHeap<String> copy = (DialsHeap<String>) heap.clone();
        
        assertEquals(heap.size(), copy.size());
        
        for (int i = 2; i < 10; i++) {
            assertTrue(copy.containsDatum(Integer.toString(i)));
        }
    }

    @Test
    public void testSize() {
        for (int i = 0; i < 10; i++) {
            assertEquals(i, heap.size());
            heap.insert(Integer.toString(i), i);
            assertEquals(i + 1, heap.size());
        }
    }
}
