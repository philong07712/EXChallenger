package com.utilityview.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.example.customview.R;
import com.utilityview.util.FontUtils;


/**
 * Created by Trinh on 03.10.2018.
 */
public class CustomButtonFonts extends AppCompatButton {

    private int currentColor = Color.BLACK;
    private String typeFont = FontUtils.TYPE_FONT_DEFAULT;

    public CustomButtonFonts(Context context) {
        super(context);
    }

    public CustomButtonFonts(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomButtonFonts(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        currentColor = getCurrentTextColor();
        try {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.CustomTextviewFonts, 0, 0);

            typeFont = a.getString(R.styleable.CustomTextviewFonts_font_type);
            if (typeFont == null) {
                typeFont = FontUtils.TYPE_FONT_DEFAULT;
            }
            setTypeface(FontUtils.getTypeface(context, typeFont));
            a.recycle();
        } catch (Exception ex) {
            try {
                setTypeface(Typeface.createFromAsset(context.getAssets(), FontUtils.TYPE_FONT_DEFAULT));
            } catch (Exception ex1) {
                setTypeface(Typeface.DEFAULT);
            }
        }
    }

    public void setTypeFont(Context context, String typeFont) {
        this.typeFont = typeFont;
        setTypeface(FontUtils.getTypeface(context, typeFont));
        invalidate();
    }
}
