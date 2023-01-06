package io.github.amzexin.commons.pathplan.test.image;

import io.github.amzexin.commons.pathplan.astar.AStarSearch;
import io.github.amzexin.commons.pathplan.image.ImagePanel;
import io.github.amzexin.commons.pathplan.image.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ImageSearch2Test {

//    private Point start = new Point(75, 30);
//    private Point end = new Point(220, 250);

    private Point start = new Point(170, 65);
    private Point end = new Point(300, 460);

    @Test
    public void test1() throws IOException {
        // 底图
        BufferedImage image = ImageIO.read(new File("/Users/lizexin/lizx/projects-my/graph/src/test/java/com/lizx/util/test/image/slam2.png"));
        ImagePanel imagePanel = new ImagePanel(image);

        // 叠加多边形
        imagePanel.add(drawRect(imagePanel));
        // 重置
        imagePanel.add(reset(imagePanel, image));
        // 二值化
        imagePanel.add(image2Binaryzation(imagePanel));
        // 膨胀
        imagePanel.add(expandImage(imagePanel));
        // 腐蚀
        imagePanel.add(corrosionImage(imagePanel));
        // 起始点与结束点
        imagePanel.add(setPoint(imagePanel));
        // 搜索路径
        imagePanel.add(searchPath(imagePanel));


        // 设置图形界面
        JFrame jFrame = new JFrame("图像处理");
        // 设置窗口位置和大小
        jFrame.setBounds(300, 100, 800, 800);
        //设置滚动
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.add(imagePanel);
        jFrame.add(scrollPane);
        // 设置窗口可见
        jFrame.setVisible(true);


        System.in.read();
    }

    private Component searchPath(ImagePanel imagePanel) {
        JButton button = new JButton("A*寻路");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] mat = ImageUtil.image2Binary(imagePanel.getImage());
                List<Point> aStarPoints = AStarSearch.searchPath(mat, start, end);
                // 路径可视化
                for (Point route : aStarPoints) {
                    // 可视化
                    ImageUtil.drawCircle(imagePanel.getImage(), (int) route.getX(), (int) route.getY(), 2);
                }
                imagePanel.updateImage(imagePanel.getImage());
            }
        });
        return button;
    }

    private Component setPoint(ImagePanel imagePanel) {
        JButton button = new JButton("设置起始点与目标点");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageUtil.drawCircle(imagePanel.getImage(), (int) start.getX(), (int) start.getY(), 8);
                ImageUtil.drawCircle(imagePanel.getImage(), (int) end.getX(), (int) end.getY(), 8);
                imagePanel.updateImage(imagePanel.getImage());
            }
        });
        return button;
    }

    private Component reset(ImagePanel imagePanel, BufferedImage image) {
        JButton button = new JButton("重置");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePanel.updateImage(image);
            }
        });
        return button;
    }

    private Component corrosionImage(ImagePanel imagePanel) {
        JButton button = new JButton("腐蚀");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] mat = ImageUtil.image2Binary(imagePanel.getImage());
                // 腐蚀处理 （扩大障碍物，以车的半径 0.4m ）
                // 默认腐蚀半径是0.4m ，=8像素， 所以  maskSize是17 * 17 像素
                int[][] newMat = ImageUtil.corrosionImage(mat, 8);
                Image newImage = ImageUtil.binary2Image(newMat);
                imagePanel.updateImage(newImage);
            }
        });
        return button;
    }

    private Component expandImage(ImagePanel imagePanel) {
        JButton button = new JButton("膨胀");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] mat = ImageUtil.image2Binary(imagePanel.getImage());
                // 膨胀处理（消除小的障碍物毛刺）
                int[][] newMat = ImageUtil.expandImage(mat, 2);
                Image newImage = ImageUtil.binary2Image(newMat);
                imagePanel.updateImage(newImage);
            }
        });
        return button;
    }

    private Component image2Binaryzation(ImagePanel imagePanel) {
        JButton button = new JButton("二值化");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] binarization = ImageUtil.image2Binary(imagePanel.getImage());
                Image newImage = ImageUtil.binary2Image(binarization);
                imagePanel.updateImage(newImage);
            }
        });
        return button;
    }

    private Component drawRect(ImagePanel imagePanel) {
        JButton button = new JButton("叠加多边形");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] xPoints = {
                        500, 560, 610, 600, 550, 500
                };
                int[] yPoints = {
                        100, 80, 120, 200, 200, 200
                };
                ImageUtil.drawRect(imagePanel.getImage(), xPoints, yPoints);
                imagePanel.updateImage(imagePanel.getImage());
            }
        });
        return button;
    }

}
