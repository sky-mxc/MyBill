package com.skymxc.mybill.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sky-mxc
 */
public class DateUtil {
    private static final String TAG = "DateUtil";
    private static Calendar calendar = Calendar.getInstance();

    /**
     * 获取当前 年份
     * @return
     */
    public static int getCurrentYear(){
        return  calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     * @return
     */
    public static int getCurrentMonth(){
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取当前日期  天
     * @return
     */
    public static int getCurrentDay(){
        return  calendar.get(Calendar.DAY_OF_MONTH);
    }




    /**
     * 获取日期格式  yyyy/MM/dd E
     * @return
     */
    public static String getStringFormart( Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd E");
       return sdf.format(date);
    }

    /**
     * 传入日期 返回日期是周几
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        Log.i(TAG, "getWeekOfDate: formartWeek= "+date.getYear()+"/"+(date.getMonth()+1)+"/"+date.getDate());
        return sdf.format(date);
    }



    /**
     * 将字符串格式化为
     * @param dateStr
     * @return
     */
    public static Date getDateFormart(String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date =null;
        try {
             date= sdf.parse(dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
     return date;
    }

    /**
     * 返回时间 字符串 yyyy/MM/dd E
     * @param time
     * @return
     */
    public static String getDateString(long time) {
        Date date = new Date(time);
        return  getStringFormart(date);
    }
}
