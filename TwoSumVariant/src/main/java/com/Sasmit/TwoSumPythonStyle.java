package com.Sasmit;

import java.io.*;
import java.util.*;

public class TwoSumPythonStyle {

    public static void main(String[] args) {
        String filename = "/Users/sasmitmunagala/Desktop/Sasmit_Algos/TwoSumVariant/src/main/java/com/Sasmit/input.txt"; // replace with your file path
        List<Long> nums = readNumbers(filename);

        int count = twoSum(nums, -10000, 10000);
        System.out.println("Number of target values: " + count);
    }

    // Reads numbers from file into a list and stores in a hash map for fast lookup
    private static List<Long> readNumbers(String filename) {
        List<Long> nums = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                long num = Long.parseLong(line.trim());
                nums.add(num);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nums;
    }

    // Implements the 2-SUM variant like the Python code
    private static int twoSum(List<Long> nums, int low, int high) {
        Map<Long, Boolean> hashtable = new HashMap<>();
        for (Long num : nums) {
            hashtable.put(num, true);
        }

        Set<Integer> foundTargets = new HashSet<>();

        for (int target = low; target <= high; target++) {
            Map<Long, Boolean> tmpDict = new HashMap<>(hashtable); // copy, like Python
            for (Long num : nums) {
                long y = target - num;
                if (tmpDict.containsKey(y) && num != y) {
                    foundTargets.add(target);
                    break; // stop checking this target
                }
            }
        }

        return foundTargets.size();
    }
}
