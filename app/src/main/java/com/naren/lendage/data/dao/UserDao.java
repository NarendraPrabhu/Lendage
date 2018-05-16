package com.naren.lendage.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.naren.lendage.data.entities.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);


    @Query("SELECT * from user")
    LiveData<List<User>> getUsers();

    @Query("SELECT * from user")
    List<User> users();

    @Query("SELECT * from user where name = :name")
    List<User> findUsersByName(String name);
}
