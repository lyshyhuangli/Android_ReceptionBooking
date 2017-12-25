package com.huizhou.receptionbooking.utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.definePublishMeeting.DefineMeetingViewActivity;
import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityCalendarPickerView extends AppCompatActivity
{
    CalendarPickerView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_picker_view);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.7);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.0f;      //设置黑暗度
        getWindow().setAttributes(p);     //设置生效
        getWindow().setGravity(Gravity.RIGHT);       //设置靠右对齐
        
        initView();
    }

    private void initView()
    {
        calendar = (CalendarPickerView) findViewById(R.id.calendar_picker);
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener()
        {
            @Override
            public void onDateSelected(final Date date)
            {
                final int size = calendar.getSelectedDates().size();
                Intent intent = new Intent(ActivityCalendarPickerView.this, DefineMeetingViewActivity.class);
                intent.putExtra("result", formatDate(date));
                setResult(RESULT_OK, intent);
                onBackPressed();
            }

            @Override
            public void onDateUnselected(Date date)
            {
            }
        });

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        Calendar oldYear = Calendar.getInstance();
        oldYear.add(Calendar.YEAR, -3);

        Date today = new Date();
        calendar.init(oldYear.getTime(), nextYear.getTime()).withSelectedDate(today);

        //默认是只选择一个日期，如果想要选择多个日期，使用下面这行代码
        //calendar.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.RANGE);
    }

    private String formatDate(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }
}
