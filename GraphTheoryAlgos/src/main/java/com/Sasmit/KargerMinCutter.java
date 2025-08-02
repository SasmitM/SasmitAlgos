package com.Sasmit;

import java.io.*;
import java.util.*;

public class KargerMinCutter {
    private Map<Integer, List<Integer>> graph;
    private int totalEdges;

    public KargerMinCutter(String filename) throws IOException {
        graph = new HashMap<>();
        totalEdges = 0;
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.trim().split("\\s+");
            int node = Integer.parseInt(tokens[0]);
            List<Integer> neighbors = new ArrayList<>();
            for (int i = 1; i < tokens.length; i++) {
                neighbors.add(Integer.parseInt(tokens[i]));
            }
            graph.put(node, neighbors);
            totalEdges += neighbors.size();
        }
        br.close();
    }

    public int findMinCut() {
        // Make a deep copy so we can reuse the original graph
        Map<Integer, List<Integer>> localGraph = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : graph.entrySet()) {
            localGraph.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        int localTotalEdges = totalEdges;

        Random rand = new Random();

        while (localGraph.size() > 2) {
            // Pick a random edge
            int randEdge = rand.nextInt(localTotalEdges);
            int fromVertex = -1;
            int toVertex = -1;

            for (Map.Entry<Integer, List<Integer>> entry : localGraph.entrySet()) {
                List<Integer> edges = entry.getValue();
                if (edges.size() <= randEdge) {
                    randEdge -= edges.size();
                } else {
                    fromVertex = entry.getKey();
                    toVertex = edges.get(randEdge);
                    break;
                }
            }

            // Merge toVertex into fromVertex
            List<Integer> fromEdges = localGraph.get(fromVertex);
            List<Integer> toEdges = localGraph.get(toVertex);

            localTotalEdges -= fromEdges.size();
            localTotalEdges -= toEdges.size();

            fromEdges.addAll(toEdges);

            for (int v : toEdges) {
                List<Integer> neighborEdges = localGraph.get(v);
                int finalToVertex = toVertex;
                neighborEdges.removeIf(e -> e == finalToVertex);
                neighborEdges.add(fromVertex);
            }

            // Remove self-loops
            int finalFromVertex = fromVertex;
            fromEdges.removeIf(v -> v == finalFromVertex);

            localTotalEdges += fromEdges.size();
            localGraph.remove(toVertex);
        }

        // Return size of the remaining edges (which should be the same for both nodes)
        for (List<Integer> edges : localGraph.values()) {
            return edges.size();
        }
        return -1;
    }

    public static void main(String[] args) throws IOException {
        int minCut = Integer.MAX_VALUE;
        int trials = 5000;

        for (int i = 0; i < trials; i++) {
            KargerMinCutter cutter = new KargerMinCutter("");
            int cut = cutter.findMinCut();
            if (cut < minCut) {
                minCut = cut;
            }
            System.out.println("Trial " + (i + 1) + ": Min Cut = " + minCut);
        }

        System.out.println("Final Min Cut: " + minCut);
    }
}
