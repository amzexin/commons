package io.github.timeway.leetcode.algorithms._1419;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://leetcode.cn/problems/minimum-number-of-frogs-croaking/
 *
 * @author Zexin Li
 * @date 2023-05-06 15:05
 */
public class Solution {
    public int minNumberOfFrogs(String croakOfFrogs) {
        if (croakOfFrogs.length() % 5 != 0) {
            return -1;
        }
        int res = 0, frogNum = 0;
        int[] cnt = new int[4];
        Map<Character, Integer> map = new HashMap<Character, Integer>() {{
            put('c', 0);
            put('r', 1);
            put('o', 2);
            put('a', 3);
            put('k', 4);
        }};
        for (int i = 0; i < croakOfFrogs.length(); i++) {
            char c = croakOfFrogs.charAt(i);
            int t = map.get(c);
            if (t == 0) {
                cnt[t]++;
                frogNum++;
                if (frogNum > res) {
                    res = frogNum;
                }
            } else {
                if (cnt[t - 1] == 0) {
                    return -1;
                }
                // 官方题解通过 -- 操作，可以使其非常慢的达到 int 最大值。
                // 在官方题解下其实很难达到 int 最大值
                cnt[t - 1]--;
                if (t == 4) {
                    frogNum--;
                } else {
                    cnt[t]++;
                }
            }
        }
        if (frogNum > 0) {
            return -1;
        }
        return res;
    }

    public static void main(String[] args) {
        List<String> caseList = Arrays.asList(
                "croakcroakcroakcroak", // 1
                "croackroak" // 2
        );
        Solution solution = new Solution();
        for (String croakOfFrogs : caseList) {
            System.out.println(solution.minNumberOfFrogs(croakOfFrogs) +  ": \t"  + croakOfFrogs);
        }
    }
}
