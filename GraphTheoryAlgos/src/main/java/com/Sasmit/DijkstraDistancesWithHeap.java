package com.Sasmit;

import java.io.*;
import java.util.*;

public class DijkstraDistancesWithHeap {
    private Map<Integer, List<int[]>> graph;

    public DijkstraDistancesWithHeap(String inputFile) throws IOException {
        this.graph = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                int vertex = Integer.parseInt(parts[0]);
                List<int[]> edges = new ArrayList<>();

                for (int i = 1; i < parts.length; i++) {
                    String edgeStr = parts[i].replace("(", "").replace(")", "");
                    String[] edgeParts = edgeStr.split(",");
                    edges.add(new int[]{
                            Integer.parseInt(edgeParts[0]),
                            Integer.parseInt(edgeParts[1])
                    });
                }

                this.graph.put(vertex, edges);
            }
        }
    }

    public Map<Integer, Integer> dijkstra(int source) {
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        Map<Integer, Integer> distances = new HashMap<>();
        Set<Integer> processed = new HashSet<>();

        // Initialize
        for (int vertex : graph.keySet()) {
            distances.put(vertex, Integer.MAX_VALUE);
        }
        distances.put(source, 0);
        heap.offer(new int[]{source, 0});

        while (!heap.isEmpty()) {
            int[] current = heap.poll();
            int vertex = current[0];
            int dist = current[1];

            if (processed.contains(vertex)) continue;
            if (dist > distances.get(vertex)) continue;

            processed.add(vertex);

            for (int[] edge : graph.get(vertex)) {
                int neighbor = edge[0];
                int weight = edge[1];
                int newDist = dist + weight;

                if (!processed.contains(neighbor) && newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    heap.offer(new int[]{neighbor, newDist});
                }
            }
        }

        return distances;
    }

    public static void main(String[] args) {
        try {
            DijkstraDistancesWithHeap pathFinder = new DijkstraDistancesWithHeap("/Users/sasmitmunagala/Desktop/Sasmit_Algos/GraphTheoryAlgos/src/main/java/com/Sasmit/dijkstraList");
            int source = pathFinder.graph.keySet().iterator().next();
            Map<Integer, Integer> distances = pathFinder.dijkstra(source);

            System.out.println(distances.get(7));
            System.out.println(distances.get(37));
            System.out.println(distances.get(59));
            System.out.println(distances.get(82));
            System.out.println(distances.get(99));
            System.out.println(distances.get(115));
            System.out.println(distances.get(133));
            System.out.println(distances.get(165));
            System.out.println(distances.get(188));
            System.out.println(distances.get(197));

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}