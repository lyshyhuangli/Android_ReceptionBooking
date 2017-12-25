package com.huizhou.receptionbooking.afterLogin.tab1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.vo.BookMeetingDbInfoRecord;
import com.huizhou.receptionbooking.request.GetMeetingConfirmByMeetingIdAndPhoneReq;
import com.huizhou.receptionbooking.request.GetMeetingInfoByIdReq;
import com.huizhou.receptionbooking.request.SaveMeetingConfirmReq;
import com.huizhou.receptionbooking.request.UpdateMeetingConfirmByMeetingIdAndPhoneReq;
import com.huizhou.receptionbooking.response.GetMeetingConfirmByMeetingIdAndPhoneResp;
import com.huizhou.receptionbooking.response.GetMeetingInfoByIdResp;
import com.huizhou.receptionbooking.response.SaveMeetingConfirmResp;
import com.huizhou.receptionbooking.response.UpdateMeetingConfirmByMeetingIdAndPhoneResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

public class ViewAndConfirmMeetingActivity extends AppCompatActivity
{
    private String id;
    private String departmentItem;
    private String threaf;
    private String meetingRoom;
    private String userName;
    private String loginShowName;
    private XTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_confirm_meeting);

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");
        loginShowName = userSettings.getString("loginShowName", "default");

        tv = (XTextView) this.findViewById(R.id.viewAndConfirmBack);

        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });


        Intent i = getIntent();
        id = i.getStringExtra("id");
        meetingRoom = i.getStringExtra("meetingRoom");
        departmentItem = i.getStringExtra("departmentItem");

        MyShowTask showTask = new MyShowTask();
        showTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    /**
     * 待开会议
     */
    private class MyShowTask extends AsyncTask<String, Integer, BookMeetingDbInfoRecord>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected BookMeetingDbInfoRecord doInBackground(String... params)
        {
            GetMeetingInfoByIdReq
                    req = new GetMeetingInfoByIdReq();
            req.setOperatorId(userName);
            req.setId(Integer.parseInt(id));

            String result = HttpClientClass.httpPost(req, "getMeetingInfoById");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            GetMeetingInfoByIdResp info = gson.fromJson(result, GetMeetingInfoByIdResp.class);
            if (null != info)
            {
                if (0 == info.getResultCode())
                {
                    return info.getInfo();
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
        protected void onPostExecute(BookMeetingDbInfoRecord result)
        {
            if (null == result)
            {
                Toast tos = Toast.makeText(ViewAndConfirmMeetingActivity.this, "查询会议信息失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }

            if(!userName.equals(result.getBookUser()))
            {
                LinearLayout ll  = (LinearLayout) findViewById(R.id.viewAdminMeetingLl) ;
                ll.setVisibility(View.GONE);
            }

            TextView viewAndConfirmThreaf = (TextView) findViewById(R.id.viewAndConfirmThreaf);
            viewAndConfirmThreaf.setText(result.getThreaf());
            threaf = result.getThreaf().toString();

            TextView meetingTime = (TextView) findViewById(R.id.meetingTime);
            meetingTime.setText(result.getMeetingDate() + " " + result.getStartTime() + "-" + result.getEndTime());

            TextView viewMeetingContent = (TextView) findViewById(R.id.viewMeetingContent);
            viewMeetingContent.setText(result.getContent());

            TextView viewMeetingRoom = (TextView) findViewById(R.id.viewMeetingRoom);
            viewMeetingRoom.setText(meetingRoom);

            TextView viewMeetingClothes = (TextView)findViewById(R.id.viewMeetingClothes);
            viewMeetingClothes.setText(result.getClothes());

            TextView viewMeetingDiscipline = (TextView) findViewById(R.id.viewMeetingDiscipline);
            viewMeetingDiscipline.setText(result.getMeetingDiscipline());

            TextView viewMeetingConnectPerson = (TextView) findViewById(R.id.viewMeetingConnectPerson);
            viewMeetingConnectPerson.setText(result.getConnectPerson());

            TextView viewMeetingConnectPhone = (TextView) findViewById(R.id.viewMeetingConnectPhone);
            viewMeetingConnectPhone.setText(result.getConnectPhone());

            TextView viewMeetingzuzhiDep = (TextView) findViewById(R.id.viewMeetingzuzhiDep);
            viewMeetingzuzhiDep.setText(result.getDepartmentName());

            TextView viewMeetingRemark = (TextView) findViewById(R.id.viewMeetingRemark);
            viewMeetingRemark.setText(result.getRemark());
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }

    /**
     * 确认参加
     *
     * @param view
     */
    public void confirmMeeting(View view)
    {
        MyAttendMeetingTask myTask = new MyAttendMeetingTask();
        myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 转发会议
     *
     * @param view
     */
    public void republishMeeting(View view)
    {
        //跳转到转发会议页面
        Intent it = new Intent(this, republishMeetingToOtherActivity.class);
        it.putExtra("id", id);
        startActivity(it);
    }

    /**
     * 生成二维码
     */
    public void makeQRCode(View view)
    {
        Intent it = new Intent(this, MakeQRCodeActivity.class);
        it.putExtra("id", id);
        it.putExtra("thread",threaf);
        it.putExtra("department",departmentItem);
        startActivity(it);

    }

    public void viewMeetingConfirmInfo(View view)
    {
        Intent it = new Intent(this, ViewMeetingConfirmInfoActivity.class);
        it.putExtra("id", id);
        startActivity(it);
    }

    /**
     * 请假
     *
     * @param view
     */
    public void leaveMeeting(View view)
    {
        //跳转到请假页面
        Intent it = new Intent(this, LeaveMeetingActivity.class);
        it.putExtra("id", id);
        startActivity(it);
    }

    /**
     * 确认参加会议
     */
    private class MyAttendMeetingTask extends AsyncTask<Integer, Integer, String>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(Integer... params)
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
                        req2.setAttendType(1);
                        req2.setReason("");
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
                        req2.setUserName(loginShowName);
                        req2.setAttendType(1);
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
                Toast tos = Toast.makeText(ViewAndConfirmMeetingActivity.this, "保存数据失败，请检查网络或重试。", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }
            else
            {
                Toast tos = Toast.makeText(ViewAndConfirmMeetingActivity.this, "确认成功，请准时参加!", Toast.LENGTH_LONG);
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
