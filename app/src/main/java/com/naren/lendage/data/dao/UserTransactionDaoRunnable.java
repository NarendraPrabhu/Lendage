package com.naren.lendage.data.dao;

import com.naren.lendage.data.entities.User;

public abstract class UserTransactionDaoRunnable implements Runnable{

    private final User user;
    public UserTransactionDaoRunnable(User user){
        this.user = user;
    }

    @Override
    public void run() {
        run(user);
    }

    protected abstract void run(User u);
}
