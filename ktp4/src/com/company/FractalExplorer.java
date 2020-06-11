package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.lang.annotation.Annotation;

public class FractalExplorer extends JFrame {
    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(800);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.drawFractal();
    }

    private int size;
    private JImageDisplay imageDisplay;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double range;

    FractalExplorer(int n){
        this.size = n;
        range = new Rectangle2D.Double();
        fractalGenerator = new Mandelbrot();
        imageDisplay = new JImageDisplay(n,n);
    }

    public void createAndShowGUI(){
        JFrame frame = new JFrame("Fractal");
        JButton clear = new JButton("Clear image");
        clearClickEvent event = new clearClickEvent();
        MouseHandler mouseHandler = new MouseHandler();

        imageDisplay.setLayout(new BorderLayout());
        frame.add(imageDisplay, BorderLayout.CENTER);
        frame.add(clear,BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        clear.addActionListener(event);
        imageDisplay.addMouseListener(mouseHandler);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal(){
        for(int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                double xCoord = FractalGenerator.getCoord (range.x, range.x + range.width, size, x);
                double yCoord = FractalGenerator.getCoord (range.y, range.y + range.width, size, y);
                int iterations = fractalGenerator.numIterations(xCoord,yCoord);
                if (iterations == -1){imageDisplay.drawPixel(x,y,0);}
                else{
                    float hue =0.7f+(float)iterations/200f;
                    int rgbColor = Color.HSBtoRGB(hue,1f,1f);
                    imageDisplay.drawPixel(x,y,rgbColor);
                }
            }
        }
        imageDisplay.repaint();
    }

    private class clearClickEvent implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            fractalGenerator.getInitialRange(range);
            drawFractal();
        }
    }

    private class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            double xCoord = FractalGenerator.getCoord (range.x, range.x + range.width, size, x);
            double yCoord = FractalGenerator.getCoord (range.y, range.y + range.width, size, y);
            fractalGenerator.recenterAndZoomRange(range,xCoord,yCoord,0.5);
            drawFractal();
        }
    }
}
