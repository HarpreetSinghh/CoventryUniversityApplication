package com.conventry.university;


import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.conventry.university.utils.TypefaceUtil;

public class MyApplication extends Application {


    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "font/calibri.ttf");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public static Context getAppContext(){
        return mContext;
    }
}
