package io.github.timeway.leetcode.algorithms._0008;

/**
 * _0008Test
 *
 * @author Zexin Li
 * @date 2023-04-25 17:24
 */
public class _0008Test {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.myAtoi("-"));
        System.out.println(solution.myAtoi("   -42"));
        System.out.println(solution.myAtoi("-91283472332"));
        System.out.println(solution.myAtoi("  0000000000012345678"));
        System.out.println(solution.myAtoi("00000-42a1234"));
        System.out.println(solution.myAtoi("42"));
        System.out.println(solution.myAtoi("-000000000000001"));
        System.out.println(solution.myAtoi("  -0k4"));
        System.out.println(solution.myAtoi("    +1146905820n1"));
    }
}
