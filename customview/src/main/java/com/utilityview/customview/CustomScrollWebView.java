package com.utilityview.customview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

public class CustomScrollWebView extends WebView {

    private OnScrollChangedCallback onScrollChangedCallback;

    public CustomScrollWebView(Context context) {
        super(context);
    }

    public CustomScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomScrollWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangedCallback != null) {
            onScrollChangedCallback.onScroll(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedCallback(OnScrollChangedCallback onScrollChangedCallback) {
        this.onScrollChangedCallback = onScrollChangedCallback;
    }

    public interface OnScrollChangedCallback {
        void onScroll(int currentHorizontal, int currentVertical, int oldHorizontal, int oldVertical);
    }
}
