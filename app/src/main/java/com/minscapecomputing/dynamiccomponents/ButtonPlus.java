package com.minscapecomputing.dynamiccomponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Arun on 12/9/2016.
 */
public class ButtonPlus extends android.support.v7.widget.AppCompatButton {
    private static final String TAG = "Button";

    public ButtonPlus(Context context) {
        super(context);
    }

    public ButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public ButtonPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
        String fontPath = a.getString(R.styleable.TextViewPlus_customFont);
        setCustomFont(ctx, fontPath);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String fontPath) {
        Typeface typeface;
        try {
            typeface = Typeface.createFromFile(fontPath);
        } catch (Exception e) {
            Log.e(TAG, "Unable to load typeface: " + e.getMessage());
            return false;
        }

        setTypeface(typeface);
        return true;
    }
}