package com.demo.pattern.factory;

import com.demo.pattern.Shape;
import com.demo.pattern.impl.Circle;
import com.demo.pattern.impl.Rectangle;
import com.demo.pattern.impl.Square;

import java.util.ArrayList;
import java.util.List;

public class ShapeFactor {


    public static Shape getInstance(Class type){
        Shape shape = null;

        if (type.equals(Rectangle.class)){
            shape = new Rectangle();
        }else if (type.equals(Circle.class)){
            shape = new Circle();
        }else if (type.equals(Square.class)){
            shape = new Square();
        }

        return shape;
    }

    public static List<Class> getAllClasses(){
        List<Class>  calssArr = new ArrayList<Class>();

        return calssArr;
    }
}
