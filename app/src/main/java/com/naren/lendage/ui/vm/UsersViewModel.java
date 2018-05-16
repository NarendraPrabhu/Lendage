package com.naren.lendage.ui.vm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.naren.lendage.data.LendageDatabase;
import com.naren.lendage.data.dao.DataTransactionThread;
import com.naren.lendage.data.dao.UserDao;
import com.naren.lendage.data.dao.UserTransactionDaoRunnable;
import com.naren.lendage.data.entities.User;

import java.util.List;

public class UsersViewModel extends AndroidViewModel{

    private UserDao dao;
    private LiveData<List<User>> userLive;

    public UsersViewModel(@NonNull Application application) {
        super(application);
        dao = LendageDatabase.getInstance(application).getUserDao();
        userLive = dao.getUsers();
    }

    public @NonNull LiveData<List<User>> getUserLive() {
        return userLive;
    }

    public void delete(User u, DataTransactionThread.OnTransactionCompleteCallback callback){
        Runnable r = new UserTransactionDaoRunnable(u) {
            @Override
            protected void run(User u) {
                dao.delete(u);
            }
        };
        execute(r, callback);
    }

    private void execute(Runnable r, DataTransactionThread.OnTransactionCompleteCallback callback){
        DataTransactionThread dtt = new DataTransactionThread(r, callback);
        dtt.start();
    }

}
