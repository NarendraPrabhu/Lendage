package com.naren.lendage.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.naren.lendage.data.dao.TransactionDao;
import com.naren.lendage.data.dao.UserDao;
import com.naren.lendage.data.entities.UnitTransaction;
import com.naren.lendage.data.entities.User;

@Database(entities = {User.class, UnitTransaction.class}, version = 1, exportSchema = false)
@TypeConverters(User.class)
public abstract class LendageDatabase extends RoomDatabase{

    private static final String NAME = "lendage.db";
    private static LendageDatabase INSTANCE;

    public static LendageDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (LendageDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, LendageDatabase.class, NAME).fallbackToDestructiveMigration().addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                        }

                    }).build();
                }
            }
        }
        return INSTANCE;
    }


    public abstract UserDao getUserDao();

    public abstract TransactionDao getTransactionDao();
}
