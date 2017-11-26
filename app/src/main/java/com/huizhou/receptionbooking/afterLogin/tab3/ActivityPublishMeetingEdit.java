package com.huizhou.receptionbooking.afterLogin.tab3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huizhou.receptionbooking.R;
import com.huizhou.receptionbooking.afterLogin.contactGroup.checkbox.ActivityGroupPersonCheckBox;
import com.huizhou.receptionbooking.afterLogin.contacts.ActivityCheckBoxContactList;
import com.huizhou.receptionbooking.common.XTextView;
import com.huizhou.receptionbooking.database.vo.BookMeetingDbInfoRecord;
import com.huizhou.receptionbooking.database.vo.BookingMeetingRecord;
import com.huizhou.receptionbooking.request.GetMeetingInfoByIdReq;
import com.huizhou.receptionbooking.request.InsertPublishMeetingReq;
import com.huizhou.receptionbooking.request.UpdateMeetingInfoByIdReq;
import com.huizhou.receptionbooking.response.GetMeetingInfoByIdResp;
import com.huizhou.receptionbooking.response.InsertPublishMeetingResp;
import com.huizhou.receptionbooking.response.UpdateMeetingInfoByIdResp;
import com.huizhou.receptionbooking.utils.HttpClientClass;

import org.apache.commons.lang3.StringUtils;

public class ActivityPublishMeetingEdit extends AppCompatActivity implements TimePicker.OnTimeChangedListener
{


    int houre = 00;
    int minute = 00;
    private Context context;

    private String type;
    private String meetingRoomId;
    private String meetingDate;
    private String meetingRoom;
    private String userName;
    private String publishRoomIdAm;
    private String publishRoomIdPm;
    private int id;

