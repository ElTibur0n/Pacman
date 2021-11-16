package com.example.pacman;

import android.graphics.Paint;

import java.util.Random;

public class Polygonal {
    point[] list;
    int red, green, blue;

    Polygonal() {
        int n=3+(int)Math.floor(Math.random()*8);
        list=new point[n];
        point center=new point(Math.random()*2-1, Math.random()*2-1);
        double r=0.1+Math.random()*0.2;
        Random rnd = new Random();
        red=rnd.nextInt(256);
        green=rnd.nextInt(256);
        blue=rnd.nextInt(256);
        for(int i=0; i<n; i++) {
            list[i]=point.sum(center,point.polar(r, 2*Math.PI*i/n));
        }
    }
}
