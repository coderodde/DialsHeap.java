package com.github.coderodde.util.benchmark;

import com.github.coderodde.util.CachedDialsHeap;
import com.github.coderodde.util.DialsHeap;
import com.github.coderodde.util.IntegerMinimumPriorityQueue;
import java.util.Random;

public final class DialsHeapBenchmark {
    
    private static final int UPPER_BOUND = 10;
    private static final int ARRAY_LENGTH = 4;
    
    public static void main(String[] args) {
        final long seed = 1715347040913L; //System.currentTimeMillis();
        System.out.printf("seed = %d.\n", seed);
        
        warmup(seed);
        benchmark(seed);
    }
    
    private static void warmup(final long seed) {
        runBenchmark(
                new DialsHeap<>(),
                seed, 
                false);
        
        runBenchmark(
                new CachedDialsHeap<>(), 
                seed, 
                false);
    }
    
    private static void benchmark(final long seed) {
        runBenchmark(
                new DialsHeap<>(), 
                seed, 
                true);
        
        System.out.println();
        
        runBenchmark(
                new CachedDialsHeap<>(), 
                seed, 
                true);
    }
    
    private static void runBenchmark(
            final IntegerMinimumPriorityQueue<Integer> heap,
            final long seed,
            final boolean print) {
        
        final Random random = new Random(seed);
        
        final Integer[] array =
                getRandomIntegerArray(
                        random, 
                        ARRAY_LENGTH, 
                        UPPER_BOUND);
        
        long totalDuration = 0L;
        
        long start = System.currentTimeMillis();
        
        for (final Integer i : array) {
            heap.insert(i, i);
        }
        
        long end = System.currentTimeMillis();
        
        totalDuration += end - start;
        
        if (print) {
            System.out.printf("insert() in %d milliseconds.\n", end - start);
        }
        
        final int[] priorities = 
                getRandomIntArray(
                        random, 
                        ARRAY_LENGTH, 
                        UPPER_BOUND);
        
        start = System.currentTimeMillis();
        
        for (int i = 0; i < heap.size(); i++) {
            heap.updatePriority(array[i], priorities[i]);
        }
        
        end = System.currentTimeMillis();
        
        totalDuration += end - start;
        
        if (print) {
            System.out.printf(
                    "updatePriority() in %d milliseconds.\n", 
                    end - start);
        }
        
        start = System.currentTimeMillis();
        
        while (heap.size() != 0) {
            System.out.println(heap.size());
            heap.extractMinimum();
        }
        
        end = System.currentTimeMillis();
        
        totalDuration += end - start;
        
        if (print) {
            System.out.printf(
                    "extractMinimum() in %d milliseconds.\n", 
                    end - start);
            
            System.out.printf(
                    "Total duration of %s: %d milliseconds.\n", 
                    heap.getClass().getSimpleName(), 
                    totalDuration);
        }
    }
    
    private static Integer[] 
        getRandomIntegerArray(
                final Random random,
                final int length, 
                final int upperBound) {
            
        final Integer[] array = new Integer[length];
        
        for (int i = 0; i < length; i++) {
            array[i] = random.nextInt(upperBound);
        }
        
        return array;
    }
    
    private static int[] 
        getRandomIntArray(
                final Random random,
                final int length, 
                final int upperBound) {
            
        final int[] array = new int[length];
        
        for (int i = 0; i < length; i++) {
            array[i] = random.nextInt(upperBound);
        }
        
        return array;
    }
}
