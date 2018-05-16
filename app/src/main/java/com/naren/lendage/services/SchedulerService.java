package com.naren.lendage.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;

import com.naren.lendage.data.LendageDatabase;
import com.naren.lendage.data.LoanType;
import com.naren.lendage.data.entities.User;

import java.util.List;

public class SchedulerService extends JobService {

    public static final int ID = 0x1000001;

    @Override
    public boolean onStartJob(JobParameters params) {
        new UpdateTask(params).execute();
        return true;
    }

    private class UpdateTask extends AsyncTask<Void, Void, Void>{
        private JobParameters params;

        UpdateTask(JobParameters params){
            this.params = params;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            LendageDatabase database = LendageDatabase.getInstance(getApplicationContext());
            List<User> users = database.getUserDao().users();

            if(users != null){
                for(User user : users){
                    LoanType type = user.type;
                    long lastUpdateTime = user.lastUpdatedTime;
                    int duration = 0;
                    switch (type.period){
                        case WEEK:
                            duration = 7;
                            break;
                        case MONTH:
                            duration = 30;
                            break;
                        case YEAR:
                            duration = 365;
                            break;
                    }
                    long differenceThreshold = (duration*24*60*60*1000);
                    long difference = (System.currentTimeMillis() - lastUpdateTime);
                    if(difference >= differenceThreshold){

                        switch (type.type){
                            case NONE:
                                break;
                            case EMI:
                                //TODO
                                break;
                            case INTEREST:
                                int i = (int)(difference/differenceThreshold);
                                int interest = user.type.interest;
                                float balance = interest*user.amount*i;
                                user.balance += balance;
                                user.lastUpdatedTime = System.currentTimeMillis();
                                break;
                        }
                    }
                }
            }

            User[] array = new User[users.size()];
            users.toArray(array);
            database.getUserDao().update(array);

            jobFinished(params,true);
            return null;
        }
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
