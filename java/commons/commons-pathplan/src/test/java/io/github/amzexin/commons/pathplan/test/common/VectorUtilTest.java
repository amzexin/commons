package io.github.amzexin.commons.pathplan.test.common;

import io.github.amzexin.commons.pathplan.common.Vector;
import io.github.amzexin.commons.pathplan.common.VectorUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2019-12-31 17:03
 */
@Slf4j
public class VectorUtilTest {

    @Test
    public void case1() {

        Vector v1 = new Vector(-67.03435126578454D, -21.57682384562519D, -67.04607506044661D, -21.59948842819231D);

        Vector v2 = new Vector(-67.03435126578454D, -21.57682384562519D, -69.43067963789416D, -25.398487701921088D);
        Vector v3 = new Vector(-67.03435126578454D, -21.57682384562519D, -71.68758880707195D, -23.185539467276D);
        Vector v4 = new Vector(-67.03435126578454D, -21.57682384562519D, -71.3493166584831D, -26.709663892257034D);

        Vector v5 = new Vector(-67.03435126578454D, -21.57682384562519D, -67.070687456989D, -21.59289299423706D);

        long start = System.currentTimeMillis();
        double angle = VectorUtil.angle(v1, v2);
        log.info("v1,v2 = [{}], 耗时：{}ms", angle, System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        angle = VectorUtil.angle(v1, v3);
        log.info("v1,v3 = [{}], 耗时：{}ms", angle, System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        angle = VectorUtil.angle(v1, v4);
        log.info("v1,v4 = [{}], 耗时：{}ms", angle, System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            angle = VectorUtil.angle(v1, v5);
        }
        log.info("v1,v5 = [{}], 耗时：{}ms", angle, System.currentTimeMillis() - start);

    }
}
