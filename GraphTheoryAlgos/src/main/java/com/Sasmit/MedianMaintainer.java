package com.Sasmit;

import java.io.*;
import java.util.*;

class MedianMaintainer {
    private PriorityQueue<Integer> heapLow;  // max-heap
    private PriorityQueue<Integer> heapHigh; // min-heap
    private long medianSum;

    public MedianMaintainer() {
        // Max-heap for lower half
        heapLow = new PriorityQueue<>(Collections.reverseOrder());
        // Min-heap for upper half
        heapHigh = new PriorityQueue<>();
        medianSum = 0;
    }

    // Add number to the data structure
    private void addNumber(int num) {
        if (heapLow.isEmpty() || num <= heapLow.peek()) {
            heapLow.add(num);
        } else {
            heapHigh.add(num);
        }

        // Rebalance heaps if necessary
        if (heapLow.size() - heapHigh.size() > 1) {
            heapHigh.add(heapLow.poll());
        } else if (heapHigh.size() - heapLow.size() > 1) {
            heapLow.add(heapHigh.poll());
        }

        // Update median sum
        if (heapLow.size() >= heapHigh.size()) {
            medianSum += heapLow.peek();
        } else {
            medianSum += heapHigh.peek();
        }
    }

    // Compute sum of medians from a file
    public long sumMediansFromFile(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                int number = Integer.parseInt(line.trim());
                addNumber(number);
            }
        }
        // Return modulo the total number of elements
        int totalElements = heapLow.size() + heapHigh.size();
        return medianSum % totalElements;
    }

    // Optional: compute from array
    public long sumMediansFromArray(int[] numbers) {
        for (int num : numbers) {
            addNumber(num);
        }
        int totalElements = heapLow.size() + heapHigh.size();
        return medianSum % totalElements;
    }

    public static void main(String[] args) throws IOException {
        MedianMaintainer mm = new MedianMaintainer();
        long medianSum = mm.sumMediansFromFile("/Users/sasmitmunagala/Desktop/Sasmit_Algos/GraphTheoryAlgos/src/main/java/com/Sasmit/listOfNumbers");
        System.out.println(medianSum);
    }
}
