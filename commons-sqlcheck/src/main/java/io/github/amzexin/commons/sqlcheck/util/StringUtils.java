package io.github.amzexin.commons.sqlcheck.util;

import java.util.List;

/**
 * Description: StringUtils
 *
 * @author Lizexin
 * @date 2021-11-10 20:14
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (a != null && a.equalsIgnoreCase(b));
    }

    public static boolean containsList(List<String> list, List<String> subList) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        if (subList == null || subList.isEmpty()) {
            return false;
        }

        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).equalsIgnoreCase(subList.get(0))) {
                continue;
            }
            for (int ii = i + 1, j = 1; ii < list.size() && j < subList.size(); ii++, j++) {
                if (!list.get(ii).equalsIgnoreCase(subList.get(j))) {
                    break;
                }
                if (j == subList.size() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private StringUtils() {
    }
}
