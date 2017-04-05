package com.minscapecomputing.dynamiccomponents;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by Arun on 03-08-2016.
 */
public class LoginParam {

    public JSONObject jsonCreate(Activity context, String userName, String passWordPlain, String devId) {

        JSONObject jsonObject = new JSONObject();
        JSONObject device_info = new JSONObject();

        String deviceModel = "";
        String deviceName = "";
        String androidVersion = "";
        String screenSize = "";

        try {
            deviceModel = android.os.Build.MODEL;
            deviceName = android.os.Build.MANUFACTURER;
            androidVersion = android.os.Build.VERSION.RELEASE;
            screenSize = getScreenInches(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            device_info.put("device_version", androidVersion);
            device_info.put("device_id", devId);
            device_info.put("device_model", deviceModel);
            device_info.put("device_name", deviceName);
            device_info.put("last_refresh_dateTime", "");
            device_info.put("last_refresh_mode", "");
            device_info.put("user_active", "");
            device_info.put("user_created", userName);
            device_info.put("user_id", userName);
//            device_info.put("user_system_date", DateUtil.getNow());
            device_info.put("screen_inch", screenSize);
            device_info.put("data_refresh_cycle_time", "");

            jsonObject.put("user_name", userName);
            jsonObject.put("password", passWordPlain);
            jsonObject.put("device_id", devId);
            jsonObject.put("device_info", device_info);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public String getScreenInches(Activity context) {

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double) width / (double) dens;
        double hi = (double) height / (double) dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        return width + "*" + height;
    }

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
            return  dm.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getStatusBarHeight(Activity context) {

        int result = 0;
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}



