package com.huizhou.receptionbooking.afterLogin.tab1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.contactGroup.checkbox.ActivityGroupPersonCheckBox;
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityCheckBoxContactList;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.request.GetMeetingConfirmByMeetingIdAndPhoneReq;
import com.huizhou.receptionbooking.request.GetMeetingInfoByIdReq;
import com.huizhou.receptionbooking.request.SaveMeetingConfirmReq;
import com.huizhou.receptionbooking.request.UpdateMeetingConfirmByMeetingIdAndPhoneReq;
import com.huizhou.receptionbooking.request.UpdateMeetingInfoByIdReq;
import com.huizhou.receptionbooking.response.GetMeetingConfirmByMeetingIdAndPhoneResp;
import com.huizhou.receptionbooking.response.GetMeetingInfoByIdResp;
import com.huizhou.receptionbooking.response.SaveMeetingConfirmResp;
import com.huizhou.receptionbooking.response.UpdateMeetingConfirmByMeetingIdAndPhoneResp;
import com.huizhou.receptionbooking.response.UpdateMeetingInfoByIdResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

public class republishMeetingToOtherActivity extends AppCompatActivity
{
    private String id;
    private String userName;
    private String showName;
    private XTextView tv;

    private TextView republishAttendPerson;
    private TextView republishAttendPersonId;
    private EditText republishAttendReason;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_republish_meeting_to_other);
        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");
        showName= userSettings.getString("loginShowName", "default");

        Intent i = getIntent();
        //会议ID
        id = i.getStringExtra("id");

        tv = (XTextView) this.findViewById(R.id.republishMeetingBack);
        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });

        republishAttendPerson = (TextView) findViewById(R.id.republishAttendPerson);
        republishAttendPersonId = (TextView) findViewById(R.id.republishAttendPersonId);

        republishAttendReason = (EditText) findViewById(R.id.republishAttendReason);
    }

    public void republishFromContact(View view)
    {
        Intent it = new Intent(this, ActivityCheckBoxContactList.class);
        startActivityForResult(it, 100);
    }

    public void republishFromPersonGroup(View view)
    {
        Intent it = new Intent(this, ActivityGroupPersonCheckBox.class);
        startActivityForResult(it, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            String id = data.getStringExtra("id");
            String name = data.getStringExtra("name");
            republishAttendPerson.setText(name);
            republishAttendPersonId.setText(id);
        }
    }

    public void republishAttendBt(View view)
    {
        String personIds = republishAttendPersonId.getText().toString();
        String personName = republishAttendPerson.getText().toString();
        String reason = republishAttendReason.getText().toString();

        if(StringUtils.isBlank(personIds))
        {
            Toast tos = Toast.makeText(republishMeetingToOtherActivity.this, "请选择与会人!", Toast.LENGTH_SHORT);
            tos.setGravity(Gravity.CENTER, 0, 0);
            tos.show();
            return;
        }

        MyRepublishMeetingTask myRepublishMeetingTask = new MyRepublishMeetingTask();
        myRepublishMeetingTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, personIds, personName,reason);

        onBackPressed();
    }

    /**
     * 会议转发
     */
    private class MyRepublishMeetingTask extends AsyncTask<String, Integer, String>
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

            GetMeetingInfoByIdReq
                    req1 = new GetMeetingInfoByIdReq();
            req1.setOperatorId(userName);
            req1.setId(Integer.parseInt(id));

            String result1 = HttpClientClass.httpPost(req1, "getMeetingInfoById");

            if (StringUtils.isBlank(result1))
            {
                return null;
            }

            Gson gson1 = new Gson();
            GetMeetingInfoByIdResp info1 = gson1.fromJson(result1, GetMeetingInfoByIdResp.class);
            if (null != info1)
            {
                if (0 == info1.getResultCode())
                {
                    String personId = info1.getInfo().getPerson();
                    String personName = info1.getInfo().getPersonName();

                    //更新会议室的参与人
                    UpdateMeetingInfoByIdReq
                            req2 = new UpdateMeetingInfoByIdReq();
                    req2.setOperatorId(userName);
                    req2.setId(Integer.parseInt(id));
                    req2.setPerson(params[0] + "," + personId);
                    req2.setPersonName(params[1] + "," + personName);
                    String result2 = HttpClientClass.httpPost(req2, "updateMeetingInfoById");

                    if (StringUtils.isBlank(result2))
                    {
                        return null;
                    }

                    Gson gson2 = new Gson();
                    UpdateMeetingInfoByIdResp info2 = gson2.fromJson(result2, UpdateMeetingInfoByIdResp.class);
                    if (null != info2)
                    {
                        if (0 == info2.getResultCode())
                        {
                            //return "OK";
                        }
                    }

                }
            }


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
                        req2.setReason(params[2]);
                        String result2 = HttpClientClass.httpPost(req2, "updateMeetingConfirmByMeetingIdAndPhone");

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
                        req2.setReason(params[2]);
                        req2.setUserName(showName);
                        String result2 = HttpClientClass.httpPost(req2, "saveMeetingConfirm");

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
                Toast tos = Toast.makeText(republishMeetingToOtherActivity.this, "保存数据失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }
            else
            {
                Toast tos = Toast.makeText(republishMeetingToOtherActivity.this, "转发成功!", Toast.LENGTH_LONG);
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
