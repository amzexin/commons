package io.github.amzexin.commons.pathplan.test.pgs;

import com.alibaba.fastjson.JSON;
import io.github.amzexin.commons.pathplan.pgs.AlgorithmPosition;
import io.github.amzexin.commons.pathplan.pgs.PlannerGlobalSearch;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2020-08-18 11:07
 */
public class PlannerGlobalSearchTest {


    @Test
    public void test() throws IOException {
        String imagePath = "/Users/lizexin/lizx/projects-my/graph/src/test/java/com/lizx/util/test/pgs/slam2.png";
        AlgorithmPosition algorithmSourcePosition = new AlgorithmPosition(-20.786434D, -15.422831D);
        double mapResolution = 0.05D;
        PlannerGlobalSearch plannerGlobalSearch = new PlannerGlobalSearch(imagePath, algorithmSourcePosition, mapResolution);

        // 绘制不可通行区域
        String area1 = "[{\"x\":5.064781137768816,\"y\":-0.3236651035907159},{\"x\":5.046023073252687,\"y\":-1.8742794515891292},{\"x\":5.027265008736558,\"y\":-2.0868636767179427},{\"x\":6.25904457862903,\"y\":-2.174398357653339},{\"x\":6.202770385080644,\"y\":-0.30490767196170054}]";
        List<AlgorithmPosition> areaOne = JSON.parseArray(area1, AlgorithmPosition.class);
        plannerGlobalSearch.drawRect(areaOne);

        // 寻路
        AlgorithmPosition start = new AlgorithmPosition(-5.91237922D, 0.785453795D);
//        AlgorithmPosition end = new AlgorithmPosition(3.88931D, -0.462853D);
        AlgorithmPosition end = new AlgorithmPosition(-2.20141D, 0.0880968D);
        List<AlgorithmPosition> search = plannerGlobalSearch.search(start, end);
        System.out.println(JSON.toJSONString(search));
    }

    /**
     * 去掉消除毛刺
     *
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        long strart = System.currentTimeMillis();
        // 地图属性（地图png、算法起点坐标、地图分辨率）
        String imagePath = "/Users/lizexin/lizx/projects-my/graph/src/test/java/com/lizx/util/test/pgs/slam3.png";
        AlgorithmPosition algorithmSourcePosition = new AlgorithmPosition(-22.04288D, -17.873915D);
        double mapResolution = 0.05D;
        PlannerGlobalSearch plannerGlobalSearch = new PlannerGlobalSearch(imagePath, algorithmSourcePosition, mapResolution);

        // 寻路
        AlgorithmPosition start = new AlgorithmPosition(-2.3366881D, 4.764617D);
        AlgorithmPosition end = new AlgorithmPosition(0.907041D, -0.253082D);
        plannerGlobalSearch.search(start, end);
        System.out.println("耗时：" + (System.currentTimeMillis() - strart));
    }
}
