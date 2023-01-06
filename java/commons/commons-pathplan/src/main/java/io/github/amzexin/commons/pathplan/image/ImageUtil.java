package io.github.amzexin.commons.pathplan.image;


import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
public class ImageUtil {

    public static Image getColorImage(BufferedImage image) {
        BufferedImage resImg = new BufferedImage(image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_3BYTE_BGR);

        resImg.getGraphics().drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);

        return resImg;
    }

    /**
     * 膨胀运算
     * 障碍物减少
     *
     * @param mat
     * @return
     */
    public static int[][] expandImage(int[][] mat, int maskSize) {
        int[] maskData = new int[(2 * maskSize + 1) * (2 * maskSize + 1)];
        // 腐蚀核默认都是1
        for (int i = 0; i < maskData.length; i++) {
            maskData[i] = 1;
        }

        int h = mat.length;
        int w = mat[0].length;

        int[][] result = new int[h][w];

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                result[y][x] = mat[y][x];

                ///边缘不进行操作，边缘内才操作
                if (x < maskSize || x >= w - maskSize || y < maskSize || y >= h - maskSize) {
                    continue;
                }

                //膨胀操作是取 mask里最大的值
                int maxPixel = 0;

                ///对该像素周围的mask遍历，卷积核
                for (int k = 0; k < maskData.length; k++) {
                    if (maskData[k] == 0) {
                        continue;
                    }
                    int deltaX = k / (2 * maskSize + 1);
                    int deltaY = k % (2 * maskSize + 1);

                    //对应的像素值
                    int value = mat[y + deltaY - maskSize][x + deltaX - maskSize];
                    if (maxPixel < value) {
                        maxPixel = value;
                    }
                }

                //取mask内最小的像素值
                result[y][x] = maxPixel;

            }
        }
        return result;
    }


    /**
     * 腐蚀运算
     * 障碍物增加
     * https://blog.csdn.net/u013230291/article/details/83543420
     * https://blog.csdn.net/alw_123/article/details/83868878
     * https://blog.csdn.net/abcd_d_/article/details/20401299?utm_source=blogxgwz2
     *
     * @param mat
     */
    public static int[][] corrosionImage(int[][] mat, int maskSize) {
        int[] maskData = new int[(2 * maskSize + 1) * (2 * maskSize + 1)];
        // 腐蚀核默认都是1
        for (int i = 0; i < maskData.length; i++) {
            maskData[i] = 1;
        }

        int h = mat.length;
        int w = mat[0].length;

        int[][] result = new int[h][w];

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                result[y][x] = mat[y][x];

                ///边缘不进行操作，边缘内才操作
                if (x < maskSize || x >= w - maskSize || y < maskSize || y >= h - maskSize) {
                    continue;
                }

                //腐蚀操作是取 mask里最小的值
                int minPixel = 1;

                ///对该像素周围的mask遍历，卷积核
                for (int k = 0; k < maskData.length; k++) {
                    if (maskData[k] == 0) {
                        continue;
                    }
                    int deltaX = k / (2 * maskSize + 1);
                    int deltaY = k % (2 * maskSize + 1);

                    //对应的像素值
                    int value = mat[y + deltaY - maskSize][x + deltaX - maskSize];
                    if (minPixel > value) {
                        minPixel = value;
                    }
                }

                //取mask内最小的像素值
                result[y][x] = minPixel;

            }
        }
        return result;
    }

    /**
     * 叠加多边形
     *
     * @param image
     * @param xPoints
     * @param yPoints
     */
    public static void drawRect(Image image, int xPoints[], int yPoints[]) {
        Graphics g = image.getGraphics();
        //画笔颜色
        g.setColor(Color.black);

        int nPoint = xPoints.length;
        g.fillPolygon(xPoints, yPoints, nPoint);
    }

    /**
     * 画点
     *
     * @param image
     * @param x
     * @param y
     */
    public static void drawCircle(Image image, int x, int y, int size) {
        Graphics g = image.getGraphics();
        //画笔颜色
        g.setColor(Color.RED);

        g.fillArc(x, y, size, size, 0, 360);
    }


    /**
     * 图像二值化
     *
     * @param image
     * @return
     */
    public static int[][] image2Binary(Image image) {
        BufferedImage imageCopy = (BufferedImage) image;

        int h = image.getHeight(null);
        int w = image.getWidth(null);
        int mat[][] = new int[h][w];

        // 获取图片每一像素点的灰度值
        int threadHold = 250;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                // getRGB()返回默认的RGB颜色模型(十进制)
                //该点的灰度值
                int grey = getImageRgb(imageCopy.getRGB(x, y));
                if (grey > threadHold) {
                    //白色
                    mat[y][x] = 1;
                } else {
                    mat[y][x] = 0;
                }
            }
        }
        return mat;
    }

    /**
     * 图像转化成二维数组
     *
     * @param image
     * @return
     */
    public static int[][] image2Array(Image image) {
        BufferedImage imageCopy = (BufferedImage) image;

        int h = image.getHeight(null);
        int w = image.getWidth(null);
        int mat[][] = new int[h][w];

        // 获取图片每一像素点的灰度值
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                // getRGB()返回默认的RGB颜色模型(十进制)
                mat[y][x] = imageCopy.getRGB(x, y);
            }
        }
        return mat;
    }

    /**
     * 二维数组转图片
     *
     * @param mat
     * @return
     */
    public static Image binary2Image(int[][] mat) {
        int h = mat.length;
        int w = mat[0].length;
        //  构造一个类型为预定义图像类型之一的 BufferedImage，TYPE_BYTE_BINARY（表示一个不透明的以字节打包的 1、2 或 4 位图像。）
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

        // 获取图片每一像素点的灰度值
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                //白色
                if (mat[y][x] == 1) {
                    bufferedImage.setRGB(x, y, new Color(255, 255, 255).getRGB());
                } else {
                    bufferedImage.setRGB(x, y, new Color(0, 0, 0).getRGB());
                }
            }
        }
        return bufferedImage;
    }

    /**
     * 二维数组转图片
     *
     * @param mat
     * @return
     */
    public static Image array2Image(int[][] mat) {
        int h = mat.length;
        int w = mat[0].length;
        //  构造一个类型为预定义图像类型之一的 BufferedImage，TYPE_BYTE_BINARY（表示一个不透明的以字节打包的 1、2 或 4 位图像。）
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

        // 获取图片每一像素点的灰度值
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                bufferedImage.setRGB(x, y, mat[y][x]);
            }
        }
        return bufferedImage;
    }


    /**
     * 获取灰度值
     *
     * @param i
     * @return
     */
    private static int getImageRgb(int i) {
        // 将十进制的颜色值转为十六进制
        String argb = Integer.toHexString(i);
        // argb分别代表透明,红,绿,蓝 分别占16进制2位
        int r = Integer.parseInt(argb.substring(2, 4), 16);//后面参数为使用进制
        int g = Integer.parseInt(argb.substring(4, 6), 16);
        int b = Integer.parseInt(argb.substring(6, 8), 16);
        return (r + g + b) / 3;
    }

    /**
     * 截取图片
     *
     * @param image
     * @param x
     * @param y
     * @param cutW
     * @param cutH
     */
    public static BufferedImage subImage(BufferedImage image, int x, int y, int cutW, int cutH) {
        Rectangle rect = new Rectangle(x, y, cutW, cutH);
        BufferedImage areaImage = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
        // 新建一个40*40的Image
        BufferedImage buffImg = new BufferedImage(cutW, cutH, BufferedImage.TYPE_INT_RGB);
        buffImg.getGraphics().drawImage(areaImage.getScaledInstance(rect.width, rect.height, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        return buffImg;
    }

    /**
     * 对图片进行放大
     *
     * @param originalImage 原始图片
     * @param times         放大倍数
     * @return
     */
    public static BufferedImage zoomInImage(BufferedImage originalImage, Integer times) {
        int width = originalImage.getWidth() * times;
        int height = originalImage.getHeight() * times;
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

}


