package com.demo.pattern.impl;

import com.demo.pattern.Shape;

public class Rectangle implements Shape {
    @Override
    public void draw() {
        System.out.println("draw method: draw  rectangle");
    }
}
