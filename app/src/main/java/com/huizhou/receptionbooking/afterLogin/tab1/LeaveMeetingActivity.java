package com.huizhou.receptionbooking.afterLogin.tab1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.request.GetMeetingConfirmByMeetingIdAndPhoneReq;
import com.huizhou.receptionbooking.request.SaveMeetingConfirmReq;
import com.huizhou.receptionbooking.request.UpdateMeetingConfirmByMeetingIdAndPhoneReq;
import com.huizhou.receptionbooking.response.GetMeetingConfirmByMeetingIdAndPhoneResp;
import com.huizhou.receptionbooking.response.SaveMeetingConfirmResp;
import com.huizhou.receptionbooking.response.UpdateMeetingConfirmByMeetingIdAndPhoneResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

/**
 * 会议请假
 */
public class LeaveMeetingActivity extends AppCompatActivity
{
    private String id;
    private String userName;
    private XTextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_meeting);

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");

        Intent i = getIntent();
        id = i.getStringExtra("id");
        tv = (XTextView) this.findViewById(R.id.leaveMeetingBack);
        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });


    }

    public void leaveMeetingSave(View view)
    {
        EditText reson = (EditText) findViewById(R.id.leaveMeetingReason);

        String temp = "";
        if (null != reson.getText())
        {
            temp = reson.getText().toString();
        }

        MyLeaveMeetingTask myTask = new MyLeaveMeetingTask();
        myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, temp);
    }

    /**
     * 会议请假
     */
    private class MyLeaveMeetingTask extends AsyncTask<String, Integer, String>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(String... params)
        {
            GetMeetingConfirmByMeetingIdAndPhoneReq
                    req = new GetMeetingConfirmByMeetingIdAndPhoneReq();
            req.setOperatorId(userName);
            req.setMeetingId(Integer.parseInt(id));
            req.setPhone(userName);
            String result = HttpClientClass.httpPost(req, "getMeetingConfirmByMeetingIdAndPhone");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            GetMeetingConfirmByMeetingIdAndPhoneResp info = gson.fromJson(result, GetMeetingConfirmByMeetingIdAndPhoneResp.class);
            if (null != info)
            {
                if (0 == info.getResultCode())
                {
                    if (null != info.getInfo())
                    {
                        //表示之前保存一条参加会议的信息
                        //为空，表示新插入记录
                        UpdateMeetingConfirmByMeetingIdAndPhoneReq
                                req2 = new UpdateMeetingConfirmByMeetingIdAndPhoneReq();
                        req2.setOperatorId(userName);
                        req2.setMeetingId(Integer.parseInt(id));
                        req2.setPhone(userName);
                        req2.setAttendType(2);
                        req2.setReason(params[0]);
                        String result2 = HttpClientClass.httpPost(req, "updateMeetingConfirmByMeetingIdAndPhone");

                        if (StringUtils.isBlank(result2))
                        {
                            return null;
                        }

                        Gson gson2 = new Gson();
                        UpdateMeetingConfirmByMeetingIdAndPhoneResp info2 = gson2.fromJson(
                                result2,
                                UpdateMeetingConfirmByMeetingIdAndPhoneResp.class
                        );
                        if (null != info2)
                        {
                            if (0 == info2.getResultCode())
                            {
                                return "OK";
                            }
                        }
                    }
                    else
                    {
                        //为空，表示新插入记录
                        SaveMeetingConfirmReq
                                req2 = new SaveMeetingConfirmReq();
                        req2.setOperatorId(userName);
                        req2.setMeetingId(Integer.parseInt(id));
                        req2.setPhone(userName);
                        req2.setAttendType(2);
                        req2.setReason(params[0]);
                        String result2 = HttpClientClass.httpPost(req, "saveMeetingConfirm");

                        if (StringUtils.isBlank(result2))
                        {
                            return null;
                        }

                        Gson gson2 = new Gson();
                        SaveMeetingConfirmResp info2 = gson2.fromJson(result2, SaveMeetingConfirmResp.class);
                        if (null != info2)
                        {
                            if (0 == info2.getResultCode())
                            {
                                return "OK";
                            }
                        }
                    }
                }
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
        protected void onPostExecute(String result)
        {
            if (!"OK".equals(result))
            {
                Toast tos = Toast.makeText(LeaveMeetingActivity.this, "保存数据失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }
            else
            {
                Toast tos = Toast.makeText(LeaveMeetingActivity.this, "请假成功!", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }

        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }


}
