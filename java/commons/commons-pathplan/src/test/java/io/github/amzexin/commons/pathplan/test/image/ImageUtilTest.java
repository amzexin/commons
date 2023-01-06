package io.github.amzexin.commons.pathplan.test.image;

import io.github.amzexin.commons.pathplan.image.ImageUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2020-05-29 18:51
 */
public class ImageUtilTest {

    @Test
    public void subImage() throws IOException {
        BufferedImage image = ImageIO.read(new File("/Users/lizexin/lizx/projects-my/graph/src/test/java/com/lizx/util/test/image/slam.png"));
        BufferedImage bufferedImage = ImageUtil.subImage(image, 290, 356, 300, 300);
        ImageIO.write(bufferedImage, "png", new File("/Users/lizexin/lizx/projects-my/graph/src/test/java/com/lizx/util/test/image/slamcut.png"));
    }

    @Test
    public void zoomInImage() throws IOException {
        BufferedImage image = ImageIO.read(new File("/Users/lizexin/lizx/projects-my/graph/src/test/java/com/lizx/util/test/image/slamcut.png"));
        BufferedImage bufferedImage = ImageUtil.zoomInImage(image, 2);
        ImageIO.write(bufferedImage, "png", new File("/Users/lizexin/lizx/projects-my/graph/src/test/java/com/lizx/util/test/image/slam2.png"));
    }
}
