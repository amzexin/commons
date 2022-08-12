package io.github.amzexin.commons.pathplan.test.pgs;

import io.github.amzexin.commons.pathplan.pgs.GlobalSearch;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2020-08-18 11:07
 */
public class GlobalSearchTest {

    private Point start = new Point(370, 380);
    private Point end = new Point(500, 600);

    @Test
    public void test() throws IOException {
        GlobalSearch plannerGlobalSearch = new GlobalSearch("/Users/lizexin/lizx/projects-my/graph/src/test/java/com/lizx/util/test/pgs/slam.png");
        plannerGlobalSearch.search(start, end);
    }
}
