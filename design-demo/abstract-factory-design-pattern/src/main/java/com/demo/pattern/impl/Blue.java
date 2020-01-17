package com.demo.pattern.impl;

import com.demo.pattern.Color;

public class Blue implements Color {
    @Override
    public void fill() {
        System.out.println("fill method: blue");
    }
}
