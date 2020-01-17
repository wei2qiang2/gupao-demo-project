package com.demo.pattern.test;

import com.demo.pattern.Shape;
import com.demo.pattern.factory.ShapeFactor;
import com.demo.pattern.impl.Circle;
import com.demo.pattern.impl.Rectangle;
import com.demo.pattern.impl.Square;

public class Main {

    public static void main(String[] args) {

        Shape instance = ShapeFactor.getInstance(Rectangle.class);

        instance.draw();

        Shape instance2 = ShapeFactor.getInstance(Square.class);

        instance2.draw();

        Shape instance3 = ShapeFactor.getInstance(Circle.class);

        instance3.draw();
    }
}
