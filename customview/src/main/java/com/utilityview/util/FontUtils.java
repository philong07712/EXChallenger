package com.utilityview.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by Trinh on 03.10.2018.
 */

public class FontUtils {

    public static final String TYPE_FONT_DEFAULT = "fonts/Nunito-Regular.ttf";

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    public static Typeface getTypeface(Context context, String fontname) {
        Typeface typeface = fontCache.get(fontname);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            } catch (Exception e) {
                try {
                    typeface = Typeface.createFromAsset(context.getAssets(), fontname + ".otf");
                } catch (Exception e1) {
                    try {
                        typeface = Typeface.createFromAsset(context.getAssets(), fontname + ".ttf");
                    } catch (Exception e2) {
                        return null;
                    }
                }
            }

            fontCache.put(fontname, typeface);
        }

        return typeface;
    }
}
