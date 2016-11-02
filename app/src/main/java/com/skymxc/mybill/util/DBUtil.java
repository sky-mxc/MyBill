package com.skymxc.mybill.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.alibaba.fastjson.JSON;
import com.skymxc.mybill.entity.Bill;
import com.skymxc.mybill.entity.BillType;
import com.skymxc.mybill.entity.CurrencyType;
import com.skymxc.mybill.entity.PayType;
import com.skymxc.mybill.view.Item;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static com.activeandroid.ActiveAndroid.getDatabase;

/**
 * Created by sky-mxc
 * 数据库工具类
 */
public class DBUtil {
    private static final String TAG = "DBUtil";

    /**
     * 初始化类型数据
     */
    public static void initData(Context context) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(context.getAssets().open("type.data"), "utf-8"));
            String billType = properties.getProperty("bill_type");
            Log.i(TAG, "initData: bill_type=" + billType);
            String currencyType = properties.getProperty("currency_type");
            Log.i(TAG, "initData: currency_type=" + currencyType);
            String payType = properties.getProperty("pay_type");
            Log.i(TAG, "initData: pay_type=" + payType);
            List<BillType> billTypes = JSON.parseArray(billType, BillType.class);
            List<CurrencyType> currencyTypes = JSON.parseArray(currencyType, CurrencyType.class);
            List<PayType> payTypes = JSON.parseArray(payType, PayType.class);
            saveAll(billTypes);
            saveAll(currencyTypes);
            saveAll(payTypes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        SDFUtil.setFirst(context);
    }


    /**
     * 保存全部数据
     *
     * @param list
     */
    private static void saveAll(List<? extends Model> list) {
        for (Model m : list) {
            m.save();
        }
    }


    /**
     * 获取全部支付类型
     *
     * @return
     */
    public static List<PayType> getPayTypes( int type) {
        return new Select().from(PayType.class).where("type = ?",type).execute();
    }

    /**
     * 获取账单类型
     *
     * @param type 类型 收入|| 支出
     * @return
     */
    public static List<BillType> getBillTypes(int type) {
        return new Select().from(BillType.class).where("type =?", type).execute();
    }

    /**
     * 获取全部货币类型
     *
     * @return
     */
    public static List<CurrencyType> getCureencyTypes() {
        return new Select().from(CurrencyType.class).execute();
    }

    /**
     * 获取 账单类型
     *
     * @param billTypeId id
     * @return 类型
     */
    public static BillType getBillType(long billTypeId) {

        return BillType.load(BillType.class, billTypeId);
    }

    /**
     * 获取支付方式
     *
     * @param payTypeId id
     * @return 支付方式对象
     */
    public static PayType getPayType(long payTypeId) {
        return PayType.load(PayType.class, payTypeId);
    }


    /**
     * 查询日期中月份的账单
     *
     * @param date yyyy/MM/dd
     * @return
     */
    public static List<Bill> getBills(Date date) {
        Date maxDate = DateUtil.getLastDayOfMonth(date);
        Date minDate = DateUtil.getFirstDayOfMonth(date);
        return new Select().from(Bill.class).where("time >=? and time <?", minDate.getTime(), maxDate.getTime()).orderBy("time").execute();
    }

    /**
     * 获取 一天的 收支情况
     *
     * @param date 日期 具体到哪一天
     * @return 收支情况
     */
    public static double getSumDay(Date date) {
        long min = DateUtil.getFirstHourOfDay(date);
        long max = DateUtil.getLastHourOfDay(date);
        List<Bill> bills = new Select().from(Bill.class).where("time >= ? and time <= ?", min, max).execute();
        double sum = 0.00;
        for (Bill bill : bills) {
            if (DBUtil.getBillType(bill.getBillTypeId()).getType() == 0) {
                //支出
                sum -= bill.getExpense();
            } else {
                //收入
                sum += bill.getExpense();
            }
        }
        return sum;
    }

    /**
     * 获取报告 数据
     * @param date
     * @return
     */
    public static List<Item> getBillsSport(Date date) {
        Log.i(TAG, "getBillsSport: ");
        List<Item> items = new ArrayList<>();
        Date maxDate = DateUtil.getLastDayOfMonth(date);
        Date minDate = DateUtil.getFirstDayOfMonth(date);
        //账单类型id icon name,钱，支出 or收入 颜色
        String sql = "select sum(b.expense) sum , b.billTypeId typeId , bt.icon icon ,  bt.type type, bt.name name , bt.color color from Bill b, BillType bt where b.billTypeId = bt.Id and time >=? and time <? group by bt.type, b.billTypeId order by  bt.type";
        Cursor cursor = getDatabase().rawQuery(sql, new String[]{minDate.getTime() + "", maxDate.getTime() + ""});
        double [] sums = getSum(date);
        if (cursor != null) {
            Log.i(TAG, "getBillsSport: " + cursor.getCount());
            while (cursor.moveToNext()) {
                Item item = new Item();
                String name = cursor.getString(cursor.getColumnIndex("name"));
                double num = cursor.getDouble(cursor.getColumnIndex("sum"));
                long btId = cursor.getLong(cursor.getColumnIndex("typeId"));
                String icon = cursor.getString(cursor.getColumnIndex("icon"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String color = cursor.getString(cursor.getColumnIndex("color"));
                int color1 = Color.parseColor(color);
                item.setName(name);
                item.setNum(num);
                item.setId(btId);
                item.setIcon(icon);
                item.setType(type);
                item.setColor(color1);
                item.setViewType(1);
                DecimalFormat df = new DecimalFormat("0.00");

                item.setScale(""+ df.format(( num*100 / sums[type])));
                item.setProgress((int)Math.ceil(num*100/sums[type]));
                if (type==1){
                    //收入
                    item.setTitle("收入分类");
                }else{
                    //支出
                    item.setTitle("支出分类");
                }

                items.add(item);
            }
            cursor.close();

        }
        return items;
    }

    /**
     * 获取 本月的 收入和支出总和
     * @param date  日期
     * @return 0 支出，1 收入
     */
    public static double[] getSum(Date date){
        Date maxDate = DateUtil.getLastDayOfMonth(date);
        Date minDate = DateUtil.getFirstDayOfMonth(date);
        double [] sums = new double[2];
        //name,钱，icon type
        String sql ="select sum(b.expense) sum, bt.type type   from Bill b,BillType bt where b.billTypeId = bt.Id and time >=? and time <? group by bt.type order by bt.type";
       Cursor cursor = ActiveAndroid.getDatabase().rawQuery(sql,new String[]{minDate.getTime() + "", maxDate.getTime() + ""});
        if (cursor!=null){
            while (cursor.moveToNext()) {
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                double sum = cursor.getDouble(cursor.getColumnIndex("sum"));
                sums[type]=sum;
            }
            cursor.close();
        }
        return sums;
    }

    public static List<Item> getPayTypeItems(Date date){
        List<Item> items = new ArrayList<>();
        Date maxDate = DateUtil.getLastDayOfMonth(date);
        Date minDate = DateUtil.getFirstDayOfMonth(date);
        //name,钱，icon type
        String sql ="select sum (b.expense) sum , pt.type type ,pt.icon icon ,pt.name name from PayType pt ,Bill b where b.payTypeId = pt.Id and  time >=? and time <? group by pt.type, b.payTypeId  order by pt.type ";

       Cursor cursor= ActiveAndroid.getDatabase().rawQuery(sql,new String[]{minDate.getTime() + "", maxDate.getTime() + ""});

        if (cursor!=null){
            while (cursor.moveToNext()){
                Item item = new Item();
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String icon = cursor.getString(cursor.getColumnIndex("icon"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                Log.i(TAG, "getPayTypeItems: type="+type);
                double sum = cursor.getDouble(cursor.getColumnIndex("sum"));
                item.setName(name);
                item.setIcon(icon);
                item.setNum(sum);
                item.setType(type);
                item.setViewType(0);
                items.add(item);
                if (type==1){
                    item.setTitle("账户收入");
                }else{
                    item.setTitle("账户支出");
                }
            }
            cursor.close();
        }


        return  items;
    }


}
