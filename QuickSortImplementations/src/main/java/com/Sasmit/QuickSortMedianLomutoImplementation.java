package com.Sasmit;

public class QuickSortMedianLomutoImplementation {

    // Find median of three values
    static int medianOfThree(int a, int b, int c) {
        if ((a > b) != (a > c))
            return a;
        else if ((b > a) != (b > c))
            return b;
        else
            return c;
    }

    // Find index of a value in array (for median pivot)
    static int findIndex(int[] arr, int low, int high, int value) {
        for (int i = low; i <= high; i++) {
            if (arr[i] == value) {
                return i;
            }
        }
        return low; // Fallback
    }

    // Lomuto partition with median-of-three
    static int partition(int[] arr, int low, int high) {
        // Step 1: Choose pivot using median-of-three
        int pivotIndex;

        if (high - low < 2) {
            // Too small for median-of-three
            pivotIndex = low;
        } else {
            // Get three values
            int first = arr[low];
            int middle = arr[(low + high) / 2];
            int last = arr[high];

            // Find median value
            int medianValue = medianOfThree(first, middle, last);

            // Find index of median value
            if (medianValue == first) {
                pivotIndex = low;
            } else if (medianValue == last) {
                pivotIndex = high;
            } else {
                pivotIndex = (low + high) / 2;
            }
        }

        // Step 2: Move pivot to front
        int temp = arr[low];
        arr[low] = arr[pivotIndex];
        arr[pivotIndex] = temp;

        // Step 3: Lomuto partition
        int pivot = arr[low];
        int i = low + 1;  // Boundary of <pivot region

        for (int j = low + 1; j <= high; j++) {
            if (arr[j] < pivot) {
                // Swap current element into <pivot region
                temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
            }
        }

        // Step 4: Place pivot in final position
        temp = arr[low];
        arr[low] = arr[i - 1];
        arr[i - 1] = temp;

        // Step 5: Return pivot's final position
        return i - 1;
    }

    // QuickSort recursive function
    static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // Partition and get pivot's final position
            int pivotPos = partition(arr, low, high);

            // Recursively sort elements before and after pivot
            quickSort(arr, low, pivotPos - 1);
            quickSort(arr, pivotPos + 1, high);
        }
    }

    // Helper function to print array
    static void printArray(int[] arr) {
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    // Main method to test
    public static void main(String[] args) {
        int[] arr = {8, 3, 1, 7, 0, 10, 2};

        System.out.println("Original array:");
        printArray(arr);

        quickSort(arr, 0, arr.length - 1);

        System.out.println("Sorted array:");
        printArray(arr);

        // Test edge cases
        int[][] testCases = {
                {1},                    // Single element
                {2, 1},                 // Two elements
                {3, 3, 3},              // All same
                {5, 4, 3, 2, 1},        // Reverse sorted
                {1, 2, 3, 4, 5},        // Already sorted
                {}                      // Empty
        };

        System.out.println("\nTesting edge cases:");
        for (int[] test : testCases) {
            if (test.length > 0) {
                int[] copy = test.clone();
                quickSort(copy, 0, copy.length - 1);
                System.out.print("Input: ");
                printArray(test);
                System.out.print("Output: ");
                printArray(copy);
            }
        }
    }
}