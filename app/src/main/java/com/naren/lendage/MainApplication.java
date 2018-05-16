package com.naren.lendage;

import android.app.Application;
import android.util.Log;

import com.naren.lendage.services.ServiceUtil;

public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        boolean scheduled = ServiceUtil.schedule(this);
        Log.v(MainApplication.class.getName(), "Scheduling succeeded - "+scheduled);
    }
}
