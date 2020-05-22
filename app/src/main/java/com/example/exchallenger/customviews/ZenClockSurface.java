package com.example.exchallenger.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.Surface.OutOfResourcesException;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.exchallenger.R;

public class ZenClockSurface extends SurfaceView implements
        SurfaceHolder.Callback {

    private DrawClock drawClock;

    public ZenClockSurface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
    }

    public ZenClockSurface(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public ZenClockSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawClock = new DrawClock(getHolder(), getResources());
        drawClock.setRunning(true);
        drawClock.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawClock.setRunning(false);
        while (retry) {
            try {
                drawClock.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }

    class DrawClock extends Thread {
        private boolean runFlag = false;
        private SurfaceHolder surfaceHolder;
        private Bitmap picture;
        private Matrix matrix;
        private Paint painter;

        public DrawClock(SurfaceHolder surfaceHolder, Resources resources) {
            this.surfaceHolder = surfaceHolder;

            picture = BitmapFactory.decodeResource(resources,
                    R.drawable.ic_add_white);
            matrix = new Matrix();
            this.painter = new Paint();
            this.painter.setStyle(Paint.Style.FILL);
            this.painter.setAntiAlias(true);
            this.painter.setFilterBitmap(true);
        }

        public void setRunning(boolean run) {
            runFlag = run;
        }

        @Override
        public void run() {
//            Canvas canvas;
//            while (runFlag) {
//
//
////                matrix.preRotate(1.0f, picture.getWidth() / 2,
////                        picture.getHeight() / 2);
////                canvas = null;
//                try {
//                    canvas = surfaceHolder.lockCanvas(null);
//                    synchronized (surfaceHolder) {
//                        canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
//                        canvas.drawBitmap(picture, matrix, this.painter);
//                    }
//                } catch (IllegalArgumentException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }  finally {
//                    if (canvas != null) {
//                        surfaceHolder.unlockCanvasAndPost(canvas);
//                    }
//                }
//            }
        }
    }
}