package io.github.amzexin.commons.pathplan.pgs;

import io.github.amzexin.commons.pathplan.astar.AStarSearch;
import io.github.amzexin.commons.pathplan.common.Http302;
import io.github.amzexin.commons.pathplan.image.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2020-08-17 17:41
 */
public class GlobalSearch {

    /**
     * 原地图数据
     */
    private BufferedImage originalImage;

    /**
     * 二值化后的数据（膨胀、腐蚀后的）
     */
    private int[][] binaryImageData;

    // region debug相关
    private Integer debugNum;
    private String debugPath;
    // endregion

    /**
     * 初始化
     *
     * @param imagePath
     * @throws IOException
     */
    public GlobalSearch(String imagePath) throws IOException {
        this.debugNum = 0;
        this.debugPath = "/Users/lizexin/lizx/projects-my/graph/src/test/java/com/lizx/util/test/pgs";
        // 获取底图
        if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
            this.originalImage = ImageIO.read(new URL(imagePath));
            if (originalImage == null) {
                originalImage = ImageIO.read(new URL(Http302.realUrl(imagePath)));
            }
        } else {
            this.originalImage = ImageIO.read(new File(imagePath));
        }
        debug();
        // 二值化
        this.binaryImageData = ImageUtil.image2Binary(originalImage);
        debug();
        // 膨胀处理（消除小的障碍物毛刺）
//        this.binaryImageData = ImageUtil.expandImage(binaryImageData, 2);
        debug();
        // 腐蚀处理 （扩大障碍物，以车的半径 0.4m ）
        // 默认腐蚀半径是0.4m ，=8像素， 所以  maskSize是17 * 17 像素
        this.binaryImageData = ImageUtil.corrosionImage(binaryImageData, 6);
        debug();
    }

    public void drawRect(List<Point> area) throws IOException {
        int[] xPoints = new int[area.size()];
        int[] yPoints = new int[area.size()];
        for (int i = 0; i < area.size(); i++) {
            xPoints[i] = (int) area.get(i).getX();
            yPoints[i] = (int) area.get(i).getY();
        }
        Image binaryImageCopy = ImageUtil.binary2Image(binaryImageData);
        ImageUtil.drawRect(binaryImageCopy, xPoints, yPoints);
        this.binaryImageData = ImageUtil.image2Binary(binaryImageCopy);
        debug();
    }

    public int height() {
        return binaryImageData.length;
    }

    public int width() {
        return binaryImageData[0].length;
    }

    public List<Point> search(Point start, Point end) throws IOException {
        debug(start, end);
        List<Point> path = AStarSearch.searchPath(binaryImageData, start, end);
        debug(path);
        return path;
    }

    private void debug(List<Point> path) throws IOException {
        if (debugNum == null) {
            return;
        }
        // 二值化地图
        Image binaryImageCopy = ImageUtil.binary2Image(binaryImageData);
        // 原地图绘制
        int[][] originalImageData = ImageUtil.image2Array(originalImage);
        Image originalImageCopy = ImageUtil.array2Image(originalImageData);
        // 绘制轨迹
        for (Point route : path) {
            ImageUtil.drawCircle(binaryImageCopy, (int) route.getX(), (int) route.getY(), 2);
            ImageUtil.drawCircle(originalImageCopy, (int) route.getX(), (int) route.getY(), 2);
        }
        write(originalImageCopy);
        write(binaryImageCopy);
    }

    private void debug(Point start, Point end) throws IOException {
        if (debugNum == null) {
            return;
        }
        Image imageCopy = ImageUtil.binary2Image(binaryImageData);
        ImageUtil.drawCircle(imageCopy, (int) start.getX(), (int) start.getY(), 8);
        ImageUtil.drawCircle(imageCopy, (int) end.getX(), (int) end.getY(), 8);
        write(imageCopy);
    }

    private void debug() throws IOException {
        if (debugNum == null) {
            return;
        }
        Image imageCopy = originalImage;
        if (binaryImageData != null) {
            imageCopy = ImageUtil.binary2Image(binaryImageData);
        }
        write(imageCopy);
    }

    private void write(Image image) throws IOException {
        ImageIO.write((RenderedImage) image, "png", new File(String.format("%s/%s.png", debugPath, ++debugNum)));
    }

}
