package com.naren.lendage.data;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class LoanType {

    public enum Type{
        INTEREST,
        EMI,
        NONE
    }

    public enum Period{
        MONTH,
        WEEK,
        YEAR
    }

    @SerializedName("type")
    public Type type;

    @SerializedName("period")
    public Period period;

    @SerializedName("installments")
    public int installments;

    @SerializedName("interest")
    public int interest;

    public LoanType(){
        this.type = Type.NONE;
        this.interest = 0;
        this.period = Period.YEAR;
        this.installments = 0;
    }

    public LoanType(int interest, Period period){
        this.type = Type.INTEREST;
        this.interest = interest;
        this.period = period;
        this.installments = 0;
    }

    public LoanType(int interest, Period period, int installments){
        this.type = Type.EMI;
        this.interest = interest;
        this.period = period;
        this.installments = installments;
    }

    @Override
    public String toString(){
        return new GsonBuilder().create().toJson(this);
    }

    public static LoanType fromJson(String json){
        return new GsonBuilder().create().fromJson(json, LoanType.class);
    }
}
