package com.Sasmit;

public class QuickSortMedianHoareImplementation {

    // Find median of three values
    static int medianOfThree(int a, int b, int c) {
        if ((a > b) != (a > c))
            return a;
        else if ((b > a) != (b > c))
            return b;
        else
            return c;
    }

    // Hoare partition with median-of-three pivot selection
    static int partition(int[] arr, int low, int high) {
        // Handle small arrays
        int pivot;
        if (high - low < 2) {
            // Too small for median-of-three
            pivot = arr[low];
        } else {
            // Get three values
            int first = arr[low];
            int middle = arr[(low + high) / 2];
            int last = arr[high];

            // Find median value
            pivot = medianOfThree(first, middle, last);
        }

        // Hoare partition
        int i = low - 1;
        int j = high + 1;

        while (true) {
            // Find element >= pivot from left
            do {
                i++;
            } while (arr[i] < pivot);

            // Find element < pivot from right
            do {
                j--;
            } while (arr[j] > pivot);

            // Check if pointers crossed
            if (i >= j) {
                return j;
            }

            // Swap elements
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    // QuickSort recursive function
    static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // Partition array and get split position
            int pi = partition(arr, low, high);

            // Recursively sort left and right parts
            quickSort(arr, low, pi);
            quickSort(arr, pi + 1, high);
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
        int[] arr = {15, 3, 9, 8, 12, 2, 18, 6};

        System.out.println("Original array:");
        printArray(arr);

        quickSort(arr, 0, arr.length - 1);

        System.out.println("Sorted array:");
        printArray(arr);
    }
}