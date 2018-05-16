package com.naren.lendage.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("MMM dd, yyyy");

    public static String format(Date date){
        return FORMATTER.format(date);
    }

    public static Date parse(String date){
        Date d = new Date();
        try{
            d = FORMATTER.parse(date);
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return d;
    }
}
