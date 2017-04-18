package com.minscapecomputing.dynamiccomponents;

import android.app.Activity;
import android.util.DisplayMetrics;

public class LoginParam {

    public int getScreenHeight(Activity context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            return dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getScreenWidth(Activity context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            return dm.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}



