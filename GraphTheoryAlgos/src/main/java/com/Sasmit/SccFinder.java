package com.Sasmit;

import java.io.*;
import java.util.*;

public class SccFinder {
    private Map<Integer, List<Integer>> graph;
    private List<Integer> finishOrder;
    private List<Integer> sccList;

    public SccFinder(String inputFile) throws IOException {
        this.graph = new HashMap<>();
        this.finishOrder = new ArrayList<>();
        this.sccList = new ArrayList<>();

        // Read the file and build the graph
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 2) {
                    int fromV = Integer.parseInt(parts[0]);
                    int toV = Integer.parseInt(parts[1]);
                    addEdgeToGraph(fromV, toV);
                }
            }
        }
    }

    private void addEdgeToGraph(int fromV, int toV) {
        // Add forward edge
        graph.computeIfAbsent(fromV, k -> new ArrayList<>()).add(toV);

        // Add reverse edge (negative value indicates reverse)
        graph.computeIfAbsent(toV, k -> new ArrayList<>()).add(-fromV);

        // Ensure both vertices exist in the graph even if they have no outgoing edges
        graph.putIfAbsent(fromV, new ArrayList<>());
        graph.putIfAbsent(toV, new ArrayList<>());
    }

    public void computeFinishTimes() {
        Set<Integer> visitedNodes = new HashSet<>();
        Set<Integer> finishedNodes = new HashSet<>();

        for (Integer vertex : graph.keySet()) {
            if (visitedNodes.contains(vertex)) {
                continue;
            }

            Stack<Integer> nodesStack = new Stack<>();
            nodesStack.push(vertex);

            while (!nodesStack.isEmpty()) {
                Integer node = nodesStack.pop();

                if (!visitedNodes.contains(node)) {
                    visitedNodes.add(node);
                    nodesStack.push(node);

                    // Get neighbors from reverse edges (negative values)
                    for (Integer edge : graph.get(node)) {
                        if (edge < 0) {
                            Integer neighbor = -edge;
                            if (!visitedNodes.contains(neighbor)) {
                                nodesStack.push(neighbor);
                            }
                        }
                    }
                } else {
                    if (!finishedNodes.contains(node)) {
                        finishOrder.add(node);
                        finishedNodes.add(node);
                    }
                }
            }
        }
    }

    public void computeSccs() {
        Set<Integer> visitedNodes = new HashSet<>();

        // Process nodes in reverse finish order
        for (int i = finishOrder.size() - 1; i >= 0; i--) {
            Integer startNode = finishOrder.get(i);

            if (visitedNodes.contains(startNode)) {
                continue;
            }

            Stack<Integer> nodesStack = new Stack<>();
            nodesStack.push(startNode);
            int size = 0;

            while (!nodesStack.isEmpty()) {
                Integer node = nodesStack.pop();

                if (!visitedNodes.contains(node)) {
                    size++;
                    visitedNodes.add(node);

                    // Get neighbors from forward edges (positive values)
                    for (Integer edge : graph.get(node)) {
                        if (edge > 0) {
                            if (!visitedNodes.contains(edge)) {
                                nodesStack.push(edge);
                            }
                        }
                    }
                }
            }

            sccList.add(size);
        }

        // Sort SCCs by size in descending order
        sccList.sort(Collections.reverseOrder());
    }

    public List<Integer> getSccList() {
        return sccList;
    }

    public void printTopSccs(int n) {
        System.out.print("[");
        for (int i = 0; i < Math.min(n, sccList.size()); i++) {
            System.out.print(sccList.get(i));
            if (i < Math.min(n, sccList.size()) - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        try {
            SccFinder sccFinder = new SccFinder("/Users/sasmitmunagala/Desktop/Sasmit_Algos/GraphTheoryAlgos/src/main/java/com/Sasmit/assignment4");
            sccFinder.computeFinishTimes();
            sccFinder.computeSccs();

            // Print top 5 SCCs
            sccFinder.printTopSccs(5);

            // Expected SCCs
            int[] expectedSccs = {434821, 968, 459, 313, 211};
            System.out.println("Expected: " + Arrays.toString(expectedSccs));

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}