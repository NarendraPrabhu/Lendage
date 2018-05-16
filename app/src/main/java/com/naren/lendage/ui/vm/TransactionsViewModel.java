package com.naren.lendage.ui.vm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.naren.lendage.data.LendageDatabase;
import com.naren.lendage.data.dao.DataTransactionThread;
import com.naren.lendage.data.dao.TransactionDao;
import com.naren.lendage.data.dao.TransactionDaoRunnable;
import com.naren.lendage.data.entities.UnitTransaction;

import java.util.List;

public class TransactionsViewModel extends AndroidViewModel{
    private TransactionDao dao;
    private LiveData<List<UnitTransaction>> transactionsLive;

    public TransactionsViewModel(@NonNull Application application) {
        super(application);
        dao = LendageDatabase.getInstance(application).getTransactionDao();
    }

    public @NonNull LiveData<List<UnitTransaction>> getTransactionsLive(long userId) {
        if(transactionsLive == null){
            transactionsLive = dao.getTransactions(userId);
        }
        return transactionsLive;
    }

    public void delete(UnitTransaction t, DataTransactionThread.OnTransactionCompleteCallback callback){
        Runnable r = new TransactionDaoRunnable(t) {
            @Override
            public void run(UnitTransaction t) {
                dao.delete(t);
            }
        };
        execute(r, callback);
    }

    private void execute(Runnable r, DataTransactionThread.OnTransactionCompleteCallback callback){
        DataTransactionThread dtt = new DataTransactionThread(r, callback);
        dtt.start();
    }
}
