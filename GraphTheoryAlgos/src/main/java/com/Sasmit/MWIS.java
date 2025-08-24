package com.Sasmit;

import java.io.*;
import java.util.*;

public class MWIS {

    // Read graph weights from file
    public static List<Integer> readGraph(String filepath) throws IOException {
        List<Integer> num = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String line;
        br.readLine(); // skip first line (number of vertices)
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                num.add(Integer.parseInt(line.split("\\s+")[0]));
            }
        }
        br.close();
        return num;
    }

    // Compute MWIS
    public static MWISResult mwis(List<Integer> num) {
        int n = num.size();
        long[] W = new long[n + 1]; // max weight array
        W[0] = 0;
        W[1] = num.get(0);

        // DP to compute max weights
        for (int i = 2; i <= n; i++) {
            W[i] = Math.max(W[i - 1], W[i - 2] + num.get(i - 1));
        }

        // Reconstruct MWIS
        List<Integer> S = new ArrayList<>();
        int i = n;
        while (i > 0) {
            if (i == 1) {
                S.add(1);
                break;
            }
            if (W[i - 1] >= W[i - 2] + num.get(i - 1)) {
                i--;
            } else {
                S.add(i);
                i -= 2;
            }
        }

        Collections.sort(S); // optional: sort in increasing order
        return new MWISResult(W[n], S);
    }

    public static void main(String[] args) throws IOException {


        List<Integer> num = readGraph("/Users/sasmitmunagala/Desktop/Sasmit_Algos/GraphTheoryAlgos/src/main/java/com/Sasmit/dynamic.txt");
        MWISResult result = mwis(num);
        int[] candidates = {1, 2, 3, 4, 17, 117, 517, 997};

        StringBuilder sb = new StringBuilder();
        Set<Integer> vertexSet = new HashSet<>(result.vertices);
        for (int c : candidates) {
            sb.append(vertexSet.contains(c) ? "1" : "0");
        }
        System.out.println(sb.toString());
    }

    // Helper class to return both weight and set
    static class MWISResult {
        long weight;
        List<Integer> vertices;

        MWISResult(long weight, List<Integer> vertices) {
            this.weight = weight;
            this.vertices = vertices;
        }
    }
}
