package com.company;

import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;
    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x=-2;
        range.y=-2.5;
        range.height=4;
        range.width=4;
    }

    @Override
    public int numIterations(double x, double y) {
        double x1 = 0;
        double y1 = 0;
        for(int i = 0; i<MAX_ITERATIONS;i++){
            double xTemp = x1 * x1 - y1 * y1 + x;
            double yTemp = Math.abs(2 * x1 * y1) + y;
            x1 = xTemp;
            y1 = yTemp;
            if(xTemp*xTemp+yTemp*yTemp>4){return i;}
        }
        return -1;
    }
    public String toString(){
        return "BurningShip";
    }
}
