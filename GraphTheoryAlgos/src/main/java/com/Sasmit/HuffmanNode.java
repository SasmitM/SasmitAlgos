package com.Sasmit;

class HuffmanNode implements Comparable<HuffmanNode> {
    int weight;
    int minLen;
    int maxLen;

    HuffmanNode(int weight, int minLen, int maxLen) {
        this.weight = weight;
        this.minLen = minLen;
        this.maxLen = maxLen;
    }

    @Override
    public int compareTo(HuffmanNode other) {
        return this.weight - other.weight; // min-heap based on weight
    }
}
