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
        Log.i(TAG, "getCurrentMonth: "+(calendar.get(Calendar.MONTH)+1));
        return calendar.get(Calendar.MONTH)+1;
    }

    /**
     * 获取当前日期  天
     * @return
     */
    public static int getCurrentDay(){
        return  calendar.get(Calendar.DAY_OF_MONTH);
    }


    public static Date getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,(calendar.get(Calendar.MONTH)+1));
        Log.i(TAG, "getCurrentDate: month="+calendar.get(Calendar.MONTH));
        return getDate(calendar.get(Calendar.YEAR)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.DAY_OF_MONTH));
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
     * @param dateStr 格式为 yyyy/MM/dd E
     * @return
     */
    public static Date getDateFormart(String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd E");
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

    /**
     * 转换为 Date 格式
     * @param dateStr  yyyy/MM/dd
     * @return
     */
    public static Date getDate(String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            return  sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回传入月份对应的最后一天的日期
     * @param date
     * @return yyyy/MM/dd 格式的Date对象
     */
    public static  Date getLastDayOfMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,1);
        //设置 天数 1
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return  calendar.getTime();
    }

    /**
     * 返回传入月份的 第一天 的日期
     * @param date
     * @return yyyy/MM/01
     */
    public static Date getFirstDayOfMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //设置 天数为月份的第一天
        calendar.set(Calendar.DAY_OF_MONTH,1);
        return  calendar.getTime();
    }

    /**
     * 获取 日期当天0点 long
     * @param date
     * @return
     */
    public  static long getFirstHourOfDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        return  calendar.getTime().getTime();
    }
    /**
     * 获取 第二天 0点  long
     * @param date
     * @return
     */
    public  static long getLastHourOfDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,(calendar.get(Calendar.DAY_OF_MONTH)+1));
        calendar.set(Calendar.HOUR_OF_DAY,0);
        return  calendar.getTime().getTime();
    }


    /**
     * 比较 连个日期是否在同一天
     * @param beforeTime
     * @param behindTime
     * @return true ：在同一天；false ：不在同一天
     */
    public static boolean compareInDay(long beforeTime, long behindTime) {
        Calendar beforeCalendar = Calendar.getInstance();
        beforeCalendar.setTimeInMillis(beforeTime);
        Calendar behindCalendar = Calendar.getInstance();
        behindCalendar.setTimeInMillis(behindTime);
      boolean isInDay=  beforeCalendar.get(Calendar.YEAR) == behindCalendar.get(Calendar.YEAR)      //同一年
                && beforeCalendar.get(Calendar.MONTH) == behindCalendar.get(Calendar.MONTH)         //同一月
                && beforeCalendar.get(Calendar.DAY_OF_MONTH) == behindCalendar.get(Calendar.DAY_OF_MONTH);  //同一天
        return  isInDay;
    }
}
