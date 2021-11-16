package com.example.pacman;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int width, height, size;
    LinearLayout linlay;
    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    CameraManager cameraManager;
    ArrayList<Polygonal> list;
    Ball ball;
    Handler handler;
    long time;
    SegmentManager segmentmanager;

    final int n = 16;

    private point canonic2screen(point p) {
        return new point(
                p.x*size/2+size/2,
                size/2-p.y*size/2
        );
    }
    private point screen2canonic (point p) {
        return new point(
                (p.x-size/2)/size*2,
                -(p.y-size/2)/size*2
        );
    }

    private point world2screen (point p) {
        return canonic2screen(cameraManager.world2camera(p));
    }

    private point screen2world (point p) {
        return cameraManager.camera2world(screen2canonic(p));
    }

    private double world2screenfactor() {
        return point.distance(world2screen(new point(0,0)), world2screen(new point(1,0)));
    }

    private void drawAll() {
        canvas.drawColor(Color.RED);
        point c=world2screen(ball.c);
        double r=ball.r*world2screenfactor();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle((int)c.x, (int)c.y, (int)r, paint);
        if (ball.thereisdestination) {
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(6);
            point direction=point.unitary(point.sub(ball.destination,ball.c));
            point position=point.sum(ball.c,point.mul(ball.r,direction));
            point d=world2screen(position);
            //point d = world2screen(ball.destination);
            //point direction=new point(ball.destination.x, -1*ball.destination.y );
            //point d=(point.sum(c, point.mul(r,direction)));
            ////dir=c+r*direction->vector unitari
            canvas.drawLine((float) c.x, (float) c.y, (float) d.x, (float) d.y, paint);
        }
        ArrayList<Segment> list = segmentmanager.list;
        for (int i=0; i<list.size(); i++) {
            drawSegment(list.get(i));
        }
        imageView.invalidate();
    }

    private void drawSegment(Segment segment) {
        point p1=world2screen(segment.p1);
        point p2=world2screen(segment.p2);
        Path path=new Path();
        path.moveTo((float) p1.x, (float) p1.y);
        path.lineTo((float) p2.x, (float) p2.y);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawPath(path, paint);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        linlay=new LinearLayout(this );
        linlay.setOrientation(LinearLayout.VERTICAL);
        linlay.setGravity(Gravity.CENTER_HORIZONTAL);
        imageView=new ImageView(this);
        linlay.addView(imageView);

        Point mypoint = new Point();
        getWindowManager().getDefaultDisplay().getSize(mypoint);
        width=mypoint.x;
        height=mypoint.y;
        size=(int)(width*0.9);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(size,size));
        bitmap=Bitmap.createBitmap(size,size,Bitmap.Config.ARGB_8888);
        canvas=new Canvas();
        imageView.setImageBitmap(bitmap);
        canvas.setBitmap(bitmap);
        canvas.drawColor(Color.RED);

        paint=new Paint();
        cameraManager= new CameraManager();
        cameraManager.center=new point(n/2.0, n/2.0);
        cameraManager.right=new point(n, n/2.0);
        //ball=new Ball();
        ball=new Ball(new point(0.5, 0.5), 0.35);
        segmentmanager=new SegmentManager(n);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("TOUCH",event.toString());

                if(event.getPointerCount()==1 && (event.getAction()==MotionEvent.ACTION_MOVE || event.getAction()==MotionEvent.ACTION_DOWN)) {
                    point finger= new point(event.getX(),event.getY());
                    point worldfinger=screen2world(finger);
                    if (event.getAction()==MotionEvent.ACTION_DOWN) {
                        if (point.distance(worldfinger, ball.c)<3*ball.r) {
                            ball.thereisdestination=true;
                            ball.destination=worldfinger;
                        } else {
                            ball.thereisdestination=false;
                            cameraManager.touch(screen2canonic(finger));
                        }
                        return true;
                    }

                    if (ball.thereisdestination) {
                        ball.thereisdestination=true;
                        ball.destination=worldfinger;
                    }
                    else {
                        cameraManager.touch(screen2canonic(finger));
                    }
                    return true;
                }
                ball.thereisdestination=false;

                if((event.getPointerCount()!=1 && event.getPointerCount()!=2) || event.getAction()!=MotionEvent.ACTION_MOVE ){
                    cameraManager.touch();
                }
                else if (event.getPointerCount()==1) {
                    point finger=new point(event.getX(), event.getY());
                    cameraManager.touch(screen2canonic(finger));
                    //cameraManager.touch(screen2canonic(finger), new point(0,0));
                    //drawAll();
                    //drawLineCoordinate();
                    //imageView.invalidate();
                }
                else {
                    point finger1= new point(event.getX(0), event.getY(0));
                    point finger2= new point(event.getX(1), event.getY(1));
                    cameraManager.touch(screen2canonic(finger1), screen2canonic(finger2));
                    //drawAll();
                    //drawLineCoordinate();
                    //imageView.invalidate();
                }
                return true;
            }
        });
        drawAll();
        handler=new Handler();
        time=System.currentTimeMillis();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long nexttime=System.currentTimeMillis();
                double delta=(nexttime-time)/1000.0;
                time=nexttime;
                ball.move(delta, segmentmanager);
                drawAll();
                handler.postDelayed(this, 50);
            }
        },50);

        setContentView(linlay);
    }
}