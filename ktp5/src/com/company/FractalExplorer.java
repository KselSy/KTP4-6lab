package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;


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
        fractalGenerator.getInitialRange(range);
        imageDisplay = new JImageDisplay(n,n);
    }

    public void createAndShowGUI(){
        Mandelbrot fractalMandelbrot = new Mandelbrot();
        Tricorn fractalTricorn = new Tricorn();
        BurningShip fractalBurningShip = new BurningShip();

        JFrame frame = new JFrame("Fractal");
        JButton clearButton = new JButton("Clear image");
        JButton saveButton = new JButton("Save Image");
        MouseHandler mouseHandler = new MouseHandler();
        JComboBox typeFractalBox = new JComboBox();

        JPanel panelSaveAndClear = new JPanel();
        panelSaveAndClear.add(saveButton);
        panelSaveAndClear.add(clearButton);

        JLabel labelBox = new JLabel("Вид фрактала:");
        JPanel typeFractalPanel = new JPanel();
        ButtonClickEvent clickEvent = new ButtonClickEvent();

        typeFractalPanel.add(labelBox);
        typeFractalPanel.add(typeFractalBox);

        clearButton.setActionCommand("clear");
        saveButton.setActionCommand("save");

        typeFractalBox.addItem(fractalMandelbrot);
        typeFractalBox.addItem(fractalBurningShip);
        typeFractalBox.addItem(fractalTricorn);

        imageDisplay.setLayout(new BorderLayout());
        frame.add(imageDisplay, BorderLayout.CENTER);
        frame.add(panelSaveAndClear,BorderLayout.SOUTH);
        frame.add(typeFractalPanel,BorderLayout.NORTH);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        clearButton.addActionListener(clickEvent);
        saveButton.addActionListener(clickEvent);
        typeFractalBox.addActionListener(clickEvent);
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

    private class ButtonClickEvent implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Object object = e.getSource();
            if (object instanceof JComboBox){
                JComboBox mySource = (JComboBox) e.getSource();
                fractalGenerator = (FractalGenerator) mySource.getSelectedItem();
                fractalGenerator.getInitialRange(range);
                drawFractal();
            }
            else if (object instanceof JButton){
                JButton button = (JButton) object;
                if(button.getActionCommand().equals("clear")){
                    fractalGenerator.getInitialRange(range);
                    drawFractal();
                }
                else if(button.getActionCommand().equals("save")){
                    JFileChooser saveDialog = new JFileChooser();
                    FileFilter fileFilter = new FileNameExtensionFilter("PNG Images","png");
                    saveDialog.setFileFilter(fileFilter);
                    saveDialog.setAcceptAllFileFilterUsed(false);
                    if(saveDialog.showSaveDialog(button.getParent())!=JFileChooser.APPROVE_OPTION){return;}
                    try{
                        ImageIO.write(imageDisplay.image,"png",
                                saveDialog.getSelectedFile());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(button.getParent(), ex.getMessage(),
                                "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
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