    private XTextView tv;
    //在TextView上显示的字符
    private StringBuffer time;
    private TextView startTime;
    private TextView endTime;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_meeting_edit);

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        userName = userSettings.getString("loginUserName", "default");

        Intent i = getIntent();
        meetingDate = i.getStringExtra("date");
        type = i.getStringExtra("anOrPmType");
        meetingRoomId = i.getStringExtra("meetingRoomId");
        meetingRoom = i.getStringExtra("meetingRoom");
        publishRoomIdPm = i.getStringExtra("publishRoomIdPm");
        publishRoomIdAm = i.getStringExtra("publishRoomIdAm");
        if ("am".equals(type))
        {
            id = Integer.parseInt(publishRoomIdPm);
        }
        else
        {
            id = Integer.parseInt(publishRoomIdAm);
        }

        time = new StringBuffer();

        startTime = (TextView) findViewById(R.id.meetingBeginTimeEdit);
        endTime = (TextView) findViewById(R.id.meetingEndTimeEdit);

        tv = (XTextView) this.findViewById(R.id.publishMeetingBackEdit);
        //回退页面
        tv.setDrawableLeftListener(new XTextView.DrawableLeftListener()
        {
            @Override
            public void onDrawableLeftClick(View view)
            {
                onBackPressed();
            }
        });

        ShowMyTask showTask = new ShowMyTask();
        showTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    public void getStartTime(View view)
    {
        houre = 00;
        minute = 00;
        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);

        builder2.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (time.length() > 0)
                { //清除上次记录的日期
                    time.delete(0, time.length());
                }
                startTime.setText(time.append(String.valueOf(houre)).append(":").append(String.valueOf(minute)).append(""));
                dialog.dismiss();
            }
        });
        builder2.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog dialog2 = builder2.create();
        View dialogView2 = View.inflate(context, R.layout.dialog_time, null);
        TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
        timePicker.setCurrentHour(houre);
        timePicker.setCurrentMinute(minute);
        timePicker.setIs24HourView(true); //设置24小时制
        timePicker.setOnTimeChangedListener(this);
        //dialog2.setTitle("获取时间");
        dialog2.setView(dialogView2);
        dialog2.show();
    }

    /**
     * 时间改变的监听事件
     *
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
    {
        this.houre = hourOfDay;
        this.minute = minute;
    }

    public void getEndTime(View view)
    {
        houre = 00;
        minute = 00;

        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        builder2.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (time.length() > 0)
                { //清除上次记录的日期
                    time.delete(0, time.length());
                }
                endTime.setText(time.append(String.valueOf(houre)).append(":").append(String.valueOf(minute)).append(""));
                dialog.dismiss();
            }
        });
        builder2.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog dialog2 = builder2.create();
        View dialogView2 = View.inflate(context, R.layout.dialog_time, null);
        TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
        timePicker.setCurrentHour(houre);
        timePicker.setCurrentMinute(minute);
        timePicker.setIs24HourView(true); //设置24小时制
        timePicker.setOnTimeChangedListener(this);
        //dialog2.setTitle("获取时间");
        dialog2.setView(dialogView2);
        dialog2.show();
    }

    public void getFromContactEdit(View view)
    {
        Intent it = new Intent(this, ActivityCheckBoxContactList.class);
        startActivityForResult(it, 100);
    }

    public void getFromGroupEdit(View view)
    {
        Intent it = new Intent(this, ActivityGroupPersonCheckBox.class);
        startActivityForResult(it, 100);
    }


    /**
     * 清空与会人
     *
     * @param v
     */
    public void clearPerson(View v)
    {
        TextView meetingPersonName = (TextView) findViewById(R.id.meetingPersonNameEdit);
        meetingPersonName.setText(null);
        TextView meetingPersonId = (TextView) findViewById(R.id.meetingPersonIdEdit);
        meetingPersonId.setText(null);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            String id = data.getStringExtra("id");
            String name = data.getStringExtra("name");

            TextView meetingPersonName = (TextView) findViewById(R.id.meetingPersonNameEdit);
            String tempName = meetingPersonName.getText().toString();
            if (StringUtils.isNotBlank(tempName))
            {
                meetingPersonName.setText(tempName + "," + name);
            }
            else
            {
                meetingPersonName.setText(name);
            }

            TextView meetingPersonId = (TextView) findViewById(R.id.meetingPersonIdEdit);
            String tempId = meetingPersonId.getText().toString();
            if (StringUtils.isNotBlank(tempId))
            {
                meetingPersonId.setText(tempId + "," + id);
            }
            else
            {
                meetingPersonId.setText(id);
            }
        }
    }

    public void publishMeetingSaveEdit(View view)
    {
        BookMeetingDbInfoRecord info = new BookMeetingDbInfoRecord();
        info.setMeetingDate(meetingDate);
        info.setAmOrPm(type);
        info.setMeetingroom(meetingRoomId);

        EditText threadEt = (EditText) findViewById(R.id.threadEt);
        info.setThreaf(threadEt.getText().toString());

        TextView meetingBeginTime = (TextView) findViewById(R.id.meetingBeginTimeEdit);
        info.setStartTime(meetingBeginTime.getText().toString());

        TextView meetingEndTime = (TextView) findViewById(R.id.meetingEndTimeEdit);
        info.setEndTime(meetingEndTime.getText().toString());

        EditText meetingContent = (EditText) findViewById(R.id.meetingContentEdit);
        info.setContent(meetingContent.getText().toString());

        TextView meetingPersonId = (TextView) findViewById(R.id.meetingPersonIdEdit);
        info.setPerson(meetingPersonId.getText().toString());

        TextView meetingPersonNameEdit = (TextView) findViewById(R.id.meetingPersonNameEdit);
        info.setPersonName(meetingPersonNameEdit.getText().toString());

        EditText meetingClothes = (EditText) findViewById(R.id.meetingClothesEdit);
        info.setClothes(meetingClothes.getText().toString());

        EditText meetingDiscipline = (EditText) findViewById(R.id.meetingDisciplineEdit);
        info.setMeetingDiscipline(meetingDiscipline.getText().toString());

        EditText meetingConnectPerson = (EditText) findViewById(R.id.meetingConnectPersonEdit);
        info.setConnectPerson(meetingConnectPerson.getText().toString());

        EditText meetingConnectPhone = (EditText) findViewById(R.id.meetingConnectPhoneEdit);
        info.setConnectPhone(meetingConnectPhone.getText().toString());

        EditText meetingRemark = (EditText) findViewById(R.id.meetingRemark);
        info.setRemark(meetingRemark.getText().toString());

        MyTask m = new MyTask();
        m.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, info);


    }

    private class MyTask extends AsyncTask<BookMeetingDbInfoRecord, Integer, Integer>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Integer doInBackground(BookMeetingDbInfoRecord... params)
        {
            UpdateMeetingInfoByIdReq
                    req = new UpdateMeetingInfoByIdReq();
            req.setOperatorId(userName);

            req.setAmOrPm(params[0].getAmOrPm());
            req.setBookUser(params[0].getBookUser());
            req.setClothes(params[0].getClothes());
            req.setConnectPerson(params[0].getConnectPerson());
            req.setConnectPhone(params[0].getConnectPhone());
            req.setContent(params[0].getContent());
            req.setCreateTime(params[0].getCreateTime());
            req.setEndTime(params[0].getEndTime());
            req.setFiles(params[0].getFiles());
            req.setMeetingDate(params[0].getMeetingDate());
            req.setMeetingDiscipline(params[0].getMeetingDiscipline());
            req.setMeetingroom(params[0].getMeetingroom());
            req.setPerson(params[0].getPerson());
            req.setQRcode(params[0].getQRcode());
            req.setRemark(params[0].getRemark());
            req.setStartTime(params[0].getStartTime());
            req.setThreaf(params[0].getThreaf());
            req.setWakeType(params[0].getWakeType());

            String result = HttpClientClass.httpPost(req, "updateMeetingInfoById");

            if (StringUtils.isBlank(result))
            {
                return null;
            }

            Gson gson = new Gson();
            UpdateMeetingInfoByIdResp info = gson.fromJson(result, UpdateMeetingInfoByIdResp.class);
            if (null != info)
            {
                if (0 == info.getResultCode())
                {
                    return info.getResult();
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
        protected void onPostExecute(Integer result)
        {

            if (null == result || result == 0)
            {
                Toast tos = Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }
            else
            {
                Toast tos = Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG);
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

    private class ShowMyTask extends AsyncTask<BookMeetingDbInfoRecord, Integer, BookMeetingDbInfoRecord>
    {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute()
        {

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected BookMeetingDbInfoRecord doInBackground(BookMeetingDbInfoRecord... params)
        {
            GetMeetingInfoByIdReq
                    req = new GetMeetingInfoByIdReq();
            req.setOperatorId(userName);
            req.setId(id);

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
                Toast tos = Toast.makeText(getApplicationContext(), "查询会议信息失败", Toast.LENGTH_LONG);
                tos.setGravity(Gravity.CENTER, 0, 0);
                tos.show();
            }


            EditText threadEt = (EditText) findViewById(R.id.threadEt);
            threadEt.setText(result.getThreaf());

            TextView meetingBeginTime = (TextView) findViewById(R.id.meetingBeginTimeEdit);
            meetingBeginTime.setText(result.getStartTime());

            TextView meetingEndTime = (TextView) findViewById(R.id.meetingEndTimeEdit);
            meetingEndTime.setText(result.getEndTime());

            EditText meetingContent = (EditText) findViewById(R.id.meetingContentEdit);
            meetingContent.setText(result.getContent());

            TextView meetingPersonId = (TextView) findViewById(R.id.meetingPersonIdEdit);
            meetingPersonId.setText(result.getPerson());

            TextView meetingPersonNameEdit = (TextView) findViewById(R.id.meetingPersonNameEdit);
            meetingPersonNameEdit.setText(result.getPersonName());

            EditText meetingClothes = (EditText) findViewById(R.id.meetingClothesEdit);
            meetingClothes.setText(result.getClothes());

            EditText meetingDiscipline = (EditText) findViewById(R.id.meetingDisciplineEdit);
            meetingDiscipline.setText(result.getMeetingDiscipline());

            EditText meetingConnectPerson = (EditText) findViewById(R.id.meetingConnectPersonEdit);
            meetingConnectPerson.setText(result.getConnectPerson());

            EditText meetingConnectPhone = (EditText) findViewById(R.id.meetingConnectPhoneEdit);
            meetingConnectPhone.setText(result.getConnectPhone());

            EditText meetingRemark = (EditText) findViewById(R.id.meetingRemark);
            meetingRemark.setText(result.getRemark());

            //如果不是自己发布的会议，则隐藏保存按钮，只查看信息
            String bookerUser = result.getBookUser();
            if (!bookerUser.equals(userName))
            {
                Button publishMeetingSaveEdit = (Button) findViewById(R.id.publishMeetingSaveEdit);
                publishMeetingSaveEdit.setVisibility(View.GONE);
            }

        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled()
        {

        }
    }


}
