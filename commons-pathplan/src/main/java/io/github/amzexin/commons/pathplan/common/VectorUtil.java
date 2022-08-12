package io.github.amzexin.commons.pathplan.common;

/**
 * Description: vector util
 *
 * @author Lizexin
 * @date 2019-12-31 16:43
 */
public class VectorUtil {

    /**
     * 求ca与cb的差积
     *
     * @param target1X
     * @param target1Y
     * @param target2X
     * @param target2Y
     * @param sourceX
     * @param sourceY
     * @return
     */
    public static double multiply(double target1X, double target1Y, double target2X, double target2Y,
                                  double sourceX, double sourceY) {
        return (target1X - sourceX) * (target2Y - sourceY) - (target2X - sourceX) * (target1Y - sourceY);
    }

    /**
     * 求vector1与求vector2的夹角
     * https://blog.csdn.net/DSTJWJW/article/details/84258760
     *
     * @param vector1SourceX
     * @param vector1SourceY
     * @param vector1TargetX
     * @param vector1TargetY
     * @param vector2SourceX
     * @param vector2SourceY
     * @param vector2TargetX
     * @param vector2TargetY
     * @return
     */
    public static double angle(double vector1SourceX, double vector1SourceY, double vector1TargetX, double vector1TargetY,
                               double vector2SourceX, double vector2SourceY, double vector2TargetX, double vector2TargetY) {
        double angle1 = Math.atan2(vector1TargetY - vector1SourceY, vector1TargetX - vector1SourceX);
        angle1 = Math.toDegrees(angle1);

        double angle2 = Math.atan2(vector2TargetY - vector2SourceY, vector2TargetX - vector2SourceX);
        angle2 = Math.toDegrees(angle2);

        double includedAngle;
        if (angle1 * angle2 >= 0)
            includedAngle = Math.abs(angle1 - angle2);
        else {
            includedAngle = Math.abs(angle1) + Math.abs(angle2);
            if (includedAngle > 180)
                includedAngle = 360 - includedAngle;
        }
        return includedAngle;
    }

    public static double angle(Vector v1, Vector v2) {
        return angle(v1.getSourceX(), v1.getSourceY(), v1.getTargetX(), v1.getTargetY(),
                v2.getSourceX(), v2.getSourceY(), v2.getTargetX(), v2.getTargetY());
    }

    private VectorUtil() {
    }
}
