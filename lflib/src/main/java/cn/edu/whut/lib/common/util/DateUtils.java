package cn.edu.whut.lib.common.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date getFirstDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        //设置周一才是一周的开始
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }
    public static Date getLastDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        //设置周一才是一周的开始
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        return calendar.getTime();
    }
    public static Date getFirstDayOfMonth(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
    public static Date getLastDayOfMonth(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

}
