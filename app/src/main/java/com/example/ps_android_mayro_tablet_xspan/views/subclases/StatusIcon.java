package com.example.ps_android_mayro_tablet_xspan.views.subclases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class StatusIcon extends TextView {
    public StatusIcon(Context context) {
        super(context);
    }

    public StatusIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatusIcon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //Font name should not contain "/".
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/flaticon.ttf");
        setTypeface(tf);
    }
}