package com.Sasmit;

import java.io.*;
import java.util.*;

public class DijkstraPathFinder {
    private Map<Integer, List<int[]>> graph;
    private int sourceVertex;

    public DijkstraPathFinder(String inputFile) throws IOException {
        this.graph = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                int vertex = Integer.parseInt(parts[0]);
                List<int[]> edges = new ArrayList<>();

                for (int i = 1; i < parts.length; i++) {
                    // Parse edge format like "(2,3)" -> [2, 3]
                    String edgeStr = parts[i].replace("(", "").replace(")", "");
                    String[] edgeParts = edgeStr.split(",");
                    int[] edge = {
                            Integer.parseInt(edgeParts[0]),
                            Integer.parseInt(edgeParts[1])
                    };
                    edges.add(edge);
                }

                this.graph.put(vertex, edges);
            }
        }

        // Get first vertex as source (equivalent to next(iter(graph.keys())))
        this.sourceVertex = this.graph.keySet().iterator().next();
    }

    public Map<Integer, PathInfo> computeShortestPaths() {
        return computeShortestPaths(this.sourceVertex);
    }

    public Map<Integer, PathInfo> computeShortestPaths(int source) {
        Map<Integer, PathInfo> shortestPaths = new HashMap<>();
        Set<Integer> visited = new HashSet<>();

        // Initialize all vertices with infinite distance
        for (int vertex : this.graph.keySet()) {
            shortestPaths.put(vertex, new PathInfo(Integer.MAX_VALUE, new ArrayList<>()));
        }

        // Set source distance to 0
        shortestPaths.put(source, new PathInfo(0, new ArrayList<>()));
        visited.add(source);

        // Main Dijkstra loop
        while (!this.graph.keySet().equals(visited)) {
            int sourceVertex = -1;
            int[] minEdge = null;

            // Find minimum edge crossing the frontier
            for (int vertex : visited) {
                for (int[] edge : this.graph.get(vertex)) {
                    int neighbor = edge[0];
                    int weight = edge[1];

                    if (visited.contains(neighbor)) {
                        continue; // Skip if neighbor already visited
                    }

                    int newDistance = shortestPaths.get(vertex).distance + weight;

                    if (minEdge == null || newDistance < minEdge[1]) {
                        minEdge = new int[]{neighbor, newDistance};
                        sourceVertex = vertex;
                    }
                }
            }

            if (minEdge != null) {
                int targetVertex = minEdge[0];
                int newDistance = minEdge[1];

                // Update shortest path
                List<Integer> newPath = new ArrayList<>(shortestPaths.get(sourceVertex).path);
                newPath.add(targetVertex);

                shortestPaths.put(targetVertex, new PathInfo(newDistance, newPath));
                visited.add(targetVertex);
            }
        }

        return shortestPaths;
    }

    // Helper class to store distance and path
    public static class PathInfo {
        public final int distance;
        public final List<Integer> path;

        public PathInfo(int distance, List<Integer> path) {
            this.distance = distance;
            this.path = path;
        }
    }

    public static void main(String[] args) {
        try {
            DijkstraPathFinder pathFinder = new DijkstraPathFinder("/Users/sasmitmunagala/Desktop/Sasmit_Algos/GraphTheoryAlgos/src/main/java/com/Sasmit/dijkstraList");
            Map<Integer, PathInfo> paths = pathFinder.computeShortestPaths();

            // Extract distances (equivalent to Python's dictionary comprehension)
            Map<Integer, Integer> actual = new HashMap<>();
            for (Map.Entry<Integer, PathInfo> entry : paths.entrySet()) {
                actual.put(entry.getKey(), entry.getValue().distance);
            }

            System.out.println("RESULTS: ");
            System.out.println(actual.get(7));
            System.out.println(actual.get(37));
            System.out.println(actual.get(59));
            System.out.println(actual.get(82));
            System.out.println(actual.get(99));
            System.out.println(actual.get(115));
            System.out.println(actual.get(133));
            System.out.println(actual.get(165));
            System.out.println(actual.get(188));
            System.out.println(actual.get(197));

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}