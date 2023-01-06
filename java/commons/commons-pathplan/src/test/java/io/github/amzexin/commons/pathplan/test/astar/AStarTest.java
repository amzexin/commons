package io.github.amzexin.commons.pathplan.test.astar;

import com.alibaba.fastjson.JSON;
import io.github.amzexin.commons.pathplan.astar.history.AStarSearchH1;
import org.junit.Test;

import java.awt.*;
import java.util.List;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2020-05-27 16:08
 */
public class AStarTest {

    @Test
    public void test1() {
        int[][] map = new int[6][6];
        map[1][3] = 1;
        map[2][3] = 1;
        map[3][3] = 1;

        Point start = new Point(1, 2);
        Point end = new Point(4, 3);
        List<Point> path = AStarSearchH1.searchPath(map, start, end);
        for (Point aStarPoint : path) {
            System.out.println(JSON.toJSONString(aStarPoint));
        }
    }
}
