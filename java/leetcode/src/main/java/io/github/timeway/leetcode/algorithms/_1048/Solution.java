package io.github.timeway.leetcode.algorithms._1048;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/longest-string-chain/
 */
public class Solution {
    public int longestStrChain(String[] words) {
        Arrays.sort(words, Comparator.comparingInt(String::length));

        int result = 0;
        Map<String, Integer> chainLengthMap = new HashMap<>();
        for (String word : words) {
            int chainLength = 1;
            for (int i = 0; i < word.length(); i++) {
                // word的前身
                String prev = word.substring(0, i) + word.substring(i + 1);
                if (chainLengthMap.containsKey(prev)) {
                    chainLength = Math.max(chainLength, chainLengthMap.get(prev) + 1);
                }
            }
            chainLengthMap.put(word, chainLength);
            result = Math.max(result, chainLength);
        }
        return result;
    }
}
