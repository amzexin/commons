package io.github.amzexin.commons.pathplan.astar;

import lombok.Data;

import java.awt.*;

/**
 * Description:
 *
 * @author Vincent
 * @date 2020-05-26 21:53
 */
@Data
public class AStarNode implements Comparable<AStarNode> {
    /**
     * 位置
     */
    private Point position;
    /**
     * 起点到当前节点的预估消耗
     */
    private double gCost;
    /**
     * 当前节点到终点的预估消耗
     */
    private double hCost;
    /**
     * 父节点
     */
    private AStarNode parent;

    public double fCost() {
        return gCost + hCost;
    }

    /**
     * 计算F
     *
     * @param parent
     * @param end
     */
    public void calculateF(AStarNode parent, Point end) {
        this.gCost = parent.getGCost() + AStarCostUtil.cost(parent.getPosition(), this.position);
        this.hCost = AStarCostUtil.cost(this.position, end);
    }

    @Override
    public int compareTo(AStarNode that) {
        if (this.getPosition().equals(that.getPosition())) {
            return 0;
        }
        return Double.compare(this.fCost(), that.fCost());
    }

    public AStarNode(Point position) {
        this.position = position;
    }
}
