package com.github.coderodde.util.benchmark;

import com.github.coderodde.util.CachedDialsHeap;
import com.github.coderodde.util.DialsHeap;
import com.github.coderodde.util.IntegerMinimumPriorityQueue;

public final class DialsHeapBenchmark {
    
    public static void main(String[] args) {
        final long seed = System.currentTimeMillis();
        warmup();
        benchmark();
    }
    
    private static void warmup() {
        runBenchmark(new DialsHeap<>(), false);
        runBenchmark(new CachedDialsHeap<>(), false);
    }
    
    private static void benchmark() {
        runBenchmark(new DialsHeap<>(), true);
        runBenchmark(new CachedDialsHeap<>(), true);
    }
    
    private static void runBenchmark(
            final IntegerMinimumPriorityQueue<String> heap,
            final boolean print) {
        long start = System.currentTimeMillis();
        
        long end = System.currentTimeMillis();
        
    }
}
