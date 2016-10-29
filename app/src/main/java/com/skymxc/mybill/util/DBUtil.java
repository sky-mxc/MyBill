package com.skymxc.mybill.util;

import android.content.Context;
import android.util.Log;

import com.activeandroid.Model;
import com.alibaba.fastjson.JSON;
import com.skymxc.mybill.entity.BillType;
import com.skymxc.mybill.entity.CurrencyType;
import com.skymxc.mybill.entity.PayType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

/**
 * Created by sky-mxc
 * 数据库工具类
 */
public class DBUtil {
    private static final String TAG = "DBUtil";

    /**
     * 初始化类型数据
     */
    public static void initData(Context context){
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(context.getAssets().open("type.data"),"utf-8"));
            String billType = properties.getProperty("bill_type");
            Log.i(TAG, "initData: bill_type="+billType);
            String currencyType = properties.getProperty("currency_type");
            Log.i(TAG, "initData: currency_type="+currencyType);
            String payType = properties.getProperty("pay_type");
            Log.i(TAG, "initData: pay_type="+payType);
            List<BillType> billTypes = JSON.parseArray(billType,BillType.class);
            List<CurrencyType> currencyTypes = JSON.parseArray(currencyType,CurrencyType.class);
            List<PayType> payTypes = JSON.parseArray(payType,PayType.class);
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
     * @param list
     */
    private  static void saveAll(List<? extends Model> list) {
      for(Model m :list){
          m.save();
      }
    }


}
