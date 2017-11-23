package com.huizhou.receptionbooking.startApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.huizhou.receptionbooking.LoginActivity;
import com.huizhou.receptionbooking.utils.Log4J;
import com.huizhou.receptionbooking.utils.SpUtils;


/**
 * @desc 启动屏
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置log文件权限
        Log4J.verifyStoragePermissions(this);

        // 判断是否是第一次开启应用
        int isFirstOpen = SpUtils.getIsFirstOpen();
        // 如果是第一次启动，则先进入功能引导页
        if (isFirstOpen == 0) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        enterLoginActivity();
    }

    private void enterLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
