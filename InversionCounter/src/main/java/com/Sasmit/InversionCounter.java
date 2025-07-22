package com.Sasmit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides a method to count inversions in an array
 * using a modified merge sort algorithm, which is an efficient
 * divide-and-conquer approach. It also includes functionality
 * to read integers from a text file.
 */
public class InversionCounter {

    /**
     * The main method to demonstrate the inversion counting,
     * including reading data from a file.
     */
    public static void main(String[] args) {
        System.out.println("\n--- Reading from file and counting inversions ---");
        String filePath = "/Users/sasmitmunagala/Desktop/Sasmit_Algos/InversionCounter/src/main/java/com/Sasmit/inversions.txt"; // <--- IMPORTANT: Change this to your file path!

        try {
            int[] largeArray = readIntegersFromFile(filePath);
            System.out.println("Successfully read " + largeArray.length + " integers from " + filePath);
            long inversions = countInversions(largeArray);
            System.out.println("Total inversions for the file: " + inversions);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.err.println("Please ensure the file path is correct and the file exists.");
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number from file: " + e.getMessage());
            System.err.println("Please ensure all lines in the file contain valid integers.");
        }
    }

    /**
     * Reads integers from a specified text file, assuming one integer per line.
     *
     * @param filePath The path to the input text file.
     * @return An array of integers read from the file.
     * @throws IOException           If an I/O error occurs while reading the file.
     * @throws NumberFormatException If a line in the file cannot be parsed as an integer.
     */
    public static int[] readIntegersFromFile(String filePath) throws IOException, NumberFormatException {
        List<Integer> integers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Trim whitespace and check if the line is not empty before parsing
                if (!line.trim().isEmpty()) {
                    integers.add(Integer.parseInt(line.trim()));
                }
            }
        }
        // Convert ArrayList to int[]
        return integers.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Public entry point to count inversions in the given array.
     * It initializes a temporary array and calls the recursive merge sort function.
     *
     * @param arr The input array of integers.
     * @return The total number of inversions.
     */
    public static long countInversions(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0; // No inversions for null or single-element arrays
        }
        // Create a temporary array for merging, to avoid reallocating in recursive calls
        int[] temp = new int[arr.length];
        return mergeSortAndCount(arr, temp, 0, arr.length - 1);
    }

    /**
     * Recursive function that performs merge sort and counts inversions.
     * It divides the array into two halves, recursively counts inversions in each half,
     * and then counts inversions during the merging of the two sorted halves.
     *
     * @param arr   The array segment to sort and count inversions in.
     * @param temp  A temporary array used for merging.
     * @param left  The starting index of the current segment.
     * @param right The ending index of the current segment.
     * @return The number of inversions in the current segment.
     */
    private static long mergeSortAndCount(int[] arr, int[] temp, int left, int right) {
        long inversions = 0;
        if (left < right) {
            int mid = left + (right - left) / 2; // Calculate the middle index

            // Recursively count inversions in the left half
            inversions += mergeSortAndCount(arr, temp, left, mid);
            // Recursively count inversions in the right half
            inversions += mergeSortAndCount(arr, temp, mid + 1, right);

            // Count inversions during the merge step
            inversions += mergeAndCountSplitInversions(arr, temp, left, mid, right);
        }
        return inversions;
    }

    /**
     * Merges two sorted sub-arrays (arr[left...mid] and arr[mid+1...right])
     * and counts the inversions that occur between elements of these two sub-arrays.
     * This is the crucial part where inversions are counted.
     *
     * @param arr   The original array containing the two sub-arrays.
     * @param temp  The temporary array to store the merged sorted result.
     * @param left  The starting index of the left sub-array.
     * @param mid   The ending index of the left sub-array (and mid+1 is start of right).
     * @param right The ending index of the right sub-array.
     * @return The number of inversions found during this merge step.
     */
    private static long mergeAndCountSplitInversions(int[] arr, int[] temp, int left, int mid, int right) {
        int i = left;      // Pointer for the left sub-array
        int j = mid + 1;   // Pointer for the right sub-array
        int k = left;      // Pointer for the temporary array
        long inversions = 0;

        // Merge the two sub-arrays into the temporary array
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                // If element from left half is smaller or equal, no inversion with arr[j]
                temp[k++] = arr[i++];
            } else {
                // If element from right half (arr[j]) is smaller, it means arr[j]
                // forms an inversion with all remaining elements in the left half (arr[i] to arr[mid]).
                temp[k++] = arr[j++];
                inversions += (mid - i + 1); // Count inversions
            }
        }

        // Copy remaining elements of the left sub-array, if any
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        // Copy remaining elements of the right sub-array, if any
        while (j <= right) {
            temp[k++] = arr[j++];
        }

        // Copy the sorted elements from the temporary array back to the original array
        for (i = left; i <= right; i++) {
            arr[i] = temp[i];
        }

        return inversions;
    }
}
