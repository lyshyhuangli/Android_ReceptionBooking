package com.huizhou.receptionbooking.afterLogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.bookDining.BookDiningListActivity;
import com.huizhou.receptionbooking.afterLogin.definePublishMeeting.DefinePublishMeetingActivity;
import com.huizhou.receptionbooking.afterLogin.dinningMenu.CookBookListActivity;
import com.xys.libzxing.zxing.activity.CaptureActivity;

public class CommonMenuActitity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_menu_actitity);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
       // p.height = (int) (d.getHeight() * 0.3);
        p.height = 450;   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.4);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.0f;      //设置黑暗度
        p.y=40;
        //p.x=0;
        p.gravity = Gravity.RIGHT| Gravity.TOP;
        getWindow().setAttributes(p);     //设置生效
        //getWindow().setGravity(Gravity.TOP);       //设置靠右对齐
    }

    public void scan(View view)
    {
        startActivityForResult(new Intent(this, CaptureActivity.class), 0);
        finish();
    }

    public void definePublishMeeting(View view)
    {
        startActivityForResult(new Intent(this, DefinePublishMeetingActivity.class), 0);
        finish();
    }

    public void defineCookbook(View view)
    {
        startActivityForResult(new Intent(this, CookBookListActivity.class), 0);
        finish();
    }

   public void  defineBookDining(View view)
   {
       startActivityForResult(new Intent(this, BookDiningListActivity.class), 0);
       finish();
   }
}
