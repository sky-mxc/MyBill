package com.skymxc.mybill;

import android.app.Application;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.skymxc.mybill.entity.Bill;
import com.skymxc.mybill.entity.BillType;
import com.skymxc.mybill.entity.CurrencyType;
import com.skymxc.mybill.entity.PayType;

/**
 * Created by sky-mxc
 */
public class App extends Application {
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        Configuration configuration = new Configuration.Builder(this)
                .setDatabaseName("mybill.DB")
                .setDatabaseVersion(1)
                .addModelClasses(Bill.class, BillType.class, CurrencyType.class, PayType.class)
                .create();
        ActiveAndroid.initialize(configuration);
    }
}
