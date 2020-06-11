package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends javax.swing.JComponent {
    public BufferedImage image;
    //Конструктор
    JImageDisplay(int width, int height){
        image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        this.setPreferredSize(new Dimension(width,height));
    }
    //Переопределение метода суперкласса
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }
    //Очистка изображения
    public void clearImage(){
        for (int i = 0; i<image.getHeight(); i++){
            for(int j = 0; j<image.getWidth(); j++){
                image.setRGB(i,j,0);
            }
        }
    }
    //Установка пикселя в определённый цвет
    public void drawPixel(int x, int y, int colorRGB){
        image.setRGB(x,y,colorRGB);
    }
}
