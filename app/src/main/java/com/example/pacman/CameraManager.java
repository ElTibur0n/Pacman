package com.example.pacman;

public class CameraManager {
    point center, right;
    int numfingers;
    point worldfinger, worldfinger1, worldfinger2;
    CameraManager() {
        center=new point(0,0);
        right=new point(1,0);
        numfingers=0;
    }
    public point camera2world(point p) {
        return point.reference2world(center, right, p);
    }
    public point world2camera(point p) {
        return point.world2reference(center,right,p);
    }
    public void touch() {
        numfingers=0;
    }
    public void touch(point camerafinger) {
        if (numfingers!=1) {
            numfingers=1;
            worldfinger=camera2world(camerafinger);
            return;
        }
        point worldfingerbis=camera2world(camerafinger);
        point move=point.sub(worldfinger, worldfingerbis);
        center=point.sum(center, move);
        right=point.sum(right, move);
    }
    public void touch(point camerafinger1, point camerafinger2) {
        if (numfingers != 2) {
            numfingers=2;
            worldfinger1=camera2world(camerafinger1);
            worldfinger2=camera2world(camerafinger2);
            return;
        }
        point centerbis=point.world2reference(camerafinger1, camerafinger2, new point(0,0));
        point rightbis=point.world2reference(camerafinger1, camerafinger2, new point(1,0));
        center=point.reference2world(worldfinger1,worldfinger2, centerbis);
        right=point.reference2world(worldfinger1, worldfinger2, rightbis);
    }
}


