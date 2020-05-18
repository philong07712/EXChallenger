package com.utilityview.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.customview.R;

public class CornerImageView extends ImageView {
    private static final String TAG = "CornerImageView";
    private float topLeftRadius;
    float topRightRadius;
    float bottomRightRadius;
    float bottomLeftRadius;

    public CornerImageView(Context context) {
        super(context);
    }

    public CornerImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CornerImageView, 0, 0);
        topLeftRadius = typedArray.getDimension(R.styleable.CornerImageView_topLeftRadius, 0);
    }

    public CornerImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override
    protected void onDraw(Canvas canvas) {

        try {
            Drawable drawable = getDrawable();

            if (drawable == null) {
                return;
            }

            if (getWidth() == 0 || getHeight() == 0) {
                return;
            }

            Bitmap bitmap;
            int w = getWidth(), h = getHeight();

            if (w <= 0 || h <= 0) {
                return;
            }

            if (drawable instanceof BitmapDrawable) {
                Bitmap b = ((BitmapDrawable) drawable).getBitmap();
                bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            } else if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(bitmap);
                c.drawColor(((ColorDrawable) drawable).getColor());

            } else {
                return;
            }


            Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap, Math.min(w, h), 0, 0, 0);
            canvas.drawBitmap(roundBitmap, 0, 0, null);
        } catch (Exception e) {
            Log.e(TAG, "onDraw Exception", e);
        }

    }

    private Bitmap getRoundedCroppedBitmap(Bitmap bitmap, float topLeftRadius,
                                           float topRightRadius, float bottomRightRadius, float bottomLeftRadius) {
        Bitmap finalBitmap;

        if (bitmap.getWidth() != topLeftRadius || bitmap.getHeight() != topLeftRadius) {
            finalBitmap = Bitmap.createScaledBitmap(bitmap, (int) topLeftRadius, 0, false);
        } else {
            finalBitmap = bitmap;
        }
        finalBitmap = bitmap;

        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }
}
