package com.naren.lendage.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.naren.lendage.services.ServiceUtil;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_BOOT_COMPLETED:
                ServiceUtil.schedule(context);
                break;
        }
    }
}
