package com.demo.pattern.impl;

import com.demo.pattern.Shape;

public class Circle implements Shape {
    @Override
    public void draw() {
        System.out.println("draw method: draw circle");
    }
}
