package com.naren.lendage.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.naren.lendage.data.entities.UnitTransaction;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    void insert(UnitTransaction... users);

    @Update
    void update(UnitTransaction... users);

    @Delete
    void delete(UnitTransaction... users);

    @Query("SELECT * from `transaction` where userId = :userId")
    LiveData<List<UnitTransaction>> getTransactions(long userId);

    @Query("SELECT * from `transaction`")
    List<UnitTransaction> allTransactions();

}
