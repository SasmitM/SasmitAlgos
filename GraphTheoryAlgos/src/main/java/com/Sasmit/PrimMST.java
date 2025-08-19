package com.Sasmit;

import java.io.*;
import java.util.*;

public class PrimMST {

    /**
     * Load the graph from a txt file
     */
    public static Map<String, Integer> loadGraph(String filename, Set<Integer> nodes) throws IOException {
        Map<String, Integer> graph = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // first line is number of nodes/edges, skip
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                int v1 = Integer.parseInt(parts[0]);
                int v2 = Integer.parseInt(parts[1]);
                int c = Integer.parseInt(parts[2]);

                // store edge cost with both directions
                graph.put(v1 + "," + v2, c);
                graph.put(v2 + "," + v1, c);

                nodes.add(v1);
                nodes.add(v2);
            }
        }
        return graph;
    }

    /**
     * Prim's MST implementation
     */
    public static long mst(Map<String, Integer> graph, Set<Integer> nodes) {
        Set<Integer> span = new HashSet<>();
        List<Integer> nodeList = new ArrayList<>(nodes);
        span.add(nodeList.get(0)); // start with any node
        long totalCost = 0;

        while (span.size() < nodes.size()) {
            int bestNode = -1;
            int minCost = Integer.MAX_VALUE;

            for (int v1 : span) {
                for (int v2 : nodes) {
                    if (!span.contains(v2)) {
                        String key = v1 + "," + v2;
                        if (graph.containsKey(key) && graph.get(key) < minCost) {
                            minCost = graph.get(key);
                            bestNode = v2;
                        }
                    }
                }
            }

            if (bestNode == -1) {
                throw new RuntimeException("Graph is not connected!");
            }

            span.add(bestNode);
            totalCost += minCost;
        }

        return totalCost;
    }

    public static void main(String[] args) throws IOException {
        Set<Integer> nodes = new HashSet<>();
        Map<String, Integer> graph = loadGraph("/Users/sasmitmunagala/Desktop/Sasmit_Algos/GraphTheoryAlgos/src/main/java/com/Sasmit/edges.txt", nodes);

        long cost = mst(graph, nodes);
        System.out.println("MST total cost: " + cost);
    }
}
