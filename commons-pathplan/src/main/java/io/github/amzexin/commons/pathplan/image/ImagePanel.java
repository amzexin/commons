package io.github.amzexin.commons.pathplan.image;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private Image image;

    public ImagePanel(Image image) {
        //setOpaque(false);//设置透明色    这个不能少，不然也会看不到效果
        updateImage(image);
        this.setBackground(Color.lightGray);
    }

    public void updateImage(Image image) {
        this.image = image;
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, 50, image.getWidth(null), image.getHeight(null), null);
    }

    public Image getImage() {
        return image;
    }
}


