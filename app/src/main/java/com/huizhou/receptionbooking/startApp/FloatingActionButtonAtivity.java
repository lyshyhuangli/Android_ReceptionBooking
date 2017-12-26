package com.huizhou.receptionbooking.startApp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.huizhou.receptionbooking.LoginActivity;
import com.huizhou.receptionbooking.R;

public class FloatingActionButtonAtivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_action_button_ativity);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.3);
        p.height = 149;   //高度设置为屏幕的1.0
        p.width = 270;    //宽度设置为屏幕的0.8
        p.dimAmount = 0.2f;      //设置黑暗度
       // p.type= WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA  ;
        p.y = 40;
        //p.x=0;
        p.gravity = Gravity.CENTER | Gravity.BOTTOM;
        getWindow().setAttributes(p);     //设置生效
    }

    public void skip(View view)
    {
        Intent intent = new Intent(FloatingActionButtonAtivity.this, LoginActivity.class);
        startActivityForResult(intent, 100);
        finish();
    }
}
