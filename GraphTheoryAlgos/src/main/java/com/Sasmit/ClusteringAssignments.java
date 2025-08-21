package com.Sasmit;

import java.io.*;
import java.util.*;

/**
 * Implements:
 * 1) Weighted k-clustering (max spacing) from cluster.txt
 * 2) Big clustering with Hamming distance <= 2 from clustering-big.txt
 * 3) UnionFind faithful to the provided Python (root + subtree)
 */
public class ClusteringAssignments {

    // =============================
    // Union-Find (faithful to Python version)
    // =============================
    static class UnionFind {
        // node -> representative
        final Map<Integer, Integer> root = new HashMap<>();
        // representative -> members
        final Map<Integer, List<Integer>> subtree = new HashMap<>();

        UnionFind(Collection<Integer> nodes) {
            for (int node : nodes) {
                root.put(node, node);
                subtree.put(node, new ArrayList<>(List.of(node)));
            }
        }

        int find(int node) {
            return root.get(node);
        }

        void union(int i, int j) {
            int pi = find(i);
            int pj = find(j);
            if (pi == pj) return;

            // merge smaller into larger (like your Python)
            if (subtree.get(pj).size() > subtree.get(pi).size()) {
                int tmp = pi;
                pi = pj;
                pj = tmp;
            }

            for (int node : subtree.get(pj)) {
                root.put(node, pi);
                subtree.get(pi).add(node);
            }
            subtree.remove(pj);
        }

        int numClusters() {
            return subtree.size();
        }
    }

    // =============================
    // Part 1: Weighted k-clustering (max spacing)
    // =============================
    static class WeightedEdge {
        final int u, v, cost;

        WeightedEdge(int u, int v, int cost) {
            this.u = u;
            this.v = v;
            this.cost = cost;
        }
    }

    static List<WeightedEdge> readCluster(String filename) throws IOException {
        List<WeightedEdge> edges = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // header (ignored)
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                int u = Integer.parseInt(parts[0]);
                int v = Integer.parseInt(parts[1]);
                int cost = Integer.parseInt(parts[2]);
                edges.add(new WeightedEdge(u, v, cost));
            }
        }
        return edges;
    }

    static int kClusteringWeighted(List<WeightedEdge> graph, int k) {
        Set<Integer> nodes = new HashSet<>();
        for (WeightedEdge e : graph) {
            nodes.add(e.u);
            nodes.add(e.v);
        }
        UnionFind uf = new UnionFind(nodes);

        graph.sort(Comparator.comparingInt(e -> e.cost));

        int idx = 0;
        while (uf.numClusters() > k && idx < graph.size()) {
            WeightedEdge e = graph.get(idx++);
            uf.union(e.u, e.v);
        }

        // first edge that links two different clusters => max spacing
        while (idx < graph.size()) {
            WeightedEdge e = graph.get(idx++);
            if (uf.find(e.u) != uf.find(e.v)) return e.cost;
        }
        return -1; // not expected
    }

    // =============================
    // Part 2: Big clustering (Hamming distance <= 2)
    // =============================
    static class BigInput {
        final Map<Integer, List<Integer>> nodes; // integer code -> line indices (handles dupes)
        final int bitLength;

        BigInput(Map<Integer, List<Integer>> nodes, int bitLength) {
            this.nodes = nodes;
            this.bitLength = bitLength;
        }
    }

    /**
     * Reads clustering-big.txt robustly:
     * - If header has two ints "N BITS", uses BITS as bitLength.
     * - Else infers bitLength from first data line's bit-string length.
     * Stores nodes as integers (spaces removed, base-2).
     */
    static BigInput readBig(String filename) throws IOException {
        Map<Integer, List<Integer>> nodes = new HashMap<>();
        int bitLength;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String first = br.readLine();           // header or first data line (Python skipped first line)
            String firstData = null;

            // read all remaining lines and remember the first non-empty one to infer bit length if needed
            List<String> dataLines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String t = line.trim();
                if (t.isEmpty()) continue;
                dataLines.add(t);
                if (firstData == null) firstData = t;
            }

            // Determine bitLength
            int parsedHeaderBits = -1;
            if (first != null) {
                String[] parts = first.trim().split("\\s+");
                if (parts.length >= 2 && isInt(parts[0]) && isInt(parts[1])) {
                    parsedHeaderBits = Integer.parseInt(parts[1]);
                }
            }
            if (parsedHeaderBits > 0) {
                bitLength = parsedHeaderBits;
            } else {
                if (firstData == null) throw new IllegalStateException("No data lines found in " + filename);
                String binary = firstData.replaceAll("\\s+", "");
                bitLength = binary.length();
            }

            // Parse nodes
            int idx = 0;
            for (String s : dataLines) {
                String binary = s.replaceAll("\\s+", "");
                if (binary.isEmpty()) continue;
                int num = Integer.parseInt(binary, 2);
                nodes.computeIfAbsent(num, k -> new ArrayList<>()).add(idx++);
            }
        }

        return new BigInput(nodes, bitLength);
    }

    // Precompute masks for speed (no allocations per node)
    static int[] masks1(int bitLength) {
        int[] m = new int[bitLength];
        for (int i = 0; i < bitLength; i++) m[i] = (1 << i);
        return m;
    }

    static int[] masks2(int bitLength) {
        int count = bitLength * (bitLength - 1) / 2;
        int[] m = new int[count];
        int k = 0;
        for (int i = 0; i < bitLength; i++) {
            for (int j = i + 1; j < bitLength; j++) {
                m[k++] = (1 << i) ^ (1 << j);
            }
        }
        return m;
    }

    /**
     * Clusters nodes where Hamming distance <= 2 using your exact Python approach:
     * for each node, union with any node at distance 1 or 2.
     */
    static int kClusteringBig(Map<Integer, List<Integer>> nodes, int bitLength) {
        UnionFind uf = new UnionFind(nodes.keySet());

        // IMPORTANT: iterate over a snapshot to avoid iterator surprises
        List<Integer> keys = new ArrayList<>(nodes.keySet());

        int[] m1 = masks1(bitLength);
        int[] m2 = masks2(bitLength);

        for (int num : keys) {
            // Hamming distance 1
            for (int mask : m1) {
                int code = num ^ mask;
                if (nodes.containsKey(code)) uf.union(num, code);
            }
            // Hamming distance 2
            for (int mask : m2) {
                int code = num ^ mask;
                if (nodes.containsKey(code)) uf.union(num, code);
            }
        }

        return uf.numClusters();
    }

    // =============================
    // Helpers
    // =============================
    private static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // =============================
    // Main driver
    // =============================
    public static void main(String[] args) throws IOException {
        // ---- Part 1: Weighted k-clustering (max spacing)
        List<WeightedEdge> graph = readCluster("/Users/sasmitmunagala/Desktop/Sasmit_Algos/GraphTheoryAlgos/src/main/java/com/Sasmit/cluster.txt");
        int spacing = kClusteringWeighted(graph, 4);
        System.out.println("Max spacing of 4-clustering = " + spacing);

        // ---- Part 2: Big clustering with Hamming distance <= 2
        BigInput input = readBig("/Users/sasmitmunagala/Desktop/Sasmit_Algos/GraphTheoryAlgos/src/main/java/com/Sasmit/clustering_big.txt");
        int numClusters = kClusteringBig(input.nodes, input.bitLength);
        System.out.println("Number of clusters (Hamming <= 2) = " + numClusters);
    }
}
