package com.huizhou.receptionbooking.afterLogin.tab1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.vo.MeetingConfirmRecord;
import com.huizhou.receptionbooking.request.ViewMeetingConfirmInfoReq;
import com.huizhou.receptionbooking.response.ViewMeetingConfirmInfoResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ViewMeetingConfirmInfoActivity extends AppCompatActivity
{

    private XTextView tv;
    private ListView listView;

    private MyTask myTask;
    private String userName;
    private ViewMeetingConfirmInfoAdapter adapter;

    private int id;
    private String meetingBookUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meeting_confirm_info);

        Intent i = getIntent();
        //会议ID
        id = Integer.parseInt(i.getStringExtra("id"));
        meetingBookUser = i.getStringExtra("meetingBookUser");

        tv = (XTextView) this.findViewById(R.id.viewMeetingAttendBack);
        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");

        listView = (ListView) findViewById(R.id.viewMeetingAttendDataList);
        loadFirstTime();

    }

    /**
     * 第一次加载数据
     */
    private void loadFirstTime()
    {
        myTask = new MyTask();
        myTask.execute();
    }


    private class MyTask extends AsyncTask<String, Integer, List<MeetingConfirmRecord>>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected List<MeetingConfirmRecord> doInBackground(String... params)
        {
            ViewMeetingConfirmInfoReq
                    req = new ViewMeetingConfirmInfoReq();
            req.setMeetingId(id);
            req.setOperatorId(userName);
            try
            {
                String result = HttpClientClass.httpPost(req, "viewMeetingConfirmInfoByMeetingId");

                if (StringUtils.isBlank(result))
                {
                    return null;
                }

                Gson gson = new Gson();
                ViewMeetingConfirmInfoResp info = gson.fromJson(result, ViewMeetingConfirmInfoResp.class);
                if (null != info)
                {
                    if (0 == info.getResultCode())
                    {
                        return info.getInfo();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses)
        {

        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(List<MeetingConfirmRecord> result)
        {
            if (null == result)
            {
                Toast tos = Toast.makeText(ViewMeetingConfirmInfoActivity.this, "查询数据失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
                return;
            }

            if (!meetingBookUser.equals(userName))
            {
                List<MeetingConfirmRecord> temp = new ArrayList<>();
                for (MeetingConfirmRecord m : result)
                {
                    if (userName.equals(m.getPhone()))
                    {
                        temp.add(m);
                        break;
                    }
                }

                adapter = new ViewMeetingConfirmInfoAdapter(ViewMeetingConfirmInfoActivity.this, temp);
            }
            else
            {
                adapter = new ViewMeetingConfirmInfoAdapter(ViewMeetingConfirmInfoActivity.this, result);
            }
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }

}
