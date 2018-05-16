package com.naren.lendage.data.entities;

import android.databinding.InverseMethod;
import android.text.TextUtils;
import android.view.View;

import com.naren.lendage.data.LoanType;
import com.naren.lendage.utils.Utils;

import java.util.Date;

public class Converter {

    @InverseMethod("fromFloat")
    public static float toFloat(String s){
        if(TextUtils.isEmpty(s)){ return 0;}
        return Float.parseFloat(s);
    }

    public static String fromFloat(float f){
        return Float.toString(f);
    }

    @InverseMethod("fromInteger")
    public static int toInteger(String s){
        if(TextUtils.isEmpty(s)){ return 0;}
        return Integer.parseInt(s);
    }

    public static String fromInteger(int i){
        return Integer.toString(i);
    }


    @InverseMethod("fromPeriod")
    public static LoanType.Period toPeriod(int i){
        return LoanType.Period.values()[i];
    }

    public static int fromPeriod(LoanType.Period p){
        return p.ordinal();
    }

    @InverseMethod("fromLoanType")
    public static LoanType.Type toLoanType(int i){
        return LoanType.Type.values()[i];
    }

    public static int fromLoanType(LoanType.Type t){
        return t.ordinal();
    }

    @InverseMethod("fromDate")
    public static long toDate(String s){
        return Utils.parse(s).getTime();
    }

    public static String fromDate(long t){
        return Utils.format(new Date(t));
    }

    public static int visibility(LoanType.Type type){
        return type == LoanType.Type.EMI ? View.VISIBLE : View.GONE;
    }

    @InverseMethod("visibility")
    public static int inverseVisibility(int visibility){
        return visibility;
    }

}
