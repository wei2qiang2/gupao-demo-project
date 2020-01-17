package com.demo.pattern.factory;

import com.demo.pattern.Color;
import com.demo.pattern.Shape;

public interface AbstractFactory {

    Shape getShapeInstace(Class type);

    Color getColorInstance(Class type);
}
