package com.example.pacman;

import java.util.ArrayList;

public class Ball {
    point c;
    double r;
    boolean thereisdestination;
    point destination;
    double speedfactor;

    public Ball (point inc, double inr) {
        c=inc;
        r=inr;
        thereisdestination=false;
        destination=new point(1,0);
        speedfactor=5;
    }

    public void move(double delta) {
        if (!thereisdestination) return;
        double d=point.distance(c,destination);
        double traversed=r*speedfactor*delta;
        if (traversed>d) {
            c=destination;
            return;
        }
        point direction=point.unitary(point.sub(destination,c));
        c=point.sum(c,point.mul(traversed,direction));
    }
    public void pushAside(point p) {
        point pc=point.sub(c,p);
        double distance=point.abs(pc);
        if (distance>=r) return;
        if (distance<0.000001) return;
        c=point.sum(p,point.mul(r,point.unitary(pc)));
    }
    public void pushAside(Segment segment) {
        point p1 = segment.p1;
        point p2=segment.p2;
        pushAside(p1);
        pushAside(p2);
        point p1p2=point.sub(p2,p1);
        point p1c=point.sub(c,p1);
        double scalarprod=point.scalarProd(p1p2, p1c);
        if (scalarprod<=0) return;
        if (scalarprod>=point.norm(p1p2)) return;
        point projection=point.sum(p1, point.mul(scalarprod/point.norm(p1p2),p1p2));
        pushAside(projection);
    }

    public void pushAside(SegmentManager segmentManager) {
        ArrayList<Segment> list=segmentManager.list;
        for (int i =0; i< list.size(); i++) {
            pushAside(list.get(i));
        }
    }

    public void move(double delta, SegmentManager segmentManager) {
        double traversed=r*speedfactor*delta;
        int steps=(int) (traversed/(r/2.0))+1;
        for (int i=0; i<steps; i++) {
            move(delta/steps);
            pushAside(segmentManager);
        }
    }
}
