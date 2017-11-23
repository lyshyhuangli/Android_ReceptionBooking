package com.huizhou.receptionbooking.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;


/**
 * 日志工具类
 * Created by Administrator on 2017/8/1.
 */

public class Log4J {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * 获取日志函数
     * @param clazzName
     * @return
     */
//    public static Logger getLogger(Class clazzName) {
//        //verifyStoragePermissions(activity);
//        //加载配置
//        ConfigureLog4J configureLog4J = new ConfigureLog4J();
//        configureLog4J.configure();
//        return Logger.getLogger(clazzName.getClass());
//    }



}
