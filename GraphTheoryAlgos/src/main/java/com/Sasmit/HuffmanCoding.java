package com.Sasmit;

import java.io.*;
import java.util.*;

public class HuffmanCoding {

    public static Map<Integer, HuffmanNode> readCode(String filepath) throws IOException {
        Map<Integer, HuffmanNode> code = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String line;
        int i = 0;
        // skip first line (usually number of symbols)
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            int weight = Integer.parseInt(parts[0]);
            code.put(i++, new HuffmanNode(weight, 0, 0));
        }
        br.close();
        return code;
    }

    public static int[] huffmanCoding(Map<Integer, HuffmanNode> code) {
        PriorityQueue<HuffmanNode> heap = new PriorityQueue<>(code.values());

        while (heap.size() > 1) {
            HuffmanNode i = heap.poll();
            HuffmanNode j = heap.poll();

            HuffmanNode merged = new HuffmanNode(
                    i.weight + j.weight,
                    1 + Math.min(i.minLen, j.minLen),
                    1 + Math.max(i.maxLen, j.maxLen)
            );
            heap.add(merged);
        }

        HuffmanNode root = heap.poll();
        return new int[]{root.minLen, root.maxLen};
    }

    public static void main(String[] args) throws IOException {
        Map<Integer, HuffmanNode> code = readCode("/Users/sasmitmunagala/Desktop/Sasmit_Algos/GraphTheoryAlgos/src/main/java/com/Sasmit/huffmanFile.txt");
        int[] result = huffmanCoding(code);
        System.out.println("File min/max: " + Arrays.toString(result));
    }
}
