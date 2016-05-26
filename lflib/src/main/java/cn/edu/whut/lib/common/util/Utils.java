package cn.edu.whut.lib.common.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String date2String(Date source){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(source);
    }
    public static Date str2Date(String str){
        String format_long = "yyyy-MM-dd HH:mm:ss";
        String fromat = "yyyy-MM-dd";
        if(str.length() > fromat.length()){
            fromat = format_long;
        }
        SimpleDateFormat prase = new SimpleDateFormat(fromat);
        try {
            return prase.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String date2ShortStr(Date source){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(source);
    }
    public static float numLeft2(double num){
        return numLeft2(num, 2);
    }
    public static float numLeft2(int num){
        return numLeft2(num, 2);
    }
    public static float numLeft2(float num){
        return numLeft2(num, 2);
    }
    public static float numLeft2(double num, int n){
        BigDecimal format = new BigDecimal(num);
        return format.setScale(n, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
