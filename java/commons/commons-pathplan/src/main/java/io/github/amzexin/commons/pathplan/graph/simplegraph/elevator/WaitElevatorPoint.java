package io.github.amzexin.commons.pathplan.graph.simplegraph.elevator;

import lombok.Data;

/**
 * Description:
 *
 * @author Vincent
 * @date 2020-01-18 14:07
 */
@Data
public class WaitElevatorPoint implements Comparable<WaitElevatorPoint> {

    /**
     * 站点编号
     */
    private String wayPointId;

    /**
     * 所属楼层
     */
    private String floor;

    /**
     * 所属地图
     */
    private String mapId;

    /**
     * 所属电梯
     */
    private String liftId;

    @Override
    public int compareTo(WaitElevatorPoint that) {
        return this.getFloor().compareTo(that.getFloor());
    }
}
