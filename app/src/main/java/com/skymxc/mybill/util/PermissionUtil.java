package com.skymxc.mybill.util;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.PermissionChecker;

/**
 * Created by sky-mxc
 * 权限帮助类
 */
public class PermissionUtil {
    private static final String TAG = "PermissionUtil";

    /**
     * 检查当前系统版本 是否需要请求权限
     * @return
     */
    public static boolean checkVersion(){
      return   Build.VERSION.SDK_INT>=Build.VERSION_CODES.M;
    }

    /**
     * 检查当前targetSdkVersion 是不是在23以上
     * @param context
     * @return
     */
//    public static boolean checkTargetSdk(Context context){
//        try {
//            PackageInfo info= context.getPackageManager().getPackageInfo(context.getPackageName(),0);
//            return Build.VERSION.SDK_INT>=info.applicationInfo.targetSdkVersion;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return  false;
//    }

    /**
     * 检查是否拥有权限
     * @param context
     * @param permissions 权限
     * @return
     */
    public static boolean checkPermission(Context context,String[] permissions){
        int result =-1;
        for (int i=0;i<permissions.length;i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                result = context.checkSelfPermission(permissions[i]);
            } else {
                result = PermissionChecker.checkSelfPermission(context, permissions[i]);
            }
            switch (result){
                case PermissionChecker.PERMISSION_DENIED:
                    case PermissionChecker.PERMISSION_DENIED_APP_OP:
                    return false;
            }
        }



        return  true;
    }


}
