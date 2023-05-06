package io.github.timeway.leetcode.algorithms._1419;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MySolution
 *
 * @author Zexin Li
 * @date 2023-05-06 15:24
 */
public class MySolution {
    public int minNumberOfFrogs(String croakOfFrogs) {
        if (croakOfFrogs.length() % 5 != 0) {
            return -1;
        }

        Map<Character, Integer> indexMap = new HashMap<>();
        indexMap.put('c', 0);
        indexMap.put('r', 1);
        indexMap.put('o', 2);
        indexMap.put('a', 3);
        indexMap.put('k', 4);

        int res = 0;
        int frogNum = 0;
        int[] charCount = new int[5];
        for (int i = 0; i < croakOfFrogs.length(); i++) {
            char c = croakOfFrogs.charAt(i);
            Integer index = indexMap.get(c);
            // 如果数量很大的时候，这块++有可能超出int的最大范围
            charCount[index]++;
            if (index == 0) {
                frogNum++;
                res = Math.max(res, frogNum);
            } else {
                if (charCount[index - 1] < charCount[index]) {
                    return -1;
                }
                if (index == 4) {
                    frogNum--;
                }
            }
        }

        if (frogNum != 0) {
            return -1;
        }

        return res;
    }

    public static void main(String[] args) {
        List<String> caseList = Arrays.asList(
                "croakcroakcroakcroak", // 1
                "croackroak" // 2
        );
        MySolution solution = new MySolution();
        for (String croakOfFrogs : caseList) {
            System.out.println(solution.minNumberOfFrogs(croakOfFrogs) + ": \t" + croakOfFrogs);
        }
    }
}
