package com.example.pacman;

public class Segment {
    point p1,p2;
    public Segment() {

    }
    public Segment(point inp1, point inp2) {
        p1=inp1;
        p2=inp2;
    }

    public Segment(double xp1, double yp1, double xp2, double yp2) {
        p1=new point(xp1, yp1);
        p2=new point(xp2, yp2);
    }
}
