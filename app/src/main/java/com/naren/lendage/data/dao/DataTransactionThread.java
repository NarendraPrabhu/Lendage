package com.naren.lendage.data.dao;

import java.lang.ref.WeakReference;

public class DataTransactionThread extends Thread {

    public interface OnTransactionCompleteCallback{
        void onTransactionComplete();
    }

    private final WeakReference<OnTransactionCompleteCallback> callback;
    private final Runnable job;

    public DataTransactionThread(Runnable job, OnTransactionCompleteCallback callback){
        this.callback = new WeakReference<OnTransactionCompleteCallback>(callback);
        this.job = job;
    }

    @Override
    public void run() {
        job.run();
        OnTransactionCompleteCallback c = callback.get();
        if(c != null){
            c.onTransactionComplete();
        }
    }
}
