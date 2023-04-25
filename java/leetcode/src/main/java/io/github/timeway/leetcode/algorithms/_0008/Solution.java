package io.github.timeway.leetcode.algorithms._0008;

/**
 * https://leetcode.cn/problems/string-to-integer-atoi/
 */
public class Solution {
    public int myAtoi(String s) {
        int startIndex = -1;
        int numStrLength = 0;
        boolean negative = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // 数字前 特殊字符 处理逻辑
            if (startIndex == -1) {
                // 空格 就跳过
                if (c == ' ') {
                    continue;
                }
                if (c == '+' || c == '-') {
                    negative = c == '-';
                    // +，- 后面是数字才能继续
                    if (i + 1 < s.length()) {
                        char nextChar = s.charAt(i + 1);
                        if (nextChar >= '0' && nextChar <= '9') {
                            startIndex = negative ? i : i + 1;
                            numStrLength = negative ? 2 : 1;
                            i++;
                            continue;
                        }
                    }
                    // 正负号后面不是数字，直接退出
                    break;
                }
                // 其他字符直接退出
                if (c < '0' || c > '9') {
                    break;
                }
            }
            // 数字后 遇到特殊字符 直接退出
            else if (c < '0' || c > '9') {
                break;
            }

            // 处理实际数字
            if (startIndex == -1) {
                startIndex = i;
            }
            numStrLength++;
        }

        if (startIndex == -1) {
            return 0;
        }

        // 去除前缀0
        StringBuilder numStrBuilder = new StringBuilder();
        boolean numStarted = false;
        for (int i = startIndex; i < startIndex + numStrLength; i++) {
            char c = s.charAt(i);
            if (c != '0' && c != '-') {
                numStarted = true;
            }
            if (c == '0' && !numStarted) {
                continue;
            }
            numStrBuilder.append(s.charAt(i));
        }

        // 判断是否为0
        String numStr = numStrBuilder.toString();
        if (numStr.isEmpty() || (numStr.length() == 1 && numStr.charAt(0) == '-')) {
            return 0;
        }

        // 判断是否超过int的界限
        int maxNum = negative ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        String maxNumStr = maxNum + "";
        if (numStr.length() > maxNumStr.length()) {
            return maxNum;
        }
        if (numStr.length() == maxNumStr.length() && numStr.compareTo(maxNumStr) >= 0) {
            return maxNum;
        }

        // 界限内的直接解析
        return Integer.parseInt(numStr);
    }
}
