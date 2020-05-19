package com.utilityview.customview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import com.example.customview.R;
import com.utilityview.util.FontUtils;


public class CustomTabLayoutFonts extends TabLayout {
    private Typeface mTypeface;
    private String typeFont = FontUtils.TYPE_FONT_DEFAULT;

    public CustomTabLayoutFonts(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomTabLayoutFonts(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mTypeface = FontUtils.getTypeface(getContext(), typeFont);
        try {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.CustomTextviewFonts, 0, 0);

            typeFont = a.getString(R.styleable.CustomTextviewFonts_font_type);
            if (typeFont == null) {
                typeFont = FontUtils.TYPE_FONT_DEFAULT;
            }
        } catch (Exception ex) {
            typeFont = FontUtils.TYPE_FONT_DEFAULT;
        }
        mTypeface = FontUtils.getTypeface(getContext(), typeFont);
    }

    @Override
    public void addTab(Tab tab) {
        super.addTab(tab);

        ViewGroup mainView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());

        int tabChildCount = tabView.getChildCount();
        for (int i = 0; i < tabChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
            }
        }
    }

}
