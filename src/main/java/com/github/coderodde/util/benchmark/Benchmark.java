package com.github.coderodde.util.benchmark;

import com.github.coderodde.util.CachedDialsHeap;
import com.github.coderodde.util.DialsHeap;
import com.github.coderodde.util.IntegerMinimumPriorityQueue;
import java.util.Arrays;
import java.util.Random;

/**
 * This class implements the benchmark program for the priority queues.
 * 
 * @version 1.0.1 (May 12, 2024)
 * @since 1.0.0 (May 11, 2024)
 */
public final class Benchmark {
    
    /**
     * The upper bound value for the randomly generated integers.
     */
    private static final int UPPER_BOUND = 100_000;
    
    /**
     * The benchmarking array lengths.
     */
    private static final int ARRAY_LENGTH = 1_000_000;
    
    /**
     * The entry point of the benchmark.
     * 
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        final long seed = System.currentTimeMillis();
        System.out.printf("seed = %d.\n", seed);
        
        warmup(seed);
        benchmark(seed);
    }
    
    /**
     * Warms up the benchmark.
     * 
     * @param seed the random number generator seed. 
     */
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
    
    /**
     * Benchmarks the heaps.
     * 
     * @param seed the random number generator seed.
     */
    private static void benchmark(final long seed) {
        final Integer[] output1 = 
                runBenchmark(
                        new DialsHeap<>(), 
                        seed, 
                        true);
        
        System.out.println();
        
        final Integer[] output2 = 
                runBenchmark(
                        new CachedDialsHeap<>(), 
                        seed, 
                        true);
        
        System.out.println();
        System.out.printf(
                "Heaps agree: %b.\n", 
                Arrays.equals(output1, 
                              output2));
    }
    
    /**
     * Implements the benchmark/warmup runner.
     * 
     * @param heap  the heap to benchmark.
     * @param seed  the random number generator seed.
     * @param print the flag specifying whether to print the intermediate 
     *              results.
     * 
     * @return the processed data.
     */
    private static Integer[] runBenchmark(
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
        
        final Integer[] output = new Integer[heap.size()];
        int index = 0;
        
        start = System.currentTimeMillis();
        
        while (heap.size() != 0) {
            output[index++] = heap.extractMinimum();
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
        
        return output;
    }
    
    /**
     * Returns the random array of {@code Integer}s. Each integer is drawn from 
     * the range {@code [0, upperBound - 1]}.
     * 
     * @param random     the random number generator.
     * @param length     the length of the generated array.
     * @param upperBound the upper bound of the integer values.
     * 
     * @return the random integer array.
     */
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
    
    /**
     * Returns the random array of {@code int}s. Each integer is drawn from 
     * the range {@code [0, upperBound - 1]}.
     * 
     * @param random     the random number generator.
     * @param length     the length of the generated array.
     * @param upperBound the upper bound of the integer values.
     * 
     * @return the random integer array.
     */
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
