package com.naren.lendage.data.dao;

import com.naren.lendage.data.entities.UnitTransaction;

public abstract class TransactionDaoRunnable implements Runnable {

    private final UnitTransaction t;

    public TransactionDaoRunnable(UnitTransaction t){
        this.t = t;
    }

    @Override
    public void run() {
        run(t);
    }

    protected abstract void run(UnitTransaction t);
}
