package com.lfkj.util.fx;

import com.lfkj.util.Pair;


public class Shape {
    private static Shape shape = new Shape();

    private static Shape getInstance() {
        return shape;
    }

    public static Rectangle rectangle(Pair<Double, Double> leftTop, double length, double height) {
        return getInstance().new Rectangle(leftTop, length, height);
    }

    public static Rectangle rectangle(double startX, double startY, double endX, double endY) {
        return getInstance().new Rectangle(startX, startY, endX, endY);
    }

    public class Rectangle {
        double startX, startY, endX, endY;

        public boolean isOutside(double x, double y) {
            return !(x > startX && x < endX && y > startY && y < endY);
        }

        Rectangle(double startX, double startY, double endX, double endY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }

        Rectangle(Pair<Double, Double> leftTop, double length, double height) {
            this.startX = leftTop._1;
            this.startY = leftTop._2;
            this.endX = leftTop._1 + length;
            this.endY = leftTop._2 + height;
        }
    }
}
