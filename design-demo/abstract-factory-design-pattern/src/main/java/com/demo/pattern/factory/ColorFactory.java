package com.demo.pattern.factory;

import com.demo.pattern.Color;
import com.demo.pattern.Shape;
import com.demo.pattern.impl.Blue;
import com.demo.pattern.impl.Red;
import com.demo.pattern.impl.Yellow;

public class ColorFactory implements AbstractFactory{


    @Override
    public Shape getShapeInstace(Class type) {
        return null;
    }

    @Override
    public Color getColorInstance(Class type) {

        if (type.equals(Red.class)){
            return new Red();
        }else if (type.equals(Yellow.class)){
            return new Yellow();
        }else if (type.equals(Blue.class)){
            return new Blue();
        }

        return null;
    }
}
