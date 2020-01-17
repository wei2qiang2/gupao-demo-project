package com.demo.pattern.factory;

import com.demo.pattern.Color;
import com.demo.pattern.Shape;
import com.demo.pattern.impl.Circle;
import com.demo.pattern.impl.Rectangle;
import com.demo.pattern.impl.Square;

public class ShapeFactory implements AbstractFactory {
    @Override
    public Shape getShapeInstace(Class type) {

        if (type.equals(Rectangle.class)){
            return new Rectangle();
        }else if(Square.class.equals(type)){
            return new Square();
        }else if(type.equals(Circle.class)){
            return new Circle();
        }
        return null;
    }

    @Override
    public Color getColorInstance(Class type) {
        return null;
    }
}
