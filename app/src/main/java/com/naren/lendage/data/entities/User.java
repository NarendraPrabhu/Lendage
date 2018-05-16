package com.naren.lendage.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.databinding.Bindable;
import android.databinding.InverseBindingMethod;
import android.databinding.InverseBindingMethods;
import android.databinding.InverseMethod;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Spinner;

import com.naren.lendage.data.LoanType;
import com.naren.lendage.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

@InverseBindingMethods({
        @InverseBindingMethod(type = Spinner.class, attribute="android:selectedItemPosition"),
})
@Entity(tableName = "user")
public class User implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    public String description;

    public long time = System.currentTimeMillis();

    public float amount;

    public LoanType type = new LoanType(2, LoanType.Period.MONTH);

    public float balance;

    public long lastTransactionDate;

    public long lastUpdatedTime;

    public String getDate(){
        return Utils.format(new Date(time));
    }

    public String getTypeString(){
        return type.type != LoanType.Type.NONE ? type.type.name() : "Shared";
    }

    public User(){
    }

    private User(Parcel parcel){
        this.id = parcel.readLong();
        this.name = parcel.readString();
        this.description = parcel.readString();
        this.amount = parcel.readFloat();
        this.time = parcel.readLong();
        this.balance = parcel.readFloat();
        this.type = LoanType.fromJson(parcel.readString());
        this.lastTransactionDate = parcel.readLong();
        this.lastUpdatedTime = parcel.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeFloat(amount);
        dest.writeLong(time);
        dest.writeFloat(balance);
        dest.writeString(type.toString());
        dest.writeLong(lastTransactionDate);
        dest.writeLong(lastUpdatedTime);
    }

    public static Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @TypeConverter
    public static String fromType(LoanType type){
        return type.toString();
    }

    @TypeConverter
    public static LoanType toType(String value){
        return LoanType.fromJson(value);
    }

}
