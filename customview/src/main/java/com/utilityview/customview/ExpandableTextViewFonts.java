package com.utilityview.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.customview.R;


/**
 * Created by Trinh on 03.10.2018.
 */
public class ExpandableTextViewFonts extends CustomTextviewFonts {

    private static final String ELLIPSIS = "... ";
    private boolean isExpand = false;
    private static final int DEFAULT_TRIM_LENGTH = 200;
    private int trimLength;
    private CharSequence originalText;
    private CharSequence trimmedText;
    private TextView.BufferType bufferType;
    private String expandText, collapseText;
    private int appendTextColor;

    public ExpandableTextViewFonts(Context context) {
        super(context);
    }

    public ExpandableTextViewFonts(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandableTextViewFonts(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextViewFonts);
        this.trimLength = typedArray.getInt(R.styleable.ExpandableTextViewFonts_trimLength, DEFAULT_TRIM_LENGTH);
        this.expandText = typedArray.getString(R.styleable.ExpandableTextViewFonts_appendText);
        if(this.expandText==null){
            expandText = "";
        }
        this.appendTextColor = typedArray.getColor(R.styleable.ExpandableTextViewFonts_appendTextColor, Color.parseColor("#343434"));
        typedArray.recycle();

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                setText();
            }
        });
    }

    private CharSequence getTrimmedText() {
        if (originalText != null && originalText.length() > trimLength) {
            SpannableStringBuilder spannableStringBuilder
                    = new SpannableStringBuilder(originalText, 0, trimLength + 1)
                    .append(ELLIPSIS)
                    .append(expandText);
            String fullText = spannableStringBuilder.toString();
            spannableStringBuilder.setSpan(new CustomSpannable(false, appendTextColor),
                    fullText.lastIndexOf(expandText),
                    fullText.lastIndexOf(expandText) + expandText.length(), 0
            );
            return spannableStringBuilder;
        } else {
            return originalText;
        }
    }

    public CharSequence getOriginalText() {
        return originalText;
    }

    public void setTrimLength(int trimLength) {
        this.trimLength = trimLength;
        trimmedText = getTrimmedText();
        setText();
    }

    private void setText() {
        super.setText(getDisplayableText(), bufferType);
    }

    private CharSequence getDisplayableText() {
        return isExpand ? originalText : trimmedText;
    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        originalText = text;
        trimmedText = getTrimmedText();
        bufferType = type;
        setText();
    }

    public int getTrimLength() {
        return trimLength;
    }
}
