package com.gregei.potato8.desktop.views;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainView extends JFrame {
    /**
     * JPanel to be drawn on
     */
    private JPanel canvas;

    /**
     * The image to be drawn
     */
    private BufferedImage image;

    public MainView(int width, int height, int scale){
        canvas = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(image, 0, 0, width * scale, height * scale, null);
            }
        };

        canvas.setPreferredSize(new Dimension(width * scale, height * scale));
        getContentPane().add(canvas);
        pack();
    }

    /**
     * Repaints the canvas panel with the passed image.
     *
     * @param image the image to be drawn
     */
    public void repaint(BufferedImage image){
        this.image = image;
        canvas.repaint();
    }
}