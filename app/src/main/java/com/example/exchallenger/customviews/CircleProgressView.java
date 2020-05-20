package com.example.exchallenger.customviews;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CircleProgressView extends View implements View.OnTouchListener {
    private static final String TAG = CircleProgressView.class.getSimpleName();
    private Context context;
    private Paint mainPaint = new Paint();
    private Paint backgroundPaint = new Paint();
    private Paint pointPaint = new Paint();
    private RectF rectangle;
    private RectF rootRectangle;
    private RectF runningRectangle;
    private Float margin;
    //    private Float angle = 0f;
    private float strokeWidth = 0f;
    private int mainColor, backgroundColor;
    private int max = 3600;
    private int min = 0;
    private int progress = 0;
    private OnProgressChangeListener onProgressChangeListener;
    private boolean isLock = false;

    public CircleProgressView(Context context) {
        super(context);
        init(context);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        mainColor = Color.parseColor("#1b76ff");
        backgroundColor = Color.parseColor("#acd5f7");
        mainPaint.setAntiAlias(true);
        mainPaint.setColor(mainColor);
        mainPaint.setStyle(Paint.Style.STROKE);

        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);

        pointPaint.setAntiAlias(true);
        pointPaint.setColor(Color.WHITE);
        pointPaint.setStyle(Paint.Style.FILL);

        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float angle = (float) progress / (float) (max - min) * 360f;
        if (rectangle == null) {
            rectangle = new RectF(0f + margin, 0f + margin, getWidth() - margin, getHeight() - margin);
            rootRectangle = new RectF(getWidth() / 2f - margin, 0f, getWidth() / 2f + margin, strokeWidth);
            setRunningPosition(angle);
        }
        canvas.drawArc(rectangle, -90f, angle, false, mainPaint);
        canvas.drawArc(rectangle, -90f + angle, 360 - angle, false, backgroundPaint);
        canvas.drawOval(rootRectangle, pointPaint);
        canvas.drawOval(runningRectangle, pointPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthPixels = View.MeasureSpec.getSize(widthMeasureSpec);
        strokeWidth = widthPixels / 10f;
        mainPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setStrokeWidth(strokeWidth);
        margin = strokeWidth / 2;
    }

    private void setAngle(Float angle) {
        if (angle < 0)
            angle = 0f;
        else if (angle > 360)
            angle = 360f;
        progress = (int) (angle / 360f * (max - min) + min);
        setRunningPosition(angle);
        invalidate();
    }

    private void setRunningPosition(Float angle) {
        double angleRadian = angle / 180 * Math.PI;
        float left = (float) (getWidth() / 2f + (getWidth() / 2f - strokeWidth / 2) * Math.sin(angleRadian)) - strokeWidth / 2;
        float top = (float) (getWidth() / 2f - (getWidth() / 2f - strokeWidth / 2) * Math.cos(angleRadian)) - strokeWidth / 2;
        runningRectangle = new RectF(left, top, left + strokeWidth, top + strokeWidth);
        if (onProgressChangeListener != null && !isLock) {
            onProgressChangeListener.onChanged(angle, progress);
        }
    }


    public void setMainColor(int mainColor) {
        this.mainColor = mainColor;
        mainPaint.setColor(mainColor);
        invalidate();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
    }

    float firstX, firstY;
//    float previusAngle;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
//                previusAngle = (float) progress / (float) (max - min);;
                firstX = event.getX();
                firstY = event.getY();
                return isTouchRunningCircle(event);
            }
            case MotionEvent.ACTION_MOVE: {
                setAngle(getAngleFromEvent(event));
                return true;
            }
            case MotionEvent.ACTION_UP: {

                return false;
            }

        }
        return false;
    }

    private boolean isTouchRunningCircle(MotionEvent event) {
        return event.getX() > runningRectangle.left - 20 && event.getX() < runningRectangle.right + 20
                && event.getY() > runningRectangle.top - 20 && event.getY() < runningRectangle.bottom + 20;
    }

    private float getAngleFromEvent(MotionEvent event) {
        double radius = getWidth() / 2f;
        if (event.getX() > radius) {
            if (event.getY() < radius) {// goc phan tu thu 1
                double k = (event.getY() - radius) / (radius - event.getX());
                double angle = 90f - Math.atan(k) * 180 / Math.PI;
                return (float) angle;
            } else if (event.getY() > radius) { // goc phan tu thu 2
                double k = (event.getY() - radius) / (event.getX() - radius);
                double angle = 90f + Math.atan(k) * 180 / Math.PI;
                return (float) angle;
            } else if (event.getY() == radius) {
                return 90f;
            }
        } else if (event.getX() < radius) {
            if (event.getY() < radius) {// goc phan tu thu 4
                double k = (event.getY() - radius) / (event.getX() - radius);
                double angle = 270f + Math.atan(k) * 180 / Math.PI;
                return (float) angle;
            } else if (event.getY() > radius) { // goc phan tu thu 3
                double k = (event.getY() - radius) / (radius - event.getX());
                double angle = 270f - Math.atan(k) * 180 / Math.PI;
                return (float) angle;
            } else if (event.getY() == radius) {
                return 270f;
            }
        }
        return 180f;
    }

    public static float convertDpToPixel(Context context, float dp) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(Context context, float px) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setProgress(int progress) {
        setAngle((float) progress / (float) max * 360f);
    }

    public void setProgressWithAnimate(int newProgress) {
        ValueAnimator animator = ValueAnimator.ofInt(progress, newProgress);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                setProgress(animatedValue);
            }
        });
        animator.setDuration(300);
        animator.start();
    }

    public interface OnProgressChangeListener {
        void onChanged(float angle, float value);
    }
}