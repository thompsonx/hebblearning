/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hebblearning.gui;

import hebblearning.perceptron.InputDescription;
import hebblearning.perceptron.Perceptron;
import hebblearning.perceptron.PerceptronTask;
import hebblearning.perceptron.TestSetElement;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author tom
 */
public class Plot extends JComponent {

    private PerceptronTask ptask = null;
    
    class ScreenData
    {
        public double x_min;
        public double x_max;
        public double y_min;
        public double y_max;
        public double ratio;
        public double rect_w;
        public double rect_h;
        public double rect_x;
        public double rect_y;
        public double x_scale;
        public double y_scale;
    }
    
    public void setPerceptronTask(PerceptronTask pt)
    {
        this.ptask = pt;
        this.repaint();
    }
    
    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        if (this.ptask == null)
            return;
        
        Graphics2D g2 = (Graphics2D)grphcs;
        
        Perceptron ptron = this.ptask.getPerceptron();
        InputDescription[] idesc = ptron.getInputDescriptions();
        if (idesc.length != 2)
            return;
        
        double x_min = idesc[0].getMinimum();
        double x_max = idesc[0].getMaximum();
        double y_min = idesc[1].getMinimum();
        double y_max = idesc[1].getMaximum();
        double x_len = Math.abs( x_max - x_min );
        double y_len = Math.abs( y_max - y_min );
        
        double width = this.getWidth();
        double height = this.getHeight();
        
        double ratio = 5;
        double rect_w = width / ratio;
        double rect_h = height / ratio;
        double rect_x = (width / 2) - (rect_w / 2);
        double rect_y = (height / 2) - (rect_h / 2);
        
//        double x_offset = Math.abs( x_max - x_min ) / 2;
//        double y_offset = Math.abs( y_max - y_min ) / 2;
//        double x_start = (width / 2) - x_offset;
//        double x_end = (width / 2) + x_offset;
//        double y_start = (height / 2) - y_offset;
//        double y_end = (height / 2) + y_offset;
        
        g2.draw(new Rectangle2D.Double(rect_x, rect_y, rect_w, rect_h));
        g2.draw(new Line2D.Double(0, rect_y+rect_h, width, rect_y+rect_h));
        g2.draw(new Line2D.Double(rect_x, 0, rect_x, height));
        int ts = g2.getFont().getSize();
        g2.drawString(Double.toString(x_min), (float)rect_x, (float)(rect_y+rect_h+ts));
        g2.drawString(Double.toString(x_max), (float)(rect_x+rect_w), (float)(rect_y+rect_h+ts));
        String y_min_str = Double.toString(y_min);
        String y_max_str = Double.toString(y_max);
        g2.drawString(y_min_str, (float)(rect_x-(ts/1.7)*y_min_str.length()), (float)(rect_y+rect_h));
        g2.drawString(y_max_str, (float)(rect_x-(ts/1.7)*y_max_str.length()), (float)(rect_y));
        
        List<TestSetElement> points = new ArrayList<>();
        points.addAll(this.ptask.getTestSet());
        points.addAll(this.ptask.getTrainSet());
        
        double x_scale = rect_w / x_len;
        double y_scale = rect_h / y_len;
        
        ScreenData sd = new ScreenData();
        sd.x_max = x_max;
        sd.x_min = x_min;
        sd.y_min = y_min;
        sd.y_max = y_max;
        sd.ratio = ratio;
        sd.rect_w = rect_w;
        sd.rect_h = rect_h;
        sd.rect_x = rect_x;
        sd.rect_y = rect_y;
        sd.x_scale = x_scale;
        sd.y_scale = y_scale;
        
        g2.setColor(Color.BLUE);
        for (TestSetElement p : points)
        {
            double x = (p.getInputs()[0] - x_min)*x_scale + rect_x;
            double y = (rect_y + rect_h) - (p.getInputs()[1] - y_min)*y_scale;
            
//            System.out.printf("Point: %f %f // %f %f\n", p.getInputs()[0],
//                    p.getInputs()[1], x, y);
            
            g2.draw(new Ellipse2D.Double(x-ratio/2, y+ratio/2, ratio, ratio));
        }
        
        g2.setColor(Color.ORANGE);
        
        List<Point2D> borders = new ArrayList<>();
        double y_0 = this.mapToPx(new Point2D.Double(0,
                f_y(this.mapFromPx(new Point2D.Double(0, 0), sd).getX())),
                sd).getY();
        double y_m = this.mapToPx(new Point2D.Double(0,
                f_y(this.mapFromPx(new Point2D.Double(width, 0), sd).getX())),
                sd).getY();;
        double x_0 = this.mapToPx(new Point2D.Double(
                f_x(this.mapFromPx(new Point2D.Double(0, 0), sd).getY()), 0),
                sd).getX();
        double x_m = this.mapToPx(new Point2D.Double(
                f_x(this.mapFromPx(new Point2D.Double(0, height), sd).getY()), 0),
                sd).getX();
        
//        System.out.printf("%f %f %f %f\n", x_0, x_m, y_0, y_m);
        
        if (!(y_0 > height || y_0 < 0))
            borders.add(new Point2D.Double(0, y_0));
        if (!(y_m > height || y_m < 0))
            borders.add(new Point2D.Double((int)width, (int)y_m));
        if (!(x_0 > width || x_0 < 0))
            borders.add(new Point2D.Double((int)x_0, 0));
        if (!(x_m > width || x_m < 0))
            borders.add(new Point2D.Double((int)x_m, (int)height));
        
        if (borders.size() == 2)
        {
//            System.out.printf("P1 x %f y %f, P2 x %f y %f\n", borders.get(0).getX(),
//                    borders.get(0).getY(),
//                    borders.get(1).getX(),
//                    borders.get(1).getX());
            g2.draw(new Line2D.Double(borders.get(0), borders.get(1)));
        }
    }
    
    private Point2D mapFromPx(Point2D point, ScreenData sd)
    {   
        double x = (point.getX() - sd.rect_x)/sd.x_scale + sd.x_min;
        double y = ((sd.rect_y + sd.rect_h) - point.getY())/sd.y_scale + sd.y_min;
        
        return new Point2D.Double(x, y);
    }
    
    private Point2D mapToPx(Point2D point, ScreenData sd)
    {
        double x = (point.getX() - sd.x_min)*sd.x_scale + sd.rect_x;
        double y = (sd.rect_y + sd.rect_h) - (point.getY() - sd.y_min)*sd.y_scale;
        
        return new Point2D.Double(x, y);
    }
    
    private double f_y(double x)
    {
        Perceptron ptron = this.ptask.getPerceptron();
        double w0 = ptron.getWeights()[0];
        double w1 = ptron.getWeights()[1];
        double w2 = ptron.getWeights()[2];
                
        return -(w1 / w2) * x - (w0 / w2);
    }
    
    private double f_x(double x)
    {
        Perceptron ptron = this.ptask.getPerceptron();
        double w0 = ptron.getWeights()[0];
        double w1 = ptron.getWeights()[1];
        double w2 = ptron.getWeights()[2];
                
        return -(w2 / w1) * x - (w0 / w1);
    }
    
}
