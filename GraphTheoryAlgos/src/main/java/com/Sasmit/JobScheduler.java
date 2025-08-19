package com.Sasmit;

import java.io.*;
import java.util.*;

public class JobScheduler {

    // Helper class to store weight and length
    static class Job {
        int weight;
        int length;

        Job(int weight, int length) {
            this.weight = weight;
            this.length = length;
        }
    }

    /**
     * Load the weight and length from txt file
     */
    public static List<Job> readJobs(String filename) throws IOException {
        List<Job> jobs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            int n = Integer.parseInt(br.readLine().trim()); // first line is number of jobs
            for (int i = 0; i < n; i++) {
                String[] parts = br.readLine().trim().split("\\s+");
                int w = Integer.parseInt(parts[0]);
                int l = Integer.parseInt(parts[1]);
                jobs.add(new Job(w, l));
            }
        }
        return jobs;
    }

    /**
     * Compute the weighted completion time given weights and lengths
     */
    public static long computeCost(List<Job> jobs) {
        long cost = 0;
        long time = 0;
        for (Job job : jobs) {
            time += job.length;
            cost += (long) job.weight * time;
        }
        return cost;
    }

    /**
     * Sort the jobs using difference (w - l), breaking ties with higher weight
     */
    public static List<Job> greedyDiff(List<Job> jobs) {
        List<Job> sorted = new ArrayList<>(jobs);
        sorted.sort((a, b) -> {
            int diffA = a.weight - a.length;
            int diffB = b.weight - b.length;
            if (diffA != diffB) {
                return Integer.compare(diffB, diffA); // larger difference first
            }
            return Integer.compare(b.weight, a.weight); // tie-break by weight
        });
        return sorted;
    }

    /**
     * Sort the jobs using ratio (w / l)
     */
    public static List<Job> greedyRatio(List<Job> jobs) {
        List<Job> sorted = new ArrayList<>(jobs);
        sorted.sort((a, b) -> {
            double ratioA = (double) a.weight / a.length;
            double ratioB = (double) b.weight / b.length;
            return Double.compare(ratioB, ratioA); // larger ratio first
        });
        return sorted;
    }

    public static void main(String[] args) throws IOException {
        List<Job> jobs = readJobs("/Users/sasmitmunagala/Desktop/Sasmit_Algos/GraphTheoryAlgos/src/main/java/com/Sasmit/jobs.txt");

        long costDiff = computeCost(greedyDiff(jobs));
        System.out.println("Difference heuristic cost: " + costDiff);

        long costRatio = computeCost(greedyRatio(jobs));
        System.out.println("Ratio heuristic cost: " + costRatio);
    }
}
