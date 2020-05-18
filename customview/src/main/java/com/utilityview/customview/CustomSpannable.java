package com.utilityview.customview;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class CustomSpannable extends ClickableSpan {

    private boolean isUnderline = false;
    private int color;

    public CustomSpannable(boolean isUnderline, int color) {
        this.isUnderline = isUnderline;
        this.color = color;
    }

    @Override
    public void onClick(@NonNull View widget) {

    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(isUnderline);
        ds.setColor(color);
    }
}
