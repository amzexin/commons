package io.github.amzexin.commons.pathplan.test.graph.simplegraph.shortestpath.elevator;

import com.alibaba.fastjson.JSON;
import io.github.amzexin.commons.pathplan.graph.simplegraph.elevator.ElevatorPathPlan;
import io.github.amzexin.commons.pathplan.graph.simplegraph.elevator.WaitElevatorPoint;
import io.github.amzexin.commons.pathplan.common.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Description:
 *
 * @author Vincent
 * @date 2020-01-19 14:46
 */
@Slf4j
public class TakeTheElevatorTest {

    private ElevatorPathPlan liftPathPlan;

    @Before
    public void before() {
        String filePath;
        // home
        filePath = "/Users/lizx/projects/Projects-myself/graph/src/test/java/com/lizx/util/graph/test/simplegraph/shortestpath/ elevator/";
        // company
        filePath = "/Users/lizexin/lizx/projects-my/graph/src/test/java/com/lizx/util/graph/test/simplegraph/shortestpath/elevator/";

        String jsonStr = FileUtil.readJsonFile(filePath + "WaitElevatorPointData.json");
        List<WaitElevatorPoint> allCallingLiftPoint = JSON.parseArray(jsonStr, WaitElevatorPoint.class);

        liftPathPlan = new ElevatorPathPlan(allCallingLiftPoint);
    }

    @Test
    public void case1() {

        long start = System.currentTimeMillis();
        // All_1-2 -> B_6-1 -> B_6-2 -> B_11-1 -> B_11-2 -> B_15 -> 1ms
        List<WaitElevatorPoint> path = liftPathPlan.path("All_one", "B_fifteen");
        for (WaitElevatorPoint callLiftPoint : path) {
            System.out.print(callLiftPoint.getWayPointId() + " -> ");
        }
        System.out.println((System.currentTimeMillis() - start) + "ms");

    }

}
