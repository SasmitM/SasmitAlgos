package com.Sasmit;

public class Main {

    /**
     * The public entry point for Merge Sort.
     * Sorts an array using the merge sort algorithm.
     *
     * @param arr The array to be sorted.
     */
    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return; // An empty or single-element array is already sorted
        }
        int[] temp = new int[arr.length]; // Temporary array for merging
        mergeSort(arr, temp, 0, arr.length - 1);
    }

    /**
     * Recursive helper method for merge sort.
     * Divides the array into two halves, sorts them, and then merges them.
     *
     * @param arr   The array to be sorted.
     * @param temp  Temporary array used for merging.
     * @param left  The starting index of the subarray to be sorted.
     * @param right The ending index of the subarray to be sorted.
     */
    private static void mergeSort(int[] arr, int[] temp, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2; // Calculate the middle index
            mergeSort(arr, temp, left, mid);      // Sort the left half
            mergeSort(arr, temp, mid + 1, right); // Sort the right half
            merge(arr, temp, left, mid, right);   // Merge the two sorted halves
        }
    }

    /**
     * Merges two sorted subarrays into a single sorted array.
     * This method assumes that the subarrays arr[left...mid] and arr[mid+1...right]
     * are already sorted.
     *
     * @param arr   The original array containing the subarrays.
     * @param temp  Temporary array to store merged elements.
     * @param left  The starting index of the left subarray.
     * @param mid   The ending index of the left subarray (and mid+1 is start of right).
     * @param right The ending index of the right subarray.
     */
    private static void merge(int[] arr, int[] temp, int left, int mid, int right) {
        // Copy both halves into the temporary array
        for (int i = left; i <= right; i++) {
            temp[i] = arr[i];
        }

        int i = left;       // Pointer for the left subarray (temp)
        int j = mid + 1;    // Pointer for the right subarray (temp)
        int k = left;       // Pointer for the original array (arr)

        // Merge the two halves back into the original array (arr) by comparing elements
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                arr[k] = temp[i];
                i++;
            } else {
                arr[k] = temp[j];
                j++;
            }
            k++;
        }

        // Copy any remaining elements of the left subarray (if any)
        while (i <= mid) {
            arr[k] = temp[i];
            i++;
            k++;
        }

        // Copy any remaining elements of the right subarray (if any)
        // (This loop is often not strictly necessary because if the left subarray
        // is exhausted first, the remaining elements of the right subarray are
        // already in their correct relative sorted positions within 'temp'
        // and thus also in 'arr' from the initial copy. However, including
        // it makes the logic complete and symmetrical for all cases.)
        while (j <= right) {
            arr[k] = temp[j];
            j++;
            k++;
        }
    }

    /**
     * Helper method to print an array.
     *
     * @param arr The array to be printed.
     */
    public static void printArray(int[] arr) {
        if (arr.length == 0) {
            System.out.println("[]");
            return;
        }
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        int[] data1 = {12, 11, 13, 5, 6, 7};
        System.out.println("Original Array 1: ");
        printArray(data1);
        mergeSort(data1);
        System.out.println("Sorted Array 1:   ");
        printArray(data1);
    }
}