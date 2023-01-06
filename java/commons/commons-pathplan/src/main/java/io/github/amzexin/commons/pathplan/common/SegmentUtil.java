package io.github.amzexin.commons.pathplan.common;

/**
 * Description: segment util
 *
 * @author Lizexin
 * @date 2019-12-31 16:45
 */
public class SegmentUtil {
    // region 向量差积求解法

    /**
     * 两线段是否相交
     * 相交返回true, 不相交返回false
     * line1 : a ------- b // a, b为一条线段两端点
     * line2 : c ------- d // c, d为另一条线段的两端点
     * https://www.cnblogs.com/tuyang1129/p/9390376.html
     *
     * @param line1ax
     * @param line1ay
     * @param line1bx
     * @param line1by
     * @param line2cx
     * @param line2cy
     * @param line2dx
     * @param line2dy
     * @return
     */
    public static boolean intersect(double line1ax, double line1ay, double line1bx, double line1by,
                                    double line2cx, double line2cy, double line2dx, double line2dy) {
        if (Math.max(line1ax, line1bx) < Math.min(line2cx, line2dx)) {
            return false;
        }
        if (Math.max(line1ay, line1by) < Math.min(line2cy, line2dy)) {
            return false;
        }
        if (Math.max(line2cx, line2dx) < Math.min(line1ax, line1bx)) {
            return false;
        }
        if (Math.max(line2cy, line2dy) < Math.min(line1ay, line1by)) {
            return false;
        }
        if (VectorUtil.multiply(line2cx, line2cy, line1bx, line1by, line1ax, line1ay) * VectorUtil.multiply(line1bx, line1by, line2dx, line2dy, line1ax, line1ay) < 0) {
            return false;
        }
        if (VectorUtil.multiply(line1ax, line1ay, line2dx, line2dy, line2cx, line2cy) * VectorUtil.multiply(line2dx, line2dy, line1bx, line1by, line2cx, line2cy) < 0) {
            return false;
        }
        return true;
    }

    // endregion

    private SegmentUtil() {
    }
}
