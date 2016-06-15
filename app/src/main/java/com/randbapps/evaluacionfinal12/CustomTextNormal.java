package com.randbapps.evaluacionfinal12;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by MAO on 14/05/2016.
 */
public class CustomTextNormal extends TextView {
    public CustomTextNormal(Context context) {
        super(context);
    }

    public CustomTextNormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomTextNormal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Cronus-Round.otf");
        setTypeface(font, Typeface.NORMAL);
    }
}
