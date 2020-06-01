package com.easysales.letsplay.utils.android;

import android.os.Build;
import android.os.SystemClock;

import com.easysales.letsplay.BuildConfig;

public final class DeviceUtils {
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model;
    }

    public static String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release +")";
    }

    public static String getAppVersion() {
        return String.format("%s (%d)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
    }

    public static long getTimeAfterBooted() {
        return SystemClock.elapsedRealtime();
    }
}
