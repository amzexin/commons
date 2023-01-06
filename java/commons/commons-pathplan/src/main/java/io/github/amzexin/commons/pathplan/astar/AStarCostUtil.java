package io.github.amzexin.commons.pathplan.astar;

import java.awt.*;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2020-05-28 15:28
 */
public class AStarCostUtil {

    /**
     * 计算两点间的预估消耗
     *
     * @param source
     * @param target
     * @return
     */
    public static double cost(Point source, Point target) {
        return method1(source, target);
    }

    /**
     * 曼哈顿H(n) = x + y
     *
     * @param target
     */
    private static double method1(Point source, Point target) {
        int xCost = (int) Math.abs(target.getX() - source.getX());
        int yCost = (int) Math.abs(target.getY() - source.getY());
        if (xCost == 1 && yCost == 1) {
            return 14;
        }
        return (xCost + yCost) * 10;
    }

    /**
     * 欧几里得式H(n) = sqrt(x^2 + y^2)
     *
     * @param target
     */
    private static double method2(Point source, Point target) {
        return Math.sqrt(Math.pow(target.getX() - source.getX(), 2) + Math.pow(target.getY() - source.getY(), 2));
    }

    private AStarCostUtil() {
    }

}
