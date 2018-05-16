package com.naren.lendage.services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

public class ServiceUtil {

    public static boolean schedule(Context context){
        JobInfo info = new JobInfo
                .Builder(SchedulerService.ID, new ComponentName(context, SchedulerService.class))
                .setPeriodic(24*60*60*1000)
                .build();
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        return scheduler.schedule(info) > 0;
    }
}
