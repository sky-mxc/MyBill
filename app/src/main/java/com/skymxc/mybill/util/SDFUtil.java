package com.skymxc.mybill.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.skymxc.mybill.R;

/**
 * Created by sky-mxc
 * SharedPreferences 工具类
 */
public class SDFUtil {
    private static final String TAG = "SDFUtil";

    /**
     * 获取 SharedPreferences 实例
     * @param context 上下文
     * @return
     */
    private static SharedPreferences getInstance(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 是否是第一加载
     * @param context 上下文
     * @return
     */
    public static boolean isFirst(Context context){
        return  getInstance(context).getBoolean(context.getString(R.string.is_first_key),true);
    }

    /**
     * 设置 不是第一次加载
     * @param context
     */
    public static void setFirst(Context context){
        getInstance(context).edit().putBoolean(context.getString(R.string.is_first_key),false).apply();
    }
}
