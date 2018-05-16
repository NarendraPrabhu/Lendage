package com.naren.lendage.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.naren.lendage.utils.Utils;

import java.util.Date;

@Entity(tableName = "transaction",
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = ForeignKey.CASCADE),
        indices  = {@Index(value = "userId", name = "userId_Index", unique = false)})
public class UnitTransaction implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "userId")
    @NonNull
    public long userId;

    @ColumnInfo(name = "time")
    public long time;

    @ColumnInfo(name = "amount")
    public float amount;

    public String getDate(){
        return Utils.format(new Date(time));
    }

    public UnitTransaction(){

    }

    private UnitTransaction(Parcel source){
        this.id = source.readLong();
        this.userId = source.readLong();
        this.amount = source.readFloat();
        this.time = source.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(userId);
        dest.writeFloat(amount);
        dest.writeLong(time);
    }

    public static Creator<UnitTransaction> CREATOR = new Creator<UnitTransaction>() {
        @Override
        public UnitTransaction createFromParcel(Parcel source) {
            return new UnitTransaction(source);
        }

        @Override
        public UnitTransaction[] newArray(int size) {
            return new UnitTransaction[size];
        }
    };
}
